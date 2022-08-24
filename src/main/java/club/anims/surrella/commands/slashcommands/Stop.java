package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;

@SlashCommand(name = "stop", description = "Stops playing and skps the queue", permission = Permission.DEFAULT)
public class Stop extends SlashCommandAdapter {
    public Stop(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers().get(guild);

        if (sendHandler == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Stop", "Bot is not in a voice channel or was not initialized on this server yet")
            ));
        }

        sendHandler.getSurrellaAudioPlayer().stop();

        return new SlashCommandReply(SlashCommandReply.ReplyType.TEXT, "Stopped");
    }
}
