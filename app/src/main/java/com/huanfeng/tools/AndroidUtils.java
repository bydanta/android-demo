package com.huanfeng.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.WebView;

import com.huanfeng.view.HFActivity;

public class AndroidUtils
{
	public static boolean isApkDebugable(Context context) {
		try {
			ApplicationInfo info = context.getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {

		}
		return false;
	}

	public static void openUrl(String url) {
		if (url != null && url.startsWith("http")) {
			final Uri uri = Uri.parse(url);
			final Intent it = new Intent(Intent.ACTION_VIEW, uri);
			HFActivity.topActivity.startActivity(it);
		}
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName() {
		String versionName = "";
		try {
			Context context = HFActivity.topActivity;
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
	 * 返回当前程序版本号
	 */
	public static int getAppVersionCode() {
		int versionCode = 0;
		try {
			Context context = HFActivity.topActivity;
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 拨打电话
	 * @param number
	 */
	public static void callPhone(String number){
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
        HFActivity.topActivity.startActivity(intent);
	}

	private static String _userAgent;
	public static String getUserAgent() {
		if(_userAgent == null){
			try {
				_userAgent =  new WebView(HFActivity.topActivity).getSettings().getUserAgentString();
			} catch (Exception e) {
			}
			if(_userAgent == null){
				_userAgent = "Android";
			}
		}
		return _userAgent;
	}
}
