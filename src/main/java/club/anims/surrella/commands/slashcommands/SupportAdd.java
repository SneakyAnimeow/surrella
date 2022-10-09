package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import club.anims.surrella.database.JPAService;
import club.anims.surrella.database.model.BlacklistedUser;
import club.anims.surrella.database.model.SupportUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "support_add", description = "Adds a user to bot support.", aliases = {"sa"}, permission = Permission.BOT_OWNER)
public class SupportAdd extends SlashCommandAdapter implements SlashCommandOptions {
    public SupportAdd(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var output = "User is already a member of bot support";

        var service = JPAService.getInstance();
        var em = service.getEmf().createEntityManager();

        var user = getContext().getOptions().get(0).getAsUser();

        var alreadyExists = em.createNamedQuery("SupportUser.existsByDiscordId", Boolean.class)
                .setParameter("discordId", user.getId())
                .getSingleResult();

        if (!alreadyExists) {
            var supportUser = new SupportUser(user.getId());
            var transaction = em.getTransaction();

            transaction.begin();

            em.persist(supportUser);

            transaction.commit();

            output = "User added to bot support";
        }

        em.close();

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - SupportAdd", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to add to bot support", true)
        };
    }
}
