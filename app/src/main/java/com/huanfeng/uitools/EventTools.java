package com.huanfeng.uitools;

import com.huanfeng.tools.Utils;

public class EventTools
{
	private static int disableKeyEventConter;
	private static int disableTouchEventConter;
	
	public static int getDisableKeyEventConter() {
		return disableKeyEventConter;
	}

	public static int getDisableTouchEventConter() {
		return disableTouchEventConter;
	}

	public static void pushDisableKeyEvent(){
		disableKeyEventConter++;
	}
	
	public static void releaseDisableKeyEvent(){
		disableKeyEventConter = Math.max(0, disableKeyEventConter - 1);
	}
	
	public static void clearDisableKeyEvent(){
		disableKeyEventConter = 0;
	}
	
	public static void pushDisableTouchEvent(){
		disableTouchEventConter++;
	}
	
	public static void releaseDisableTouchEvent(){
		disableTouchEventConter = Math.max(0, disableTouchEventConter - 1);
	}
	
	public static void clearDisableTouchEvent(){
		disableTouchEventConter = 0;
	}
	
	public static void pushDisableAnyEvent(){
		pushDisableKeyEvent();
		pushDisableTouchEvent();
	}
	
	public static void releaseDisableAnyEvent(){
		releaseDisableKeyEvent();
		releaseDisableTouchEvent();
	}
	
	public static void clearDisableAnyEvent(){
		clearDisableKeyEvent();
		clearDisableTouchEvent();
	}
}
