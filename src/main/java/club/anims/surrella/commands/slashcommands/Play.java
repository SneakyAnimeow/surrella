package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "play", description = "Plays a song", permission = Permission.DEFAULT)
public class Play extends SlashCommandAdapter implements SlashCommandOptions {
    public Play(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var guild = getContext().getChannelUnion().asGuildMessageChannel().getGuild();

        var sendHandler = Surrella.getInstance().getGuildAudioSendHandlers().get(guild);

        if (sendHandler == null) {
            return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                    SurrellaEmbedFactory.createEmbed("Surrella - Play", "Bot is not in a voice channel or was not initialized on this server yet")
            ));
        }

        var response = sendHandler.getSurrellaAudioPlayer().enqueue(getContext().getOptions().get(0).getAsString(),
                (getContext().getOptions().size() > 1 && getContext().getOptions().get(1).getAsBoolean()));

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(SurrellaEmbedFactory.createEmbed("Surrella - Play", response.message)));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.STRING, "url", "Song url", true, true),
                new OptionData(OptionType.BOOLEAN, "force_play", "Skip the queue", false, false)
        };
    }
}
