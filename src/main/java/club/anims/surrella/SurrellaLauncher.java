package club.anims.surrella;

/**
 * Main class for the bot.
 */
public class SurrellaLauncher {

    public static void main(String[] args) {
        var token = "";

        try{
            token = args[0];
        }catch (Exception e){
            System.out.println("Please provide a token as the first argument");
            System.exit(1);
        }

        Surrella.getInstance().start(token);
    }
}
