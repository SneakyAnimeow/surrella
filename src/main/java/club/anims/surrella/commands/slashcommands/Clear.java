package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import club.anims.surrella.Surrella;

import java.util.List;

@SlashCommand(name = "clear", description = "Clears the queue", permission = Permission.DEFAULT)
public class Clear extends SlashCommandAdapter {
    public Clear(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers().get(guild);

        if (sendHandler == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.TEXT, "Bot is not in a voice channel or was not initialized on this server yet");
        }

        sendHandler.getSurrellaAudioPlayer().clear();

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Clear", "Cleared the queue")
        ));
    }
}