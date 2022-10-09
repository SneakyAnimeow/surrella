package club.anims.surrella;

import club.anims.surrella.database.JPAService;

/**
 * Main class for the bot.
 */
public class SurrellaLauncher {

    public static void main(String[] args) {
        JPAService.getInstance();
        Surrella.getInstance().start();
    }
}
