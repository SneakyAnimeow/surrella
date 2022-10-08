package club.anims.surrella.commands;

import club.anims.surrella.config.Config;
import club.anims.surrella.database.JPAService;
import com.sun.istack.Nullable;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

public enum Permission {
    /**
     * Everyone can use this command
     */
    DEFAULT(0),

    /**
     * Requires user to have permissions to move and kick members from voice channels
     */
    MOD(1),

    /**
     * Requires user to have Administrator permission
     */
    ADMIN(2),

    /**
     * Requires user to be the owner of the server
     */
    OWNER(3),

    /**
     * Command available only for users selected by the bot owner
     */
    BOT_SUPPORT(4),

    /**
     * Command available only for the bot owner
     */
    BOT_OWNER(5);

    @Getter
    private final int permissionLevel;

    Permission(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    /**
     * Creates a Permission object from an integer
     *
     * @param targetPermissionLevel The integer to convert
     * @return The Permission object
     */
    public static Permission fromInt(int targetPermissionLevel) {
        var output = DEFAULT;

        for (var permission : values()) {
            if (permission.getPermissionLevel() == targetPermissionLevel) {
                output = permission;
                break;
            }
        }

        if (targetPermissionLevel > values()[values().length - 1].getPermissionLevel()) {
            output = BOT_OWNER;
        }

        return output;
    }

    /**
     * Checks if the permission level is equal to or higher than the target permission level
     *
     * @param permission       The permission level of the user
     * @param targetPermission The target permission level of the command
     * @return True if the permission level is equal to or higher than the target permission level
     */
    public static boolean hasSufficientPermissions(Permission permission, Permission targetPermission) {
        return permission.getPermissionLevel() >= targetPermission.getPermissionLevel();
    }

    /**
     * Checks guild member's permissions
     *
     * @param member The guild member
     * @return The permission level of the guild member
     */
    public static Permission fromMember(@Nullable Member member) {
        var permission = Permission.DEFAULT;

        if (member == null)
            return permission;

        if (member.hasPermission(net.dv8tion.jda.api.Permission.KICK_MEMBERS, net.dv8tion.jda.api.Permission.VOICE_MOVE_OTHERS))
            permission = Permission.MOD;

        if (member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR))
            permission = Permission.ADMIN;

        if (member.isOwner())
            permission = Permission.OWNER;

        var service = JPAService.getInstance();

        var em = service.getEmf().createEntityManager();

        if (em.createNamedQuery("SupportUser.existsByDiscordId", Boolean.class)
                .setParameter("discordId", member.getId())
                .getSingleResult()) {
            permission = Permission.BOT_SUPPORT;
        }

        em.close();

        if (Config.getInstance().getOwnerId().equals(member.getId()))
            permission = Permission.BOT_OWNER;

        return permission;
    }

    /**
     * Checks if the permission level is equal to or higher than the target permission level
     *
     * @param targetPermission The target permission level of the command
     * @return True if the permission level is equal to or higher than the target permission level
     */
    public boolean isSufficient(Permission targetPermission) {
        return hasSufficientPermissions(this, targetPermission);
    }

    /**
     * Returns the permission level as a string
     *
     * @return The permission level as a string
     */
    @Override
    public String toString() {
        return switch (this) {
            case DEFAULT -> "User";
            case MOD -> "Server Moderator";
            case ADMIN -> "Server Administrator";
            case OWNER -> "Server Owner";
            case BOT_SUPPORT -> "Bot Support";
            case BOT_OWNER -> "Bot Owner";
        };
    }
}
