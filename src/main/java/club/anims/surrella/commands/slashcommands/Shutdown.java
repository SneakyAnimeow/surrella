package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;
import java.util.TimerTask;

@SlashCommand(name = "shutdown", description = "Shuts down the bot", permission = Permission.BOT_OWNER)
public class Shutdown extends SlashCommandAdapter {
    public Shutdown(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        Surrella.getInstance().getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                Surrella.getInstance().getJda().shutdown();
                System.exit(0);
            }
        }, 5000L);

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Shutdown", "Shutting down...")
        ));
    }
}
