package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "find", description = "Finds in which voice channel the provided user currently is + can move the command sender there",
        permission = Permission.DEFAULT, aliases = {"look_for"})
public class Find extends SlashCommandAdapter implements SlashCommandOptions {
    public Find(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();
        var member = guild.getMember(getContext().getSender());

        try {
            var audioChannel = member.getVoiceState().getChannel();

            if (audioChannel == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Find", "You are not in a voice channel")
            ));
        }

        var target = getContext().getOptions().get(0).getAsMember();

        AudioChannel targetAudioChannel;
        try {
            targetAudioChannel = target.getVoiceState().getChannel();

            if (targetAudioChannel == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Find", "The provided user is not in a voice channel")
            ));
        }

        boolean dontMove = false;

        if (getContext().getOptions().size() > 1)
            dontMove = getContext().getOptions().get(1).getAsBoolean();

        var output = "User is in voice channel " + targetAudioChannel.getAsMention();

        if (!dontMove) {
            output = "Moved you to the voice channel of " + target.getAsMention();
            guild.moveVoiceMember(member, targetAudioChannel).queue();
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Find", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to find", true, false),
                new OptionData(OptionType.BOOLEAN, "dont_move", "Don't move the command sender there", false, false)
        };
    }
}
