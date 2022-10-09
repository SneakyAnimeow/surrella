package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import club.anims.surrella.database.JPAService;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "support_delete", description = "Deletes user from bot support", aliases = {"sd"}, permission = Permission.BOT_OWNER)
public class SupportDelete extends SlashCommandAdapter implements SlashCommandOptions {
    public SupportDelete(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var output = "User is not a member of bot support";

        var service = JPAService.getInstance();
        var em = service.getEmf().createEntityManager();

        var user = getContext().getOptions().get(0).getAsUser();

        var exists = em.createNamedQuery("SupportUser.existsByDiscordId", Boolean.class)
                .setParameter("discordId", user.getId())
                .getSingleResult();

        if (exists) {
            var transaction = em.getTransaction();

            transaction.begin();

            em.createNamedQuery("SupportUser.deleteByDiscordId")
                    .setParameter("discordId", user.getId())
                    .executeUpdate();

            transaction.commit();

            output = "User deleted from bot support";
        }

        em.close();

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - SupportDelete", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to remove from bot support", true)
        };
    }
}
