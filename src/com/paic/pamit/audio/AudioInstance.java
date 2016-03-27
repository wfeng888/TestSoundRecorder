package com.paic.pamit.audio;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

import com.paic.pamit.audio.TimerService.TimerListener;
import com.paic.pamit.audio.player.IAudioPlayer;
import com.paic.pamit.audio.player.LocalAudioPlayer;
import com.paic.pamit.audio.ui.AudioUI;
import com.paic.pamit.audio.ui.IAudioUI;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.provider.Contacts.Intents.UI;
import android.renderscript.Type;

public class AudioInstance implements IAudioUI.IAudioUIListener,OnErrorListener,OnCompletionListener,OnBufferingUpdateListener,TimerListener{
	public static final String TAG = AudioInstance.class.getSimpleName();
	private String audioFilePath; 
	IAudioRecorder mRecorder;
	 IAudioUI  mUI;
	 IAudioPlayer mPlayer;
	 private boolean isShowing = false;
	 private SamplingMaxAmplitude mSamplingMaxAmplitude;
	 private void initResource(Context ctx,String filePath) throws InvalidParamException{
		 try {
			 audioFilePath = filePath;
			 mRecorder = new AudioRecorderProxy(ctx);
			 mUI = new AudioUI(ctx);
			 mUI.setAudioUIListener(this);
			 mSamplingMaxAmplitude = new SamplingMaxAmplitude();
			 mPlayer = (IAudioPlayer) new LocalAudioPlayer();
			 mPlayer = (IAudioPlayer) new LocalAudioPlayer();
			 mPlayer.setOnBufferingUpdateListener(this);
			 mPlayer.setOnCompletionListener(this);
			 mPlayer.setOnErrorListener(this);
		} catch(InvalidParamException e){
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 private void releaseResource(){
		 try {
			 audioFilePath = null;
			 if (mRecorder != null
					 && (mRecorder.isPuased() || mRecorder.isRunning())) {
				 mRecorder.endRecord();
			 }
			 if (mPlayer != null && (mPlayer.isPausing() || mPlayer.isRunning())) {
				 mPlayer.stop();
			 }
			 if (mSamplingMaxAmplitude!=null) {
				 mSamplingMaxAmplitude.stopSampling();
			 }
			 
			 mUI = null;
			 mRecorder = null;
			 mPlayer = null;
			 mSamplingMaxAmplitude = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	public  void startRecord(Context ctx,String filePath) throws InvalidParamException{
		try {
			initResource(ctx,filePath);
			mUI.createView((Activity)ctx,IAudioUI.OPT_TYPE_RECORD);
			mSamplingMaxAmplitude.setOptType(SamplingMaxAmplitude.OPT_TYPE_RECORD);
			isShowing = true;
		}catch(InvalidParamException e){
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startPlay(Context ctx,String filePath) throws InvalidParamException{
		try {
			initResource(ctx,filePath);
			mUI.createView((Activity)ctx,IAudioUI.OPT_TYPE_PLAY);
			mSamplingMaxAmplitude.setOptType(SamplingMaxAmplitude.OPT_TYPE_PLAY);
			isShowing = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startPlayFromUI() {
		try {
			if (mPlayer==null) {
				mPlayer = (IAudioPlayer) new LocalAudioPlayer();
				mPlayer.setOnBufferingUpdateListener(this);
				mPlayer.setOnCompletionListener(this);
				mPlayer.setOnErrorListener(this);
			}
			mPlayer.start(audioFilePath);
			mSamplingMaxAmplitude.setOptType(SamplingMaxAmplitude.OPT_TYPE_PLAY);
			mSamplingMaxAmplitude.startSampling();
			startTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopPlayFromUI() {
		try {
			mSamplingMaxAmplitude.stopSampling();
			mPlayer.stop();
			stopTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pausePlayFromUI() {
		try {
			mSamplingMaxAmplitude.stopSampling();
			mPlayer.pause();
			pauseTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startRecordFromUI() {
		try {
			mRecorder.setOutputFile(audioFilePath);
			mRecorder.startRecord();
			mSamplingMaxAmplitude.setOptType(SamplingMaxAmplitude.OPT_TYPE_RECORD);
			mSamplingMaxAmplitude.startSampling();
			startTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopRecordFromUI() {
		try {
			mSamplingMaxAmplitude.stopSampling();
			mRecorder.endRecord();
			stopTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pauseRecordFromUI() {
		try {
			mSamplingMaxAmplitude.stopSampling();
			mRecorder.pause();
			pauseTimerService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exitFromUI() {
		try {
			releaseResource();
			stopTimerService();
			isShowing = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setProgressFromUI(long progress) {
		try {
			mPlayer.setProgress((int)progress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startTimerService(){
		try {
			TimerService.getDefault().start(this);
			} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void pauseTimerService(){
		
		try {
			TimerService.getDefault().pause(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void stopTimerService(){
		try {
			TimerService.getDefault().stop(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void delayStopTimerService(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2*1000);
					stopTimerService();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	class SamplingMaxAmplitude implements Runnable{

		public static final int OPT_TYPE_PLAY = 1;
		public static final int OPT_TYPE_RECORD = 2;
		private int optType;
		private boolean needSampling = false;
		
		public void setOptType(int optType){
			this.optType = optType;
		}
		
		public void startSampling(){
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			try {
				if (needSampling) {
					return;
				}
				needSampling = true;
				while (needSampling) {
					if (mUI!=null) {
						int temp = 0;
						if (optType==OPT_TYPE_PLAY) {
							temp = (int)mPlayer.getMaxAmplitude();
							mUI.setMaxAmplitude(temp);
						}else if (needSampling) {
							temp = (int)mRecorder.getMaxAmplitude();
							mUI.setMaxAmplitude(temp);
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void stopSampling(){
			needSampling = false;
		}
		 
	 }

	@Override
	public boolean isRunning() {
		try {
			if (mRecorder!=null&&mRecorder.isRunning()) {
				return true;
			}
			if (mPlayer !=null&&mPlayer.isRunning()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void onResume(){
		try {
			int optType = -1;
			if (mSamplingMaxAmplitude!=null) {
				if (mRecorder!=null&&mRecorder.isRunning()) {
					optType = mSamplingMaxAmplitude.OPT_TYPE_RECORD;
				}else if (mPlayer !=null&&mPlayer.isRunning()) {
					optType = mSamplingMaxAmplitude.OPT_TYPE_PLAY;
				}
				if (optType==mSamplingMaxAmplitude.OPT_TYPE_PLAY||optType == mSamplingMaxAmplitude.OPT_TYPE_RECORD) {
					mSamplingMaxAmplitude.setOptType(optType);
					mSamplingMaxAmplitude.startSampling();
					mUI.awakeUP();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onPause(){
		try {
			if (mSamplingMaxAmplitude!=null) {
				mSamplingMaxAmplitude.stopSampling();
				mUI.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		try {
			mUI.onCompletion(arg0);
			if (listener!=null) {
				listener.onPlayOk();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		try {
			mUI.onError(arg0, arg1, arg2);
			if (listener!=null) {
				listener.onPlayError();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void timing(long remainTime) {
		try {
			if (mUI!=null) {
				if (mPlayer!=null&&!mPlayer.isPausing()&&mPlayer.isRunning()) {
					Float[] position = new Float[2];
					mPlayer.getCurrentPosition(position);
					mUI.setMax(position[IAudioPlayer.INDEX_DURATION].longValue());
					mUI.setProgress(position[IAudioPlayer.INDEX_CURRENT_POSITION].longValue());
				}else if (mRecorder!=null&&!mRecorder.isPuased()&&mRecorder.isRunning()) {
					mUI.setProgress(remainTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			if (listener!=null) {
				listener.onRecordOK(audioFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private CallbackListener listener;
	
	public void setCallbackListener(CallbackListener listener){
		this.listener = listener;
	}
	
	public static interface CallbackListener{
		public void onPlayError();
		public void onPlayOk();
		public void onRecordError();
		public void onRecordOK(String filePath);
		
	}
	
	public void forceClose(){
		try {
			if (mUI!=null) {
				mUI.forceClose();
			}
			exitFromUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isShowing(){
		return isShowing;
	}
}
