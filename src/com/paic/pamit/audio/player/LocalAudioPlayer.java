package com.paic.pamit.audio.player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.paic.pamit.audio.AmplitudeUtil;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.text.TextUtils;
import android.util.Log;

public class LocalAudioPlayer implements IAudioPlayer,OnPreparedListener{
	public static String TAG = LocalAudioPlayer.class.getSimpleName();
	
	public static final int RESULT_ERROR = -1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_PAUSE = 2;
	int runningState = STATE_IDLE;

	MediaPlayer _mPlayer;
	AmplitudeUtil amplitudeUtil;
	OnCompletionListener _mExternalOnCompletionListener;
	OnBufferingUpdateListener _mExternalBufferingUpdateListener;
	OnErrorListener _mExternalOnErrorListener;
	
	public LocalAudioPlayer(){
		_mPlayer = new MediaPlayer();
		amplitudeUtil = new AmplitudeUtil();
	}
	
	@SuppressLint("NewApi")
	OnTimedTextListener _mOnTime = new OnTimedTextListener() {
		
		@Override
		public void onTimedText(MediaPlayer mediaplayer, TimedText timedtext) {
			Log.e(TAG, timedtext.getText());
		}
	};
	
	OnErrorListener _mOnErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mediaplayer, int i, int j) {
			Log.e(TAG, "audio play error ");
			if(_mExternalOnErrorListener != null){
				_mExternalOnErrorListener.onError(mediaplayer,i, j);
			}
			return stop();
		}
	};
	
	OnCompletionListener _mOnCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			if(_mExternalOnCompletionListener!=null){
				_mExternalOnCompletionListener.onCompletion(mediaplayer);
			}
			stop();
		}
	};
	
	
	@Override
	public boolean pause() {
		amplitudeUtil.stop();
		try {
			_mPlayer.pause();
			runningState = STATE_PAUSE;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean stop() {
		amplitudeUtil.stop();
		try {
			if (_mPlayer != null) {
				_mPlayer.stop();
				_mPlayer.reset();
				_mPlayer.release();
				_mPlayer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		runningState = STATE_IDLE;
		return true;
	}

	@Override
	public boolean playContinue() {
		if (runningState==STATE_PAUSE) {
			_mPlayer.start();
			try {
				amplitudeUtil.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			runningState = STATE_RUNNING;
		}
		return false;
	}

	@Override
	public long getMaxAmplitude() {
		if (runningState==STATE_RUNNING&&amplitudeUtil!=null) {
			return amplitudeUtil.getVolume();
		}
		return 0;
	}

	@Override
	public boolean isPausing() {
		return runningState==STATE_PAUSE;
	}

	@Override
	public void getCurrentPosition(Float[] position) {
		if (position==null||position.length<2) {
			position = new Float[]{0f,0f};
		}
		if(runningState != STATE_IDLE && _mPlayer != null){
			position[INDEX_DURATION] = Float.valueOf((float)_mPlayer.getDuration());
			position[INDEX_CURRENT_POSITION] = Float.valueOf((float)_mPlayer.getCurrentPosition());
		}
	}

	@Override
	public boolean setProgress(int percentage) {
		if(runningState != STATE_IDLE && _mPlayer!=null){
			_mPlayer.seekTo(percentage);
			return true;
		}
		return false;
	}

	@Override
	public boolean setOnBufferingUpdateListener(
			OnBufferingUpdateListener listener) {
		if (listener!=null) {
			this._mExternalBufferingUpdateListener = listener;
			return true;
		}
		return false;
	}

	@Override
	public boolean setOnErrorListener(OnErrorListener listener) {
		if (listener!=null) {
			this._mExternalOnErrorListener = listener;
			return true;
		}
		return false;
	}

	@Override
	public boolean setOnCompletionListener(OnCompletionListener listener) {
		if (listener!=null) {
			this._mExternalOnCompletionListener = listener;
			return true;
		}
		return false;
	}

	@Override
	public int start(String url) {
		if (url!=null&&!TextUtils.isEmpty(url)) {
			return start(null, url);
		}
		return RESULT_ERROR;
	}
	
	@Override
	public int start(File audioFile) {
		if (audioFile!=null&&!audioFile.exists()) {
			return start(audioFile, null);
		}
		return RESULT_ERROR;
	}

	@SuppressLint("NewApi")
	private int start(File argFile,String url){
		if (isOnCompletionListenerNull()||isOnBufferingUpdateListenerNull()||isOnErrorListenerNull()) {
			return RESULT_ERROR;
		}
		if(runningState==STATE_RUNNING && _mPlayer!=null ){
			stop();
		}
		if (runningState==STATE_PAUSE) {
			playContinue();
		}
		if(runningState==STATE_IDLE){
		if(argFile!=null&&argFile.exists()||!TextUtils.isEmpty(url)){
			_mPlayer = new MediaPlayer();
			_mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			_mPlayer.setOnCompletionListener(_mOnCompletionListener);
			_mPlayer.setOnPreparedListener(this);
			_mPlayer.setOnBufferingUpdateListener(_mExternalBufferingUpdateListener);
			_mPlayer.setOnErrorListener(_mOnErrorListener);
			_mPlayer.setOnTimedTextListener(_mOnTime);
			try {
				if(!TextUtils.isEmpty(url)){
					_mPlayer.setDataSource(url);
				}else{
					_mPlayer.setDataSource(argFile.getAbsolutePath());
				}
				_mPlayer.prepare();
				runningState = STATE_RUNNING;
				return 0;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
		return RESULT_ERROR;
	}
	
	private boolean isOnCompletionListenerNull(){
		if (_mExternalOnCompletionListener==null) {
			Log.e(TAG, "OnCompletionListener is null,you have to set it!");
			return true;
		}
		return false;
	}
	
	private boolean isOnBufferingUpdateListenerNull(){
		if (_mExternalBufferingUpdateListener==null) {
			Log.e(TAG, "OnBufferingUpdateListener is null,you have to set it!");
			return true;
		}
		return false;
	}
	
	private boolean isOnErrorListenerNull(){
		if (_mExternalOnErrorListener==null) {
			Log.e(TAG, "OnErrorListener is null,you have to set it!");
			return true;
		}
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mediaplayer) {
		if(_mPlayer!=null){
			_mPlayer.start();
			try {
				amplitudeUtil.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isRunning() {
		return runningState==STATE_RUNNING;
	}
}
