package club.anims.surrella.config;

import club.anims.surrella.interfaces.Jsonable;
import lombok.*;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Config implements Jsonable<Config> {
    private static final Path PATH = Path.of("config.json");

    private static Config instance;

    /**
     * The bot token
     */
    private String token;

    /**
     * Discord ID of the bot owner
     */
    private String ownerId;

    @SneakyThrows
    public static Config getInstance(){
        return instance == null ? instance = new Config().fromJson(Files.readString(PATH)) : instance;
    }
}
