package com.paic.pamit.audio.ui;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.testsoundrecorder.R;
import com.paic.pamit.audio.DeviceUtil;

public class AudioUI implements IAudioUI,android.view.View.OnClickListener{
	public static final String TAG = AudioUI.class.getSimpleName();

	private static final int PLAY_START_VISIBLE = 1;
	private static final int PLAY_STOP_VISIBLE = 2;
	private static final int PLAY_PAUSE_VISIBLE = 4;
	private static final int RECORD_START_VISIBLE = 8;
	private static final int RECORD_PAUSE_VISIBLE = 16;
	private static final int RECORD_STOP_VISIBLE = 32;
	private static final int PLAY_SEEK_BAR_PANNEL_VISIBLE = 64;
	private static final int SAVE_BUTTON_VISIBLE = 128;
	private static final int GIVE_UP_VISIBLE = 256; 
	private static final int PLAY_GIVE_UP_VISIBLE = 512;
	
	private static final int PLAY_START_ENABLE = 1;
	private static final int PLAY_STOP_ENABLE = 2;
	private static final int PLAY_PAUSE_ENABLE = 4;
	private static final int RECORD_START_ENABLE = 8;
	private static final int RECORD_PAUSE_ENABLE = 16;
	private static final int RECORD_STOP_ENABLE = 32;
	private static final int PLAY_SEEK_BAR_PANNEL_ENABLE = 64;
	private static final int SAVE_BUTTON_ENABLE = 128;
	private static final int GIVE_UP_ENABLE = 256; 
	private static final int PLAY_GIVE_UP_ENABLE = 512;
	
	
	private  int optMode;
	
	private int PROGRESS_SCALE = 10;
	private boolean animationRunning = false;
	
	
	RelativeLayout _contentView;
	Handler mHandler;
	Animation animIn;
	SpectrumView spectrumView;
	LinearLayout seekBarLayout;
	ImageButton btnPlay,btnPlayPause,btnPlayStop,btnRecord,btnRecordPause,btnRecordStop,btnSave,btnGiveUp/*,btnPlayGiveUP*/;
	SeekBar skbPlayback;
	TextView txtProgressPosition,txtProgressDuration,txtTopTime;

	private IAudioUIListener uiListener;
	
	private void changeButton(int controlVisiblePwd,int controlEnable){
		if((controlVisiblePwd&PLAY_START_VISIBLE) == PLAY_START_VISIBLE){
			btnPlay.setVisibility(View.VISIBLE);
			if ((controlEnable&PLAY_START_ENABLE) == PLAY_START_ENABLE) {
				btnPlay.setEnabled(true);
			}else{
				btnPlay.setEnabled(false);
			}
		}else{
			btnPlay.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&PLAY_STOP_VISIBLE) == PLAY_STOP_VISIBLE){
			btnPlayStop.setVisibility(View.VISIBLE);
			if ((controlEnable&PLAY_STOP_ENABLE) == PLAY_STOP_ENABLE) {
				btnPlayStop.setEnabled(true);
			}else{
				btnPlayStop.setEnabled(false);
			}
		}else{
			btnPlayStop.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&PLAY_PAUSE_VISIBLE) == PLAY_PAUSE_VISIBLE){
			btnPlayPause.setVisibility(View.VISIBLE);
			if ((controlEnable&PLAY_PAUSE_ENABLE) == PLAY_PAUSE_ENABLE) {
				btnPlayPause.setEnabled(true);
			}else{
				btnPlayPause.setEnabled(false);
			}
		}else{
			btnPlayPause.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&RECORD_START_VISIBLE) == RECORD_START_VISIBLE){
			btnRecord.setVisibility(View.VISIBLE);
			if ((controlEnable&RECORD_START_ENABLE) == RECORD_START_ENABLE) {
				btnRecord.setEnabled(true);
			}else{
				btnRecord.setEnabled(false);
			}
		}else{
			btnRecord.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&RECORD_PAUSE_VISIBLE) == RECORD_PAUSE_VISIBLE){
			btnRecordPause.setVisibility(View.VISIBLE);
			if ((controlEnable&RECORD_PAUSE_ENABLE) == RECORD_PAUSE_ENABLE) {
				btnRecordPause.setEnabled(true);
			}else{
				btnRecordPause.setEnabled(false);
			}
		}else{
			btnRecordPause.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&RECORD_STOP_VISIBLE) == RECORD_STOP_VISIBLE){
			btnRecordStop.setVisibility(View.VISIBLE);
			if ((controlEnable&RECORD_STOP_ENABLE) == RECORD_STOP_ENABLE) {
				btnRecordStop.setEnabled(true);
			}else{
				btnRecordStop.setEnabled(false);
			}
		}else{
			btnRecordStop.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&PLAY_SEEK_BAR_PANNEL_VISIBLE) == PLAY_SEEK_BAR_PANNEL_VISIBLE){
			seekBarLayout.setVisibility(View.VISIBLE);
		}else{
			seekBarLayout.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&SAVE_BUTTON_VISIBLE) == SAVE_BUTTON_VISIBLE){
			btnSave.setVisibility(View.VISIBLE);
			if ((controlEnable&SAVE_BUTTON_ENABLE) == SAVE_BUTTON_ENABLE) {
				btnSave.setEnabled(true);
			}else{
				btnSave.setEnabled(false);
			}
		}else{
			btnSave.setVisibility(View.INVISIBLE);
		}
		if((controlVisiblePwd&GIVE_UP_VISIBLE) == GIVE_UP_VISIBLE){
			btnGiveUp.setVisibility(View.VISIBLE);
			if ((controlEnable&GIVE_UP_ENABLE) == GIVE_UP_ENABLE) {
				btnGiveUp.setEnabled(true);
			}else{
				btnGiveUp.setEnabled(false);
			}
		}else{
			btnGiveUp.setVisibility(View.INVISIBLE);
		}
