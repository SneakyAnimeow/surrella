package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import club.anims.surrella.database.JPAService;
import club.anims.surrella.database.model.BlacklistedUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "blacklist_delete", description = "Deletes user from blacklist", aliases = {"bd"}, permission = Permission.BOT_OWNER)
public class BlacklistDelete extends SlashCommandAdapter implements SlashCommandOptions {
    public BlacklistDelete(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var output = "User is not blacklisted";

        var service = JPAService.getInstance();
        var em = service.getEmf().createEntityManager();

        var user = getContext().getOptions().get(0).getAsUser();

        var exists = em.createNamedQuery("BlacklistedUser.existsByDiscordId", Boolean.class)
                .setParameter("discordId", user.getId())
                .getSingleResult();

        if (exists) {
            var transaction = em.getTransaction();

            transaction.begin();

            em.createNamedQuery("BlacklistedUser.deleteByDiscordId")
                    .setParameter("discordId", user.getId())
                    .executeUpdate();

            transaction.commit();

            output = "User deleted from blacklist";
        }

        em.close();

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - BlacklistDelete", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to remove from blacklist", true)
        };
    }
}
