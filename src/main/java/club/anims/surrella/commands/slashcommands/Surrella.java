package club.anims.surrella.commands.slashcommands;

import club.anims.surrella.SurrellaEmbedFactory;
import club.anims.surrella.commands.*;

import java.util.List;

@SlashCommand(name = "surrella", description = "Default command", permission = Permission.DEFAULT)
public class Surrella extends SlashCommandAdapter{
    public Surrella(SlashCommandContext context) {
        super(context);
    }

    @Override
    public SlashCommandReply execute() {
        return new SlashCommandReply(SlashCommandReply.ReplyType.EMBEDS, List.of(
                SurrellaEmbedFactory.createEmbed("Surrella - New Commands", "Due to discord changing their policies, !commands are no longer available. Please use brand new slash commands")
        ));
    }
}
