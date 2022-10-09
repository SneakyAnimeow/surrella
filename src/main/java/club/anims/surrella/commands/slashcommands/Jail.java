package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.List;

@SlashCommand(name = "jail", description = "Jails a user", aliases = {"j", "jailuser"}, permission = Permission.ADMIN)
public class Jail extends SlashCommandAdapter implements SlashCommandOptions {
    public Jail(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var user = getContext().getOptions().get(0).getAsUser();
        var channel = getContext().getOptions().get(1).getAsChannel();
        var guild = channel.getGuild();

        if (!channel.getType().isAudio()) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Jail", "Channel is not a voice channel")
            ));
        }

        var audioChannel = channel.asAudioChannel();

        var member = guild.retrieveMember(user).complete();

        Surrella.getInstance().getJailedMembers().computeIfAbsent(guild, v -> new HashMap<>()).put(member, audioChannel);

        var connectedMember = guild.getMember(user);

        if (connectedMember != null) {
            guild.moveVoiceMember(connectedMember, audioChannel).queue();
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - Jail", "Jailed " + user.getAsMention() + " in " + channel.getAsMention())
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "The user to jail", true),
                new OptionData(OptionType.CHANNEL, "channel", "Jail channel", true)
        };
    }
}
