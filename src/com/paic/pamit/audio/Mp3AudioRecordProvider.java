package com.paic.pamit.audio;
//  @ Project : Audio
//  @ File Name : Mp3AudioRecordProvider.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475
public class Mp3AudioRecordProvider implements IAudioRecordProvider {
	public IAudioRecorder provideRecorder() {
		return new Mp3AudioRecorder();
	}
}
