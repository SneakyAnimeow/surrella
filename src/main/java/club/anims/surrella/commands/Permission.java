package club.anims.surrella.commands;

import lombok.Getter;

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

    Permission(int permissionLevel){
        this.permissionLevel = permissionLevel;
    }

    public static Permission fromInt(int targetPermissionLevel){
        var output = DEFAULT;

        for(var permission : values()){
            if(permission.getPermissionLevel() == targetPermissionLevel){
                output = permission;
                break;
            }
        }

        if(targetPermissionLevel > values()[values().length - 1].getPermissionLevel()){
            output = BOT_OWNER;
        }

        return output;
    }

    /**
     * Checks if the permission level is equal to or higher than the target permission level
     * @param permission The permission level of the user
     * @param targetPermission The target permission level of the command
     * @return True if the permission level is equal to or higher than the target permission level
     */
    public static boolean hasSufficientPermissions(Permission permission, Permission targetPermission){
        return permission.getPermissionLevel() >= targetPermission.getPermissionLevel();
    }

    /**
     * Checks if the permission level is equal to or higher than the target permission level
     * @param targetPermission The target permission level of the command
     * @return True if the permission level is equal to or higher than the target permission level
     */
    public boolean isSufficient(Permission targetPermission){
        return hasSufficientPermissions(this, targetPermission);
    }
}
