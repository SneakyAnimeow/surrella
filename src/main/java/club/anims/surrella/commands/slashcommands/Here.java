package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "here", description = "Moves everyone or selected users (up to 10) to the voice channel", permission = Permission.ADMIN, aliases = {"mhere", "move_here"})
public class Here extends SlashCommandAdapter implements SlashCommandOptions {
    public Here(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();
        var member = guild.getMember(getContext().getSender());
        AudioChannel audioChannel;

        try {
            audioChannel = member.getVoiceState().getChannel();

            if (audioChannel == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Here", "You are not in a voice channel")
            ));
        }

        if (getContext().getOptions().size() < 1) {
            guild.getMembers().forEach(m -> {
                try {
                    guild.moveVoiceMember(m, audioChannel).queue();
                } catch (Exception ignored) {
                }
            });
        } else {
            getContext().getOptions().forEach(option -> {
                try {
                    guild.moveVoiceMember(option.getAsMember(), audioChannel).queue();
                } catch (Exception ignored) {
                }
            });
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Here", "Moved " +
                        (getContext().getOptions().size() < 1 ? "Everyone" : "Selected users") + " to the voice channel")
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "member", "Member to move", false),
                new OptionData(OptionType.USER, "member2", "Member to move", false),
                new OptionData(OptionType.USER, "member3", "Member to move", false),
                new OptionData(OptionType.USER, "member4", "Member to move", false),
                new OptionData(OptionType.USER, "member5", "Member to move", false),
                new OptionData(OptionType.USER, "member6", "Member to move", false),
                new OptionData(OptionType.USER, "member7", "Member to move", false),
                new OptionData(OptionType.USER, "member8", "Member to move", false),
                new OptionData(OptionType.USER, "member9", "Member to move", false),
                new OptionData(OptionType.USER, "member10", "Member to move", false),
        };
    }
}