//		if((controlVisiblePwd&PLAY_GIVE_UP_VISIBLE) == PLAY_GIVE_UP_VISIBLE){
//			btnPlayGiveUP.setVisibility(View.VISIBLE);
//			if ((controlEnable&PLAY_GIVE_UP_ENABLE) == PLAY_GIVE_UP_ENABLE) {
//				btnPlayGiveUP.setEnabled(true);
//			}else{
//				btnPlayGiveUP.setEnabled(false);
//			}
//		}else{
//			btnPlayGiveUP.setVisibility(View.INVISIBLE);
//		}
	}
	
	public AudioUI(Context ctx){
		Looper _looper = Looper.getMainLooper();
		mHandler = new Handler(_looper);
		if (animIn==null&&ctx!=null) {
			animIn = AnimationUtils.loadAnimation(ctx, R.anim.modal_in);
		}
		if (_contentView==null) {
			_contentView = (RelativeLayout)LayoutInflater.from((Context)ctx).inflate(R.layout.sound_content, null);
			initResource();
		}else if (_contentView.getParent()!=null) {
			((ViewGroup)_contentView.getParent()).removeView(_contentView);
		}
	}
	@Override
	public void setMaxAmplitude(int maxAmplitude) {
		spectrumView.setMaxAmplitude(maxAmplitude);
	}

	@Override
	public void setProgress(final long time) {
		final String s = TimeToShow.timeHMSMIL(time);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				txtTopTime.setText(s);
				if (seekBarLayout.getVisibility()==View.VISIBLE) {
					txtProgressPosition.setText(s);
					int tmp = (int)(time*PROGRESS_SCALE);
					skbPlayback.setProgress(tmp);
				}
				
			}
		});
	}
	
	@Override
	public void setMax(final long max) {
		final String s = TimeToShow.timeHMSMIL(max);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				int tmp = (int)(max*PROGRESS_SCALE);
				skbPlayback.setMax(tmp);
				txtProgressDuration.setText(s);
			}
		});
		
	}
	
	private void initResource(){
		if (_contentView!=null) {
			btnPlay = (ImageButton)_contentView.findViewById(R.id.btn_play);
			btnPlay.setOnClickListener(this);
			btnPlayPause = (ImageButton)_contentView.findViewById(R.id.btn_play_pause);
			btnPlayPause.setOnClickListener(this);
			btnPlayStop = (ImageButton)_contentView.findViewById(R.id.btn_play_stop);
			btnPlayStop.setOnClickListener(this);
			btnRecord = (ImageButton)_contentView.findViewById(R.id.btn_record);
			btnRecord.setOnClickListener(this);
			btnRecordPause = (ImageButton)_contentView.findViewById(R.id.btn_record_pause);
			btnRecordPause.setOnClickListener(this);
			btnRecordStop = (ImageButton)_contentView.findViewById(R.id.btn_record_stop);
			btnRecordStop.setOnClickListener(this);
			seekBarLayout = (LinearLayout)_contentView.findViewById(R.id.seekbar_layout);
			spectrumView = (SpectrumView)_contentView.findViewById(R.id.audio_content);
			skbPlayback = (SeekBar)_contentView.findViewById(R.id.skb_playback);
			skbPlayback.setOnSeekBarChangeListener(_SKBChangeListener);
			txtProgressPosition = (TextView)_contentView.findViewById(R.id.txt_progress_position);
			txtProgressDuration = (TextView)_contentView.findViewById(R.id.txt_progress_duration);
			txtTopTime = (TextView)_contentView.findViewById(R.id.time_text);
			btnSave = (ImageButton)_contentView.findViewById(R.id.btn_finish);
			btnSave.setOnClickListener(this);
			btnGiveUp = (ImageButton)_contentView.findViewById(R.id.btn_give_up);
			btnGiveUp.setOnClickListener(this);
//			btnPlayGiveUP = (ImageButton)_contentView.findViewById(R.id.btn_play_give_up);
//			btnPlayGiveUP.setOnClickListener(this);
		}
	}
	
	OnSeekBarChangeListener _SKBChangeListener =  new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {
			if (uiListener!=null) {
				uiListener.setProgressFromUI(((long)seekbar.getProgress())/PROGRESS_SCALE);
				startPlay();
			}
			
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekbar) {
			pausePlay();
		}
		@Override
		public void onProgressChanged(SeekBar seekbar, int i, boolean flag) {
		}
	};
	
	@Override
	public void createView(final Activity activity,final int optType) {
		
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				FrameLayout.LayoutParams _lp = null;
				if (activity!=null&&!activity.isFinishing()) {
					int width = DeviceUtil.dip2px((activity), activity.getResources().getDimension(R.dimen.sound_content_width));
					int height = DeviceUtil.dip2px(activity, activity.getResources().getDimension(R.dimen.sound_content_height));
					_lp = new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
					activity.addContentView(_contentView, _lp);
					_contentView.startAnimation(animIn);
					
					if (optType==OPT_TYPE_PLAY) {
						optMode = OPT_TYPE_PLAY;
						startPlay();
					}else {
						optMode = OPT_TYPE_RECORD;
						initialRecordView();
					}
				}
			}
		});
		
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_play:
			startPlay();
			break;
		case R.id.btn_play_pause:
			pausePlay();
			break;
		case R.id.btn_play_stop:
			stopPlay();
			break;
		case R.id.btn_record:
			startRecord();
			break;
		case R.id.btn_record_pause:
			pauseRecord();
			break;
		case R.id.btn_record_stop:
			stopRecord();
			break;
		case R.id.btn_finish:
			save();
			break;
		case R.id.btn_give_up:
			exit();
			break;
		default:
			break;
		}
	}
	
	private void exit(){
		if (_contentView!=null) {
			if (_contentView.getParent()!=null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						((ViewGroup)_contentView.getParent()).removeView(_contentView);
					}
				});
			}
		}
		if (uiListener!=null) {
			uiListener.exitFromUI();
		}
	}
	
	private void initialRecordView(){
		int controlVisiblePwd = RECORD_STOP_VISIBLE|RECORD_START_VISIBLE|GIVE_UP_VISIBLE;
		int controlEnablePwd = RECORD_START_VISIBLE|GIVE_UP_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
	}
	
	public void startPlayView() {
		int controlVisiblePwd;
		int controlEnablePwd;
		if (optMode==OPT_TYPE_PLAY) {
			controlVisiblePwd = PLAY_SEEK_BAR_PANNEL_VISIBLE|PLAY_PAUSE_VISIBLE|PLAY_STOP_VISIBLE;
		}else{
			controlVisiblePwd = PLAY_SEEK_BAR_PANNEL_VISIBLE|PLAY_PAUSE_VISIBLE|PLAY_STOP_VISIBLE|RECORD_START_VISIBLE;
		}
		controlEnablePwd = PLAY_PAUSE_ENABLE|PLAY_STOP_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
		startAnimation();
	}
	
	public void startPlay(){
		if (uiListener!=null) {
			uiListener.startPlayFromUI();
		}
		startPlayView();
	}
	
	private void pausePlayView(){
		int controlVisiblePwd;
		if (optMode==OPT_TYPE_PLAY) {
			controlVisiblePwd = PLAY_SEEK_BAR_PANNEL_VISIBLE|PLAY_START_VISIBLE|PLAY_STOP_VISIBLE;
		}else{
			controlVisiblePwd = PLAY_SEEK_BAR_PANNEL_VISIBLE|PLAY_START_VISIBLE|PLAY_STOP_VISIBLE|RECORD_START_VISIBLE;
		}
		int controlEnablePwd = PLAY_START_ENABLE|PLAY_STOP_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
		stopAnimation();
	}
	
	private void pausePlay(){
		if (uiListener!=null) {
			uiListener.pausePlayFromUI();
		}
		pausePlayView();
	}
	
	private void stopPlayView(){
		int controlVisiblePwd;
		int controlEnablePwd;
		if (optMode==OPT_TYPE_PLAY) {
			exit();
		}else{
			controlVisiblePwd = PLAY_SEEK_BAR_PANNEL_VISIBLE|PLAY_START_VISIBLE|SAVE_BUTTON_VISIBLE|RECORD_START_VISIBLE;
			controlEnablePwd = PLAY_START_ENABLE|RECORD_START_ENABLE|SAVE_BUTTON_ENABLE;
			changeButton(controlVisiblePwd,controlEnablePwd);
			String formatString = "00:00:00:000";
			txtProgressDuration.setText(formatString);
			txtProgressDuration.setText(formatString);
			txtProgressPosition.setText(formatString);
			skbPlayback.setProgress(0);
			stopAnimation();
		}
	}
	
	private void startRecord(){
		if (uiListener!=null) {
			uiListener.startRecordFromUI();
		}
		startRecordView();
	}
	
	private void startRecordView(){
		
		int controlVisiblePwd;
		int controlEnablePwd;
		controlVisiblePwd = RECORD_PAUSE_VISIBLE|RECORD_STOP_VISIBLE|PLAY_START_VISIBLE;
		controlEnablePwd = RECORD_PAUSE_ENABLE|RECORD_STOP_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
		startAnimation();
	}
	
	private void pauseRecord(){
		if (uiListener!=null) {
			uiListener.pauseRecordFromUI();
		}
		pauseRecordView();
	}
	
	private void pauseRecordView(){
		int controlVisiblePwd = RECORD_START_VISIBLE|RECORD_STOP_VISIBLE|PLAY_START_VISIBLE;
		int controlEnablePwd = RECORD_START_ENABLE|RECORD_STOP_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
		stopAnimation();
	}
	
	private void stopRecord(){
		if (uiListener!=null) {
			uiListener.stopRecordFromUI();
		}
		stopRecordView();
	}
	private void stopRecordView(){
		int controlVisiblePwd = RECORD_START_VISIBLE|PLAY_START_VISIBLE|SAVE_BUTTON_VISIBLE;
		int controlEnablePwd = RECORD_START_ENABLE|PLAY_START_ENABLE|SAVE_BUTTON_ENABLE;
		changeButton(controlVisiblePwd,controlEnablePwd);
		stopAnimation();
	}
	
	private void stopPlay(){
		stopPlayView();
		if (uiListener!=null) {
			uiListener.stopPlayFromUI();
		}
	}
	
	private void save(){
		if (uiListener!=null) {
			uiListener.save();
		}
		exit();
	}
	
	private void startAnimation(){
		if (spectrumView!=null) {
			spectrumView.startAnimation();
		}
	}
    
	private void stopAnimation(){
		if (spectrumView!=null) {
			spectrumView.stopAnimation();
		}
	}
	
	@Override
	public void setAudioUIListener(IAudioUIListener listener) {
		this.uiListener = listener;
	}

	@Override
	public void awakeUP() {
		if (uiListener!=null&&uiListener.isRunning()) {
			startAnimation();
		}
	}
	@Override
	public void sleep() {
		stopAnimation();
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		stopPlayView();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		stopPlayView();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		
	}
	
	static class TimeToShow{
		public static String timeHMSMIL(long time){
			StringBuilder _sb = new StringBuilder();
			long mils = time%1000;
			long tmp = time/1000;
			_sb.append(String.format("%02d", tmp/(60*60))).append(":").append(String.format("%02d", (tmp/60)%60)).append(":").append(String.format("%02d", tmp%60)).append(":").append(String.format("%03d", time%1000));
			return _sb.toString();
		}
	}

	@Override
	public void forceClose() {
		exit();
	}

}
