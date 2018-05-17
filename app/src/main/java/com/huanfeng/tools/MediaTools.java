package com.huanfeng.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.huanfeng.view.HFActivity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MediaTools
{
	private static ArrayList<MediaPlayer> cacheList = new ArrayList<>();

	public static MediaPlayer play(int resid){
		return play(resid,false);
	}
	
	public static MediaPlayer play(int resid,boolean loop){
		MediaPlayer mp = MediaPlayer.create(HFActivity.topActivity, resid);
		if(mp != null){
			cacheList.add(mp);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					cacheList.remove(mp);
				}
			});
		}
		return mp;
	}
	
}
