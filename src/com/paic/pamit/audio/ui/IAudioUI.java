package com.paic.pamit.audio.ui;

import android.app.Activity;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

public interface IAudioUI extends OnErrorListener,OnCompletionListener,OnBufferingUpdateListener{
	public static final int OPT_TYPE_PLAY = 1;
	public static final int OPT_TYPE_RECORD = 2;
	public void setMaxAmplitude(int maxAmplitude);
	public void setProgress(long time);
	public void setMax(long progress);
	public void createView(Activity activity,int optType);
	public void setAudioUIListener(IAudioUIListener listener);
	public void awakeUP();
	public void sleep();
	public void forceClose();
	public interface IAudioUIListener{
		public void startPlayFromUI();
		public void stopPlayFromUI();
		public void pausePlayFromUI();
		public void startRecordFromUI();
		public void stopRecordFromUI();
		public void pauseRecordFromUI();
		public void exitFromUI();
		public void setProgressFromUI(long progress);
		public boolean isRunning();
		public void save();
	}
}
