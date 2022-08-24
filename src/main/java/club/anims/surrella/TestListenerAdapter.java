package club.anims.surrella;

import club.anims.surrella.interfaces.Loggable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TestListenerAdapter extends ListenerAdapter implements Loggable {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        var command = event.getName();

        getLogger().info("Received slash command interaction: " + command);

        switch (command){
            case "ping" -> {
                event.reply("pong").queue();
            }

            case "say" -> {
                event.reply(event.getOptions().get(0).getAsString()).queue();
            }

            default -> {
                getLogger().info("Unknown command: " + command);
                event.getChannel().sendMessage("Unknown command: " + command).queue();
            }
        }
    }
}
