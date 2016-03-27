package com.example.testsoundrecorder;

import java.io.File;

import com.paic.pamit.audio.AudioInstance;
import com.paic.pamit.audio.DeviceUtil;
import com.paic.pamit.audio.InvalidParamException;
import com.paic.pamit.audio.ui.SpectrumView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements android.view.View.OnClickListener,AudioInstance.CallbackListener {

	SpectrumView audioView;
	Button play,record;
	AudioInstance audio;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			play = (Button)findViewById(R.id.play);
			play.setOnClickListener(this);
			record = (Button)findViewById(R.id.record);
			record.setOnClickListener(this);
			audio = new AudioInstance();
	}
	
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (audio!=null) {
			audio.onPause();
		}
	}




	@Override
	protected void onResume() {
		super.onResume();
		if (audio!=null) {
			audio.onResume();
		}
	}




	public void test(){
		RelativeLayout soundContent = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.sound_content, null);
		FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
				DeviceUtil.dip2px(this, this.getResources().getDimension(R.dimen.sound_content_width)),
				DeviceUtil.dip2px(this, this.getResources().getDimension(R.dimen.sound_content_height)),
				Gravity.CENTER);
		lParams.gravity = Gravity.CENTER;
		addContentView(soundContent, lParams);
		Animation animIN = AnimationUtils.loadAnimation(this, R.anim.modal_in);
		soundContent.startAnimation(animIN);
		audioView = (SpectrumView)soundContent.findViewById(R.id.audio_content);
		audioView.startAnimation();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					audioView.setMaxAmplitude((int)(Math.random()*50000));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			//audio.startPlay(filePath);
			break;
		case R.id.record:
			String audioFilePath = getExternalFilesDir("AudioRecord").getAbsolutePath()+File.separator+System.currentTimeMillis()+".amr";
			try {
				audio.startRecord(this,audioFilePath);
			} catch (InvalidParamException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onPlayError() {
		
	}
	@Override
	public void onPlayOk() {
		
	}
	@Override
	public void onRecordError() {
		
	}
	@Override
	public void onRecordOK(String filePath) {
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (audio != null&&audio.isShowing()) {
				audio.forceClose();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
