package club.anims.surrella.listeners;

import club.anims.surrella.Surrella;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MovementListenerAdapter extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        var guild = event.getGuild();
        var member = event.getMember();

        var jailedMembers = Surrella.getInstance().getJailedMembers().computeIfAbsent(guild, v -> new HashMap<>());
        var followingMembers = Surrella.getInstance().getFollowingMembers().computeIfAbsent(guild, v -> new HashMap<>());

        if (jailedMembers.containsKey(member)) {
            var channel = jailedMembers.get(member);
            guild.moveVoiceMember(member, channel).queue();
            return;
        }

        for(var entry : followingMembers.entrySet()) {
            var followingMember = entry.getKey();
            var followedMember = entry.getValue();

            if (member.equals(followedMember)) {
                guild.moveVoiceMember(followingMember, member.getVoiceState().getChannel()).queue();
            }
        }
    }
}
