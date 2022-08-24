package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;

@SlashCommand(name = "skip", description = "Skips the current song", permission = Permission.DEFAULT, aliases = {"next"})
public class Skip extends SlashCommandAdapter {
    public Skip(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers().get(guild);

        if (sendHandler == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Skip", "Bot is not in a voice channel or was not initialized on this server yet")
            ));
        }

        sendHandler.getSurrellaAudioPlayer().skip();

        return new SlashCommandReply(SlashCommandReply.ReplyType.TEXT, "Skipped");
    }
}
