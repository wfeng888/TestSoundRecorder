package com.paic.pamit.audio;
//  @ Project : Audio
//  @ File Name : LocalAudioRecordProvider.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475
//
//
public class LocalAudioRecordProvider implements IAudioRecordProvider {
	public IAudioRecorder provideRecorder() {
		return new LocalAudioRecorder();
	}
}
