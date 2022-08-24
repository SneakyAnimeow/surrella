package club.anims.surrella.commands;

import club.anims.surrella.interfaces.Loggable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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

        var reflections = new Reflections("club.anims.surrella.commands.slashcommands");

        var slashCommands = reflections.get(TypesAnnotated.with(SlashCommand.class).asClass());

        for (var slashCommand : slashCommands) {
            var annotatedClass = slashCommand.getAnnotation(SlashCommand.class);

            if(annotatedClass.name().equals(command) || Arrays.asList(annotatedClass.aliases()).contains(command)) {
                try {
                    var slashCommandAdapter = (SlashCommandAdapter) slashCommand.getDeclaredConstructor(SlashCommandContext.class).newInstance(context);
                    var reply = slashCommandAdapter.execute();
                    switch (reply.getType()) {
                        case TEXT -> event.reply((String) reply.getValue()).queue();
                        case MESSAGE -> event.reply((net.dv8tion.jda.api.entities.Message) reply.getValue()).queue();
                        case EMBEDS -> event.replyEmbeds((Collection<? extends MessageEmbed>) reply.getValue()).queue();
                        case FILE -> event.replyFile((File) reply.getValue()).queue();
                        case BYTE_ARRAY -> {
                            var data = (SlashCommandReply.ByteArrayReply) reply.getValue();
                            event.replyFile(data.getBytes(), data.getName(), data.getOptions()).queue();
                        }
                        case INPUT_STREAM -> {
                            var data = (SlashCommandReply.InputStreamReply) reply.getValue();
                            event.replyFile(data.getInputStream(), data.getName(), data.getOptions()).queue();
                        }
                        default -> {}
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
