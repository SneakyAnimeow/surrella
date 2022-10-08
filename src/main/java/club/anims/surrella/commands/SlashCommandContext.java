package club.anims.surrella.commands;

import lombok.*;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SlashCommandContext {
    private String id;

    private MessageChannelUnion channelUnion;

    private ChannelType channelType;

    private User sender;

    private List<OptionMapping> options;
}
