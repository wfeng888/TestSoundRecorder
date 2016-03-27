package com.paic.pamit.audio.ui;

import java.util.LinkedList;

public class MyLinkedList{
	LinkedList mList;
	public MyLinkedList(LinkedList list){
		mList = list;
	}
	
	public void addFirst(Object obj){
		mList.addFirst(obj);
	}
	
	public void removeLast(){
		if (mList.size()>0) {
			mList.remove(mList.size()-1);
		}
	}
	
	public MyIterator iterator(){
		return new MyIterator(mList);
	}
	
	public Object getLast(){
		if (mList.size()>0) {
			return mList.get(mList.size()-1);
		}
		return null;
	}
	
	public int size(){
		return mList.size();
	}
	
	public void clear(){
		mList.clear();
	}
	
	static class MyIterator{
		int position = -1;
		LinkedList mList;
		private MyIterator(LinkedList list){
			mList = list;
		}
		public boolean hasNext(){
			if (position+1<mList.size()) {
				return true;
			}
			return false;
		}
		public Object next(){
			if (hasNext()) {
				position +=1;
				return mList.get(position);
			}
			return null;
		}
	}
}
