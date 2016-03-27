package com.paic.pamit.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AmplitudeUtil {

	//=======================AudioRecord Default Settings=======================
	private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
	/**
	 * 浠ヤ笅涓夐」涓洪粯璁ら厤缃弬鏁般�侴oogle Android鏂囨。鏄庣‘琛ㄦ槑鍙湁浠ヤ笅3涓弬鏁版槸鍙互鍦ㄦ墍鏈夎澶囦笂淇濊瘉鏀寔鐨勩��
	 */
	private static final int DEFAULT_SAMPLING_RATE = 44100;//妯℃嫙鍣ㄤ粎鏀寔浠庨害鍏嬮杈撳叆8kHz閲囨牱鐜�
	private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	/**
	 * 涓嬮潰鏄姝ょ殑灏佽
	 * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	 */
	private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;
	
	//======================Lame Default Settings=====================
	private static final int DEFAULT_LAME_MP3_QUALITY = 7;
	/**
	 * 涓嶥EFAULT_CHANNEL_CONFIG鐩稿叧锛屽洜涓烘槸mono鍗曞０锛屾墍浠ユ槸1
	 */
	private static final int DEFAULT_LAME_IN_CHANNEL = 1;
	/**
	 *  Encoded bit rate. MP3 file will be encoded with bit rate 32kbps 
	 */ 
	private static final int DEFAULT_LAME_MP3_BIT_RATE = 32;
	
	//==================================================================
	
	/**
	 * 鑷畾涔� 姣�0甯т綔涓轰竴涓懆鏈燂紝閫氱煡涓�涓嬮渶瑕佽繘琛岀紪鐮�
	 */
	private static final int FRAME_COUNT = 160;
	private AudioRecord mAudioRecord = null;
	private int mBufferSize;
	private short[] mPCMBuffer;
	private boolean mIsRecording = false;

	/**
	 * Start recording. Create an encoding thread. Start record from this
	 * thread.
	 * 
	 * @throws IOException  initAudioRecorder throws
	 */
	public void start() throws IOException {
		if (mIsRecording) return;
	    initAudioRecorder();
		mAudioRecord.startRecording();
		new Thread() {

			@Override
			public void run() {
				try {
					//璁剧疆绾跨▼鏉冮檺
					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
					mIsRecording = true;
					while (mIsRecording) {
						int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
						if (readSize > 0) {
							calculateRealVolume(mPCMBuffer, readSize);
						}
					}
					// release and finalize audioRecord
					mAudioRecord.stop();
					mAudioRecord.release();
					mAudioRecord = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**
			 * 姝よ绠楁柟娉曟潵鑷猻amsung寮�鍙戣寖渚�
			 * 
			 * @param buffer buffer
			 * @param readSize readSize
			 */
			private void calculateRealVolume(short[] buffer, int readSize) {
				double sum = 0;
				for (int i = 0; i < readSize; i++) {  
				    // 杩欓噷娌℃湁鍋氳繍绠楃殑浼樺寲锛屼负浜嗘洿鍔犳竻鏅扮殑灞曠ず浠ｇ爜  
				    sum += buffer[i] * buffer[i]; 
				} 
				if (readSize > 0) {
					double amplitude = sum / readSize;
					mVolume = (int) Math.sqrt(amplitude);
				}
			};
		}.start();
	}
	private int mVolume;
	public int getVolume(){
		return mVolume;
	}
	private static final int MAX_VOLUME = 2000;
	public int getMaxVolume(){
		return MAX_VOLUME;
	}
	public void stop(){
		mIsRecording = false;
	}
	public boolean isRecording() {
		return mIsRecording;
	}
	/**
	 * Initialize audio recorder
	 */
	private void initAudioRecorder() throws IOException {
		mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
				DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());
		
		int bytesPerFrame = DEFAULT_AUDIO_FORMAT.getBytesPerFrame();
		/* Get number of samples. Calculate the buffer size 
		 * (round up to the factor of given frame size) 
		 * 浣胯兘琚暣闄わ紝鏂逛究涓嬮潰鐨勫懆鏈熸�ч�氱煡
		 * */
		int frameSize = mBufferSize / bytesPerFrame;
		if (frameSize % FRAME_COUNT != 0) {
			frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
			mBufferSize = frameSize * bytesPerFrame;
		}
		
		/* Setup audio recorder */
		mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
				DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(),
				mBufferSize);
		
		mPCMBuffer = new short[mBufferSize];
	}
}