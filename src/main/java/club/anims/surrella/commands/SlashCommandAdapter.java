package club.anims.surrella.commands;

import lombok.Getter;

public abstract class SlashCommandAdapter {
    @Getter
    private SlashCommandContext context;

    public SlashCommandAdapter(SlashCommandContext context){
        this.context = context;
    }

    /**
     * Executes the command
     * @return The reply to send to the user
     */
    public SlashCommandReply execute(){
        return new SlashCommandReply(SlashCommandReply.ReplyType.NONE, null);
    }
}
