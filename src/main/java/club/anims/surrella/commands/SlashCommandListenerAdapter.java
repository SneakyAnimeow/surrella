package club.anims.surrella.commands;

import club.anims.surrella.database.JPAService;
import club.anims.surrella.interfaces.Loggable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class SlashCommandListenerAdapter extends ListenerAdapter implements Loggable {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        var command = event.getName();
        var context = new SlashCommandContext(event.getId(), event.getChannel(), event.getChannelType(), event.getUser(), event.getOptions());

        getLogger().info("Received slash command: %s from %s (ID: %s)".formatted(command, event.getUser().getAsTag(), event.getUser().getId()));

        var service = JPAService.getInstance();
        var em = service.getEmf().createEntityManager();

        try {
            if (em.createNamedQuery("BlacklistedUser.existsByDiscordId", Boolean.class).setParameter("discordId", event.getUser().getId()).getSingleResult()) {
                getLogger().info("%s (ID %s) got rejected from using the bot".formatted(event.getUser().getAsTag(), event.getUser().getId()));

                event.reply("You are blacklisted from using this bot").queue();
                return;
            }
        } finally {
            em.close();
        }

        var reflections = new Reflections("club.anims.surrella.commands.slashcommands");

        var slashCommands = reflections.get(TypesAnnotated.with(SlashCommand.class).asClass());

        for (var slashCommand : slashCommands) {
            var annotatedClass = slashCommand.getAnnotation(SlashCommand.class);

            if (annotatedClass.name().equals(command) || Arrays.asList(annotatedClass.aliases()).contains(command)) {
                var permission = Permission.fromMember(event.getMember());

                if (!permission.isSufficient(annotatedClass.permission())) {
                    event.reply("To use this command your permission level must be at least: " + annotatedClass.permission()).setEphemeral(true).queue();
                    return;
                }

                try {
                    var slashCommandAdapter = (SlashCommandAdapter) slashCommand.getDeclaredConstructor(SlashCommandContext.class).newInstance(context);
                    var reply = slashCommandAdapter.execute();
                    switch (reply.getType()) {
                        case TEXT -> event.reply((String) reply.getValue()).queue();

                        case MESSAGE -> event.reply((MessageCreateData) reply.getValue()).queue();

                        case EMBEDS -> event.replyEmbeds((Collection<? extends MessageEmbed>) reply.getValue()).queue();

                        case FILE -> event.replyFiles((Collection<? extends FileUpload>) reply.getValue()).queue();

//                        case BYTE_ARRAY -> {
//                            var data = (SlashCommandReply.ByteArrayReply) reply.getValue();
//                            event.replyFile(data.getBytes(), data.getName(), data.getOptions()).queue();
//                        }
//
//                        case INPUT_STREAM -> {
//                            var data = (SlashCommandReply.InputStreamReply) reply.getValue();
//                            event.replyFile(data.getInputStream(), data.getName(), data.getOptions()).queue();
//                        }

                        default -> {
                        }
                    }
                } catch (Exception e) {
                    event.reply("Failed to execute command " + annotatedClass.name()).queue();
                    getLogger().error("Failed to execute command " + annotatedClass.name(), e);
                }
                break;
            }
        }
    }
}
