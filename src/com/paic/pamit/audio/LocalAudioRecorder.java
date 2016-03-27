package com.paic.pamit.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.util.Log;
//  @ Project : Audio
//  @ File Name : LocalAudioRecorder.java
//  @ Date : 2016-03-23
//  @ Author : wangfeng475

public class LocalAudioRecorder implements IAudioRecorder {

	private Boolean latch = true;
	private final String TAG = LocalAudioRecorder.class.getSimpleName();
	private LinkedList<String> fileList = new LinkedList<String>();
	public static final String SUFFIX = ".amr";
	private static final boolean CAN_PAUSE = true;
	private boolean recoderRunning = false;
	private boolean recoderPausing = false;
	//¼���ļ�����С����λKB
	private long maxFileSize;
	private String filePath;
	private int outFormat;

	private MediaRecorder recorder;

	public LocalAudioRecorder() {
	}

	public void setMaxFileSize(long maxSize) {
		if (maxSize<512) {
			maxFileSize = 512;
		}
		maxFileSize = maxSize;
	};

	public void setOutputFile(String filePath) {
		this.filePath = filePath;
	};

	public boolean startRecord() {
			if (recoderRunning) {
				endRecord();
			}
			try {
				
				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder
						.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
				recorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				if (null==filePath) {
					return false;
				}
				if (recoderPausing) {
					filePath = filePath.substring(0, filePath.lastIndexOf(File.separator)+1)+System.currentTimeMillis()+SUFFIX;
					fileList.add(filePath);
				}else{
					fileList.add(filePath);
				}
				recorder.setOutputFile(filePath);
				recorder.prepare();
				recorder.start();
				recorder.setOnInfoListener(new OnInfoListener() {
					@Override
					public void onInfo(MediaRecorder mr, int what, int extra) {
						int mMax = mr.getMaxAmplitude();
						Log.i(TAG, String.valueOf(mMax));
					}
				});
				recoderRunning = true;
				recoderPausing = false;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					endRecord();
				} catch (Exception ex) {
					e.printStackTrace();
				}
				return false;
			}
			return true;
		}

	public boolean endRecord() {
			if (recoderRunning&&recorder!=null) {
				recorder.stop();
				recorder.release();
				recorder = null;
			}
			recoderRunning = false;
			recoderPausing = false;
			try {
				montageFile();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

	public void setOutputFormat(int format) {
		this.outFormat = format;
	};

	public void setAudioSamplingRate(int rate) {
		//recorder.setAudioSamplingRate(rate);
	};

	public void setAudioEncodingBitRate(int rate) {
		//recorder.setAudioEncodingBitRate(rate);
	};

	public void setAudioEncoder(int encorderType) {
		//recorder.setAudioEncoder(encorderType);
	};

	public void setAudioEncoder(IAudioEncoder encorder) {
	};

	public boolean canPause() {
		return CAN_PAUSE;
	};

	public long getMaxAmplitude() {
		if (recorder!=null) {
			return recorder.getMaxAmplitude();
		}
		return 0;
	};

	public boolean isPuased() {
		return recoderPausing;
	};

	public boolean pause() {
		try {
			if (recoderRunning&&recorder!=null) {
				recorder.stop();
				recorder.release();
				recorder = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			recoderRunning = false;
			recoderPausing = true;
		}
		return true;
	}

	private void montageFile() throws Exception {
		HashMap<String, String> wrongsEscape = new HashMap<String, String>();
		File _file;
		FileOutputStream _ous = null;
		String _filePath = null;
		FileInputStream _in = null;
		try {
		if (fileList.size() > 1) {
				Iterator<String> _iterator = fileList.iterator();
				try {
					_filePath = _iterator.next();
					_ous = new FileOutputStream(_filePath, true);
				} catch (FileNotFoundException e) {
					Log.e(TAG, "File " + _filePath + " is not exists");
					e.printStackTrace();
					throw e;
				}
				wrongsEscape.put(_filePath, _filePath);
				byte[] buffer = new byte[1024];
				byte[] bufferHead = new byte[6];
				int _readLength;
				while (_iterator.hasNext()) {
					_file = new File(_iterator.next());
					
					if (_file.exists()&&!wrongsEscape.containsKey(_file.getAbsolutePath())) {
						try {
							_in = new FileInputStream(_file);
							_in.read(bufferHead);
							while ((_readLength = _in.read(buffer)) > -1) {
								_ous.write(buffer, 0, _readLength);
							}
						} catch (Exception e) {
							Log.e(TAG, "File " + _file.getAbsolutePath()
									+ " is not exists");
							e.printStackTrace();
							throw e;
						} finally {
							if (_in != null) {
								_in.close();
							}
						}
						_file.delete();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (_ous != null) {
				_ous.close();
			}
			if (fileList!=null) {
				fileList.clear();
			}
			if (wrongsEscape!=null) {
				wrongsEscape.clear();
			}
		}
	}

	@Override
	public boolean isRunning() {
		return recoderRunning&&!recoderPausing;
	}
}
