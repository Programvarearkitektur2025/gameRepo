package io.github.progark.Client.Audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
/*
 * MusicManager.java
 * This class is responsible for managing the background music in the application.
 * It handles playing, stopping, and adjusting the volume of the music.
 */
public class MusicManager {

    private Music currentMusic;
    private String currentTrack = "";
    private float volume = 1.0f; 

    public void play(String fileName) {
        if (currentTrack.equals(fileName) && currentMusic != null && currentMusic.isPlaying()) return;

        stop();

        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
        currentMusic.setLooping(true);
        currentMusic.setVolume(volume); 
        currentMusic.play();
        currentTrack = fileName;
    }

    public void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
        currentTrack = "";
    }

    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    public String getCurrentTrack() {
        return currentTrack;
    }

    public void setVolume(float newVolume) {
        volume = Math.max(0f, Math.min(1f, newVolume)); // clamp 0–1
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public float getVolume() {
        return volume;
    }
}
