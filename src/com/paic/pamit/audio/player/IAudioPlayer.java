package com.paic.pamit.audio.player;

import java.io.File;

import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import com.paic.pamit.audio.Audio;

public interface IAudioPlayer extends Audio{
	public static int INDEX_DURATION = 0;
	public static int INDEX_CURRENT_POSITION = 1;
	public boolean pause();
	public int start(File audioFile);
	public int start(String url);
	public boolean stop();
	public boolean playContinue();
	public long getMaxAmplitude(); 
	public boolean isPausing();
	public void getCurrentPosition(Float[] position); 
	public boolean setProgress(int percentage);
	public boolean setOnBufferingUpdateListener(OnBufferingUpdateListener listener);
	public boolean setOnErrorListener(OnErrorListener listener);
	public boolean setOnCompletionListener(OnCompletionListener listener);
	public boolean isRunning();
	public interface IAudioPlayerListener {
		public void onError(int i, int j);
		public void onComplete();
	}
}
