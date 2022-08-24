package club.anims.surrella.audio;

import club.anims.surrella.Surrella;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.*;

import java.util.LinkedList;
import java.util.Queue;

public class SurrellaAudioPlayer {
    @Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class SurrellaAudioPlayerResponse{
        public String message;
        public boolean success;
    }

    private final AudioPlayer audioPlayer;

    private final Queue<AudioTrack> queue;

    public SurrellaAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.audioPlayer.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                audioPlayer.playTrack(queue.poll());
            }
        });
        queue = new LinkedList<>();
    }

    public SurrellaAudioPlayerResponse enqueue(String url, boolean forcePlay) {
        var response = SurrellaAudioPlayerResponse.builder().success(true);

        var result = Surrella.getInstance().getPlayerManager().loadItemOrdered(audioPlayer, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if(forcePlay) {
                    queue.clear();
                    queue.add(track);
                    audioPlayer.stopTrack();
                    response.message("Force playing " + track.getInfo().title);
                } else {
                    queue.add(track);
                    response.message(track.getInfo().title + " has been added to the queue");
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(forcePlay) {
                    queue.clear();
                    queue.addAll(playlist.getTracks());
                    audioPlayer.stopTrack();
                    response.message("Force playing " + playlist.getName());
                } else {
                    queue.addAll(playlist.getTracks());
                    response.message(playlist.getTracks().size() + " tracks have been added to the queue");
                }
            }

            @Override
            public void noMatches() {
                response.success(false).message("No matches found");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                response.success(false).message(exception.getMessage());
            }
        });

        while (!result.isDone()){}

        if(audioPlayer.getPlayingTrack()==null){
            audioPlayer.playTrack(queue.poll());
        }

        return response.build();
    }

    public void pause(){
        audioPlayer.setPaused(true);
    }

    public void resume(){
        audioPlayer.setPaused(false);
    }

    public void skip(){
        audioPlayer.stopTrack();
    }

    public void clear(){
        queue.clear();
    }

    public void stop(){
        audioPlayer.stopTrack();
        queue.clear();
    }
}
