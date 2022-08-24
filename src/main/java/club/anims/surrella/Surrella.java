package club.anims.surrella;

import club.anims.surrella.audio.AudioPlayerSendHandler;
import club.anims.surrella.commands.*;
import club.anims.surrella.interfaces.Loggable;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;

import static org.reflections.scanners.Scanners.TypesAnnotated;

public class Surrella implements Loggable {
    @Getter
    private static final String VERSION = "beta 1.2";

    @Getter
    private static Surrella instance;

    @Getter
    private JDA jda;

    @Getter
    private AudioPlayerManager playerManager;

    @Getter
    private Timer timer;

    @Getter
    private HashMap<Guild, AudioPlayerSendHandler> guildAudioSendHandlers;

    /**
     * HashMap of Followers and Followees
     */
    @Getter
    private HashMap<Guild, HashMap<Member, Member>> followingMembers;

    /**
     * Initializes the Surrella instance
     */
    public static void init() {
        instance = new Surrella();
    }

    /**
     * Starts the Surrella instance
     * @param token The bot token
     */
    public void start(String token){
        getLogger().info("Connecting to discord with provided token...");

        try{
            jda = JDABuilder.createDefault(token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .setActivity(Activity.listening("/surrella"))
                    .build();

        }catch (Exception e){
            getLogger().error("Failed to connect to discord", e);
            System.exit(1);
        }

        loadCommands();
        jda.addEventListener(new SlashCommandListenerAdapter());

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        guildAudioSendHandlers = new HashMap<>();
        timer = new Timer();
        followingMembers = new HashMap<>();
    }

    /**
     * Loads all slash commands from the club.anims.commands.slashcommands package
     */
    private void loadCommands() {
        var reflections = new Reflections("club.anims.surrella.commands.slashcommands");

        var slashCommands = reflections.get(TypesAnnotated.with(SlashCommand.class).asClass());

        slashCommands.forEach(slashCommand -> {
            var commands = new ArrayList<CommandCreateAction>();
            var annotatedClass = slashCommand.getAnnotation(SlashCommand.class);

            commands.add(jda.upsertCommand(annotatedClass.name(), annotatedClass.description()));

            Arrays.stream(annotatedClass.aliases()).forEach(alias -> {
                commands.add(jda.upsertCommand(alias, annotatedClass.description()));
            });

            OptionData[] options = null;
            if(Arrays.asList(slashCommand.getInterfaces()).contains(SlashCommandOptions.class)) {
                try {
                    options = ((SlashCommandOptions) slashCommand.getDeclaredConstructor(SlashCommandContext.class).newInstance(new SlashCommandContext())).getOptions();
                } catch (Exception e) {
                    getLogger().error("Failed to load options for command " + annotatedClass.name(), e);
                    System.exit(1);
                }
            }

            if(options != null) {
                for(var i=0; i<commands.size(); i++) {
                    commands.set(i, commands.get(i).addOptions(options));
                }
            }

            for(var i=0; i<commands.size(); i++){
                commands.set(i, commands.get(i).setGuildOnly(annotatedClass.serverOnly()));
                getLogger().debug("Loaded command " + commands.get(i).getName());
            }

            commands.forEach(RestAction::queue);
        });
    }
}
