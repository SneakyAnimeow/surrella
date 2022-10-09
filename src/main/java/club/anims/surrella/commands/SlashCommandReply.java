package club.anims.surrella.commands;

import lombok.*;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.io.InputStream;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlashCommandReply {
    public enum ReplyType {
        TEXT,
        MESSAGE,
        EMBEDS,
        FILE,
        BYTE_ARRAY,
        INPUT_STREAM,
        NONE
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ByteArrayReply {
        private byte[] bytes;
        private String name;
        private AttachmentOption[] options;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class InputStreamReply {
        private InputStream inputStream;
        private String name;
        private AttachmentOption[] options;
    }

    private ReplyType type;
    private Object value;
}
