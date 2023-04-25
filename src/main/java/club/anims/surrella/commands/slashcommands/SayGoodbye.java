package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.audio.AudioPlayerSendHandler;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.TimerTask;

@SlashCommand(name = "say_goodbye", description = "Says goodbye", permission = Permission.ADMIN)
public class SayGoodbye extends SlashCommandAdapter implements SlashCommandOptions {
    public SayGoodbye(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var member = getContext().getOptions().get(0).getAsMember();
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        AudioChannel audioChannel;
        try{
            audioChannel = member.getVoiceState().getChannel();

            if (audioChannel == null) {
                throw new Exception();
            }
        }catch (Exception e){
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Say Goodbye", "Member is not in a voice channel")
            ));
        }

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers()
                .computeIfAbsent(guild, v -> new AudioPlayerSendHandler(Surrella.getInstance().getPlayerManager().createPlayer()));

        guild.getAudioManager().setSendingHandler(sendHandler);

        guild.getAudioManager().openAudioConnection(audioChannel);

        sendHandler.getSurrellaAudioPlayer().enqueue("https://youtu.be/RCMcnf4Qf1o", true);

        Surrella.getInstance().getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                var voiceChannel = guild.getVoiceChannels().get((int) (Math.random() * guild.getVoiceChannels().size()));

                guild.moveVoiceMember(member, voiceChannel).queue();

                if(!member.getUser().isBot()){
                    member.getUser().openPrivateChannel().queue(channel -> {
                        channel.sendMessage("Goodbye").queue();
                    });
                }

                guild.getAudioManager().closeAudioConnection();
            }
        }, 7500);

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Say Goodbye", "You know the rules and so do I... " + member.getAsMention()))
        );
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "member", "Member", true, false)
        };
    }
}
