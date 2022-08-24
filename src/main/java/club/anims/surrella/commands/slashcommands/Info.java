package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;
import java.util.Map;

@SlashCommand(name = "info", description = "Shows info about the bot", permission = Permission.DEFAULT, aliases = {"about"})
public class Info extends SlashCommandAdapter {
    public Info(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella", "A multi-purpose discord bot", Map.of(
                        "Version", Surrella.getVERSION(),
                        "Framework", "JDA",
                        "JDK", System.getProperty("java.version")
                ))
        ));
    }
}
