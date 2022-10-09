package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.List;

@SlashCommand(name = "unjail", description = "Unjails a user", aliases = {"unj", "unjailuser"}, permission = Permission.ADMIN)
public class Unjail extends SlashCommandAdapter implements SlashCommandOptions {
    public Unjail(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var user = getContext().getOptions().get(0).getAsUser();
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        var jailedMembers = Surrella.getInstance().getJailedMembers().computeIfAbsent(guild, v -> new HashMap<>());

        var member = guild.retrieveMember(user).complete();

        var channel = jailedMembers.remove(member);

        if (channel == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Unjail", "User is not jailed")
            ));
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Unjail", "Unjailed " + user.getAsMention() + " from " + channel.getAsMention())
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "The user to unjail", true)
        };
    }
}
