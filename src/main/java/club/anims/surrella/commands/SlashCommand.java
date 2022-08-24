package club.anims.surrella.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommand {
    /**
     * The name of the command.
     */
    String name();

    /**
     * The description of the command.
     */
    String description();

    /**
     * The permission required to use the command.
     */
    Permission permission();

    /**
     * The aliases of the command.
     */
    String[] aliases() default {};

    /**
     * Whether the command is available in DMs.
     */
    boolean serverOnly() default true;
}
