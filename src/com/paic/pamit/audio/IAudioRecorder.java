package com.paic.pamit.audio;
//  @ Project : Audio
//  @ File Name : IAudioRecorder.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475
public interface IAudioRecorder {
	public  void setMaxFileSize(long maxSize);
	public  void setOutputFile(String filePath);
	public  boolean startRecord();
	public  boolean endRecord();
	public  void setOutputFormat(int format);
	public  void setAudioSamplingRate(int rate);
	public  void setAudioEncodingBitRate(int rate);
	public  void setAudioEncoder(int encorderType);
	public  boolean canPause();
	public  long getMaxAmplitude();
	public  boolean isPuased();
	public  boolean pause();
	public  boolean isRunning();
}
