package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import club.anims.surrella.Surrella;

import java.util.HashMap;
import java.util.List;

@SlashCommand(name = "follow", description = "Follows the user", permission = Permission.DEFAULT)
public class Follow extends SlashCommandAdapter implements SlashCommandOptions {
    public Follow(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();
        var member = guild.retrieveMember(getContext().getSender()).complete();
        var memberToFollow = getContext().getOptions().get(0).getAsMember();

        if (memberToFollow == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Follow", "Member not found")
            ));
        }

        if (member.getIdLong() == memberToFollow.getIdLong()) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Follow", "You can't follow yourself")
            ));
        }

        var followingMembers = Surrella.getInstance().getFollowingMembers().computeIfAbsent(guild, v -> new HashMap<>());

        followingMembers.put(member, memberToFollow);

        if (!memberToFollow.getUser().isBot()) {
            memberToFollow.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(member.getAsMention() + " is now following you in server " + guild.getName()).queue();
            });
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Follow", "Now following " + memberToFollow.getAsMention())
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to follow", true, false)
        };
    }
}
