package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

@SlashCommand(name = "argentina_news", description = "Sends Daily Argentina News", permission = Permission.MOD, aliases = {"an"})
public class ArgentinaNews extends SlashCommandAdapter implements SlashCommandOptions {
    //    private static final int ARGENTINA_NEWS_INTERVAL = 1000 * 60 * 60 * 24;
    private static final int ARGENTINA_NEWS_INTERVAL = 1000 * 10;

    private static final HashMap<Guild, TextChannel> subscribedGuilds = new HashMap<>();

    public static void init() {
        Surrella.getInstance().getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (var guild : subscribedGuilds.keySet()) {
                    var channel = subscribedGuilds.get(guild);
                    var news = SurrellaEmbedFactory.createEmbed("Surrella - ArgentinaNews", "Daily Argentina News");
                    channel.sendMessageEmbeds(news).queue();
                }
            }
        }, 0L, ARGENTINA_NEWS_INTERVAL);
    }

    public ArgentinaNews(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var output = "Daily Argentina News could not be enabled - channel not found";

        if(getContext().getOptions().size() > 1 && getContext().getOptions().get(0).getAsBoolean()){
            var channel = getContext().getOptions().get(1).getAsChannel();

            try {
                var textChannel = channel.asTextChannel();
                var guild = textChannel.getGuild();
                subscribedGuilds.put(guild, textChannel);
                output = "Daily Argentina News has been enabled on " + textChannel.getAsMention() + " channel";
            } catch (Exception ignored) {}
        }

        if(!getContext().getOptions().get(0).getAsBoolean()){
            output = "Daily Argentina News has been disabled";
            subscribedGuilds.remove(getContext().);
        }

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - ArgentinaNews", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.BOOLEAN, "enable", "Enable Daily Argentina News on server", true),
                new OptionData(OptionType.CHANNEL, "channel", "Channel to send Daily Argentina News", false)
        };
    }
}
