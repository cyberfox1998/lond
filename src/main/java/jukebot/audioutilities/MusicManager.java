package jukebot.audioutilities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import jukebot.JukeBot;

public class MusicManager {

    public AudioPlayer player;
    public AudioHandler handler;

    public MusicManager() {
        this.player = JukeBot.playerManager.createPlayer();
        this.handler = new AudioHandler(this.player);
        this.player.addListener(this.handler);
    }

    public boolean isPlaying() {
        return player.getPlayingTrack() != null;
    }

}