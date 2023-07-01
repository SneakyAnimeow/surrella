package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.Surrella;
import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import club.anims.surrella.config.Config;
import club.anims.surrella.interfaces.Loggable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

@SlashCommand(name = "argentina_news", description = "Sends Daily Argentina News", permission = Permission.MOD, aliases = {"an"})
public class ArgentinaNews extends SlashCommandAdapter implements SlashCommandOptions, Loggable {
    //    private static final int ARGENTINA_NEWS_INTERVAL = 1000 * 60 * 60 * 24;
    private static final int ARGENTINA_NEWS_INTERVAL = 1000 * 30;

    private static final HashMap<Guild, TextChannel> subscribedGuilds = new HashMap<>();

    public static void init() {
        Surrella.getInstance().getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (var guild : subscribedGuilds.keySet()) {
                    var channel = subscribedGuilds.get(guild);
                    var fields = new HashMap<String, String>();
                    try {
                        populateFields(fields);
                    } catch (Exception e) {
                        fields.put("An error occurred during fetching", "Please try again later");
                        new ArgentinaNews(null).getLogger().error("An error occurred during fetching", e);
                    }

                    var news = SurrellaEmbedFactory.createEmbed("Surrella - ArgentinaNews", "Daily Argentina News", fields);
                    channel.sendMessageEmbeds(news).queue();
                }
            }
        }, 0L, ARGENTINA_NEWS_INTERVAL);
    }

    public ArgentinaNews(SlashCommandContext context) {
        super(context);
    }

    public static void populateFields(HashMap<String, String> fields) throws IOException {
        var client = new OkHttpClient();
        var gson = new Gson();

        var urlBuilder = Objects.requireNonNull(HttpUrl.parse("http://api.exchangeratesapi.io" + "/v1/latest")).newBuilder();
        urlBuilder.addQueryParameter("access_key", Config.getInstance().getExchangeRateApiKey());

        var url = urlBuilder.build().toString();

        var request = new Request.Builder()
                .url(url)
                .build();
        var call = client.newCall(request);
        var response = call.execute();

        if (!response.isSuccessful() && response.code() != 200)
            throw new IOException("Unexpected code " + response);

        assert response.body() != null;
        var body = response.body().string();

        var fetchedObject = gson.fromJson(body, JsonObject.class);

        var rates = fetchedObject.get("rates").getAsJsonObject();

        var plnToEuro = rates.get("PLN").getAsDouble();
        var arsToEuro = rates.get("ARS").getAsDouble();

        var plnToArs = plnToEuro / arsToEuro;
        var arsToPln = arsToEuro / plnToEuro;

        fields.put("Exchange Rate [PLN => ARS]", String.format("%.5f", plnToArs));
        fields.put("Exchange Rate [ARS => PLN]", String.format("%.5f", arsToPln));
    }

    @Override
    public SlashCommandReply execute() {
        var output = "Daily Argentina News could not be enabled - channel not found";

        if (getContext().getOptions().size() > 1 && getContext().getOptions().get(0).getAsBoolean()) {
            var channel = getContext().getOptions().get(1).getAsChannel();

            try {
                var textChannel = channel.asTextChannel();
                var guild = textChannel.getGuild();
                subscribedGuilds.put(guild, textChannel);
                output = "Daily Argentina News has been enabled on " + textChannel.getAsMention() + " channel";
            } catch (Exception ignored) {
            }
        }

        if (!getContext().getOptions().get(0).getAsBoolean()) {
            output = "Daily Argentina News has been disabled";
            subscribedGuilds.remove(getContext().getChannelUnion().asGuildMessageChannel().getGuild());
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
