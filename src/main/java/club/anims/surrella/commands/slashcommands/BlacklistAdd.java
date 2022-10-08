package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;
import club.anims.surrella.database.JPAService;
import club.anims.surrella.database.model.BlacklistedUser;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand(name = "blacklist_add", description = "Blacklists a user from using the bot", aliases = {"ba"}, permission = Permission.BOT_OWNER)
public class BlacklistAdd extends SlashCommandAdapter implements SlashCommandOptions{
    public BlacklistAdd(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        var output = "User is already blacklisted";

        var service = JPAService.getInstance();
        var em = service.getEmf().createEntityManager();

        var user = getContext().getOptions().get(0).getAsUser();

        var alreadyExists = em.createNamedQuery("BlacklistedUser.existsByDiscordId", Boolean.class)
                .setParameter("discordId", user.getId())
                .getSingleResult();

        if (!alreadyExists) {
            var blacklistedUser = new BlacklistedUser(user.getId());
            var transaction = em.getTransaction();

            transaction.begin();

            em.persist(blacklistedUser);

            transaction.commit();

            output = "User blacklisted";
        }

        em.close();

        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - BlacklistAdd", output)
        ));
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to blacklist", true, true)
        };
    }
}
