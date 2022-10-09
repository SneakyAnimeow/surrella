package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.audio.AudioPlayerSendHandler;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.AudioChannel;

import club.anims.surrella.Surrella;

import java.util.List;

@SlashCommand(name = "join", description = "Joins the voice channel of the user", permission = Permission.DEFAULT)
public class Join extends SlashCommandAdapter {
    public Join(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        AudioChannel audioChannel;
        try {
            audioChannel = guild.getMember(getContext().getSender()).getVoiceState().getChannel();
        } catch (Exception e) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Join", "You are not in a voice channel")
            ));
        }

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers()
                .computeIfAbsent(guild, v -> new AudioPlayerSendHandler(Surrella.getInstance().getPlayerManager().createPlayer()));

        guild.getAudioManager().setSendingHandler(sendHandler);

        guild.getAudioManager().openAudioConnection(audioChannel);

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Join", "Joined the voice channel")
        ));
    }
}