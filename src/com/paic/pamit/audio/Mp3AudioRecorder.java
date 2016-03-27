package com.paic.pamit.audio;
//
//
//
//  @ Project : Audio
//  @ File Name : Mp3AudioRecorder.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475
//
//

public class Mp3AudioRecorder implements IAudioRecorder {
	public void setMaxFileSize(long maxSize){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public void setOutputFile(String filePath){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public boolean startRecord(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public boolean endRecord(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public void setOutputFormat(int format){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public void setAudioEncodingBitRate(int rate){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public void setAudioEncoder(int encorderType){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public void setAudioEncoder(IAudioEncoder encorder){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public boolean canPause(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public long getMaxAmplitude(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public boolean isPuased(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
	public boolean pause(){
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	}
	@Override
	public void setAudioSamplingRate(int rate) {
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	}
	@Override
	public boolean isRunning() {
		throw new UnsupportedOperationException("unsupport mp3 audio record");
	};
}
