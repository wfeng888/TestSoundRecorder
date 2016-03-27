package com.paic.pamit.audio;
//@ Project : Audio
//@ File Name : AudioRecordService.java
//@ Date : 2016-03-23
//@ Author : wangfeng475
import java.io.FileDescriptor;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class AudioRecordService extends Service{
	
	IAudioRecorder mRecorder;
	AudioServiceBind mBind;
	@Override
	public IBinder onBind(Intent arg0) {
		return mBind;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mRecorder = new LocalAudioRecordProvider().provideRecorder();
		mBind = new AudioServiceBind();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mRecorder = null;
		mBind = null;
	}
	private void sendToService(Context context, Intent intent) {
	}
	class AudioServiceBind extends Binder implements IAudioRecorder{

		@Override
		public void setMaxFileSize(long maxSize) {
			mRecorder.setMaxFileSize(maxSize);
		}

		@Override
		public void setOutputFile(String filePath) {
			mRecorder.setOutputFile(filePath);
		}

		@Override
		public boolean startRecord() {
			return mRecorder.startRecord();
		}

		@Override
		public boolean endRecord() {
			return mRecorder.endRecord();
		}

		@Override
		public void setOutputFormat(int format) {
			mRecorder.setOutputFormat(format);
		}

		@Override
		public void setAudioSamplingRate(int rate) {
			mRecorder.setAudioSamplingRate(rate);
		}

		@Override
		public void setAudioEncodingBitRate(int rate) {
			mRecorder.setAudioEncodingBitRate(rate);
		}

		@Override
		public void setAudioEncoder(int encorderType) {
			mRecorder.setAudioEncoder(encorderType);
		}

		@Override
		public boolean canPause() {
			return mRecorder.canPause();
		}

		@Override
		public long getMaxAmplitude() {
			return mRecorder.getMaxAmplitude();
		}

		@Override
		public boolean isPuased() {
			return mRecorder.isPuased();
		}

		@Override
		public boolean pause() {
			return mRecorder.pause();
		}

		@Override
		public boolean isRunning() {
			return mRecorder.isRunning();
		}
		
	}
}
