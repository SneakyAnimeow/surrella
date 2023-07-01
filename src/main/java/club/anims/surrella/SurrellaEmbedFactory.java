package club.anims.surrella;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Map;

public abstract class SurrellaEmbedFactory {
    public static MessageEmbed createEmbed(Map<String, String> fields) {
        var embed = new EmbedBuilder();
        embed.setColor(0x1214bd);
        fields.forEach((key, value) -> embed.addField(key, value, false));
        return embed.build();
    }

    public static MessageEmbed createEmbed(String title, String description) {
        var embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setColor(0x1214bd);
        return embed.build();
    }

    public static MessageEmbed createEmbed(String title, String description, Map<String, String> fields) {
        var embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setColor(0x1214bd);
        fields.forEach((key, value) -> embed.addField(key, value, false));
        return embed.build();
    }
}
