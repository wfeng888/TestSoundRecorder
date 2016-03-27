package com.paic.pamit.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
//  @ Project : Audio
//  @ File Name : AudioRecorderProxy.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
public class AudioRecorderProxy implements IAudioRecorder {
	public static final String TAG =  AudioRecorderProxy.class.getSimpleName();
	Context mContext;
	AudioRecordService.AudioServiceBind mProxy;
	private int RESULT_ERROR = -1;
	
	public AudioRecorderProxy(Context ctx) throws InvalidParamException{
		if (null==ctx) {
			Log.e(TAG, "AudioRecorderProxy must get a not null context");
			throw new InvalidParamException("AudioRecorderProxy must get a not null context");
		}
		this.mContext = ctx;
		Intent _intent = new Intent(mContext, AudioRecordService.class);
		ServiceConnection sc = new AudioServiceConnection();
		mContext.bindService(_intent, sc, Context.BIND_AUTO_CREATE);
	}
	
	class AudioServiceConnection implements ServiceConnection{

		public void onServiceConnected(ComponentName componentname,
				IBinder ibinder) {
			Log.d(TAG, componentname.toString());
			Log.d(TAG, "AudioRecordService has connected");
			mProxy = (AudioRecordService.AudioServiceBind)ibinder;
		}
		@Override
		public void onServiceDisconnected(ComponentName componentname) {
			Log.d(TAG, componentname.toString());
			Log.d(TAG, "AudioRecordService has disconnected");
			mProxy = null;
		}
	}
	public void setMaxFileSize(long maxSize){
		if (mProxy!=null) {
			mProxy.setMaxFileSize(maxSize);
		}
	};
	public void setOutputFile(String filePath){
		if (mProxy!=null) {
			mProxy.setOutputFile(filePath);
		}
	};
	public boolean startRecord(){
		if (mProxy!=null) {
			return mProxy.startRecord();
		}
		return false;
	};
	public boolean endRecord(){
		if (mProxy!=null) {
			mProxy.endRecord();
			return true;
		}
		return false;
	};
	public void setOutputFormat(int format){
		if (mProxy!=null) {
			mProxy.setOutputFormat(format);
		}
	};
	public void setAudioSamplingRate(int rate){
		if (mProxy!=null) {
		}
	};
	public void setAudioEncodingBitRate(int rate){
		if (mProxy!=null) {
			mProxy.setAudioEncodingBitRate(rate);
		}
	};
	public void setAudioEncoder(int encorderType){
		if (mProxy!=null) {
			mProxy.setAudioEncoder(encorderType);
		}
	};
	public boolean canPause(){
		if (mProxy!=null) {
			return mProxy.pause();
		}
		return false;
	};
	public long getMaxAmplitude(){
		if (mProxy!=null) {
			return mProxy.getMaxAmplitude();
		}
		return RESULT_ERROR;
	};
	public boolean isPuased(){
		if (mProxy!=null) {
			return mProxy.isPuased();
		}
		return false;
	};
	public boolean pause(){
		if (mProxy!=null) {
			return mProxy.pause();
		}
		return false;
	}
	@Override
	public boolean isRunning() {
		if (mProxy!=null) {
			return mProxy.isRunning();
		}
		return false;
	};
}
