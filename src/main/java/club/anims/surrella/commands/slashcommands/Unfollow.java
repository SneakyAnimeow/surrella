package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;

@SlashCommand(name = "unfollow", description = "Unfollows the user", permission = Permission.DEFAULT)
public class Unfollow extends SlashCommandAdapter{
    public Unfollow(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();
        var member = guild.retrieveMember(getContext().getSender()).complete();

        var followingMembers  = Surrella.getInstance().getFollowingMembers().get(guild);

        if (followingMembers == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Unfollow", "No one was ever followed in this server")
            ));
        }

        followingMembers.remove(member);

        if(!member.getUser().isBot()){
            member.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(member.getAsMention() + " is no longer following you in server "+ guild.getName()).queue();
            });
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Unfollow", "No longer following " + member.getAsMention())
        ));
    }
}
