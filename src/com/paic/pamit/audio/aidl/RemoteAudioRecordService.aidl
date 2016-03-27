package com.paic.pamit.audio.aidl;
interface RemoteAudioRecordService{
      void setMaxFileSize(long maxSize);
	  void setOutputFile(String filePath);
	  void startRecord();
	  void endRecord();
	  void setOutputFormat(in int format);
	  void setAudioSamplingRate();
	  void setAudioEncodingBitRate(in int rate);
	  void setAudioEncoder(in int encorderType);
	  boolean canPause();
	  long getMaxAmplitude();
	  boolean isPuased();
	  void pause();
}