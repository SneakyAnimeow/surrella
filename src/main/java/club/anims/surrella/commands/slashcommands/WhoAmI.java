package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;

@SlashCommand(name = "whoami", description = "Shows information about the user", aliases = {"userinfo"}, permission = Permission.DEFAULT)
public class WhoAmI extends SlashCommandAdapter {
    public WhoAmI(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var permission = Permission.DEFAULT;
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();
        var member = guild.retrieveMember(getContext().getSender()).complete();

        permission = Permission.fromMember(member);

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("User info", "You are classified as " + permission)
        ));
    }
}
