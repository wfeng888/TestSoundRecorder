package com.paic.pamit.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public final class TimerService {
	
	private static TimerService mTimerService;
	private ArrayList<TimerListener> customers = new ArrayList<TimerListener>();
	private HashMap<TimerListener, ServiceTarget> customerRemainTimeList = new HashMap<TimerService.TimerListener, TimerService.ServiceTarget>();
	Timer mTimer;
	
	class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			long remain;
			ServiceTarget _serviceTarget;
			for(TimerListener s:customers){
				if (customerRemainTimeList.containsKey(s)) {
					_serviceTarget = customerRemainTimeList.get(s);
					remain = System.currentTimeMillis()-_serviceTarget.getStartTime()+_serviceTarget.getAddTime();
				}else{
					remain = 0;
				}
				s.timing(remain);
			}
			_serviceTarget = null;
		
		}
		
	}
	
	public static TimerService getDefault(){
		if(mTimerService==null){
			mTimerService = new TimerService();
		}
		return mTimerService;
	}
	
	public boolean start(TimerListener target){
		if(!customers.contains(target)){
			synchronized(customers){
				if(!customers.contains(target)){
					customers.add(target);
					ServiceTarget _serviceTarget;
					if (customerRemainTimeList.containsKey(target)) {
						_serviceTarget = customerRemainTimeList.get(target);
						_serviceTarget.startTime = System.currentTimeMillis();
					}else{
						_serviceTarget = new ServiceTarget(System.currentTimeMillis(), 0);
						customerRemainTimeList.put(target, _serviceTarget);
					}
				}
			}
		}
		if(mTimer == null){
			new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (mTimerService) {
						if(mTimer==null){
							mTimer = new Timer();
							mTimer.schedule(new MyTimerTask(), 1000, 200);
						}
					}
				}
			}).start();
		}
		return true;
	}
	
	public boolean pause(TimerListener target){
		if (customerRemainTimeList.containsKey(target)) {
			ServiceTarget _serviceTarget = customerRemainTimeList.get(target);
			_serviceTarget.addTime += System.currentTimeMillis() - _serviceTarget.startTime;
		}
		return stop(target,false);
	}
	
	private boolean stop(TimerListener target,boolean removeServiceTarget){
		if(customers.contains(target)){
			synchronized(mTimerService){
				if(customers.contains(target)){
					customers.remove(target);
					if (removeServiceTarget&&customerRemainTimeList.containsKey(target)) {
						customerRemainTimeList.remove(target);
					}
				}
			}
		}
		if(customers.size()<1){
			return reset();
		}
		return false;
	}
	
	public boolean stop(TimerListener target){
		return stop(target,true);
	}
	
	private boolean reset(){
		if(mTimer!=null){
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
//			mTimerService = null;
		}
		return true;
	}
	
	class ServiceTarget{
		private long startTime;
		private long addTime;
		public ServiceTarget(long startTime,long addTime){
			this.startTime = startTime;
			this.addTime = this.addTime;
		}
		
		public long getStartTime() {
			return startTime;
		}
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
		public long getAddTime() {
			return addTime;
		}
		public void setAddTime(long addTime) {
			this.addTime = addTime;
		}
		
	} 
	
	public interface TimerListener{
		public void timing(long remainTime);
	}
	
}
