package com.huanfeng.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.huanfeng.view.HFActivity;

import java.util.Set;

/**
 * Created by YXL on 2015/12/12.
 */
public class Prefs {
    private static SharedPreferences sp = HFActivity.topActivity.getSharedPreferences("huanfeng_prefs", Context.MODE_PRIVATE);

    public static int getInt(String key){
        return getInt(key, 0);
    }
    public static int getInt(String key,int defaultValue){
        return sp.getInt(key, defaultValue);
    }
    public static void setInt(String key,int value){
        sp.edit().putInt(key,value).commit();
    }

    public static long getLong(String key){
        return getInt(key, 0);
    }
    public static long getLong(String key,long defaultValue){
        return sp.getLong(key, defaultValue);
    }
    public static void setLong(String key,long value){
        sp.edit().putLong(key, value).commit();
    }

    public static float getFloat(String key){
        return getFloat(key, 0);
    }
    public static float getFloat(String key,float defaultValue){
        return sp.getFloat(key, defaultValue);
    }
    public static void setFloat(String key,float value){
        sp.edit().putFloat(key, value).commit();
    }

    public static String getString(String key){
        return getString(key, null);
    }
    public static String getString(String key,String defaultValue){
        return sp.getString(key, defaultValue);
    }
    public static void setString(String key,String value){
        sp.edit().putString(key, value).commit();
    }

    public static Set<String> getStringSet(String key){
        return getStringSet(key);
    }

    public static void setStringSet(String key,Set<String> value){
        sp.edit().putStringSet(key, value).commit();
    }

    public static boolean getBoolean(String key){
        return getBoolean(key,false);
    }
    public static boolean getBoolean(String key,boolean defaultValue){
        return sp.getBoolean(key, defaultValue);
    }
    public static void setBoolean(String key,boolean value){
        sp.edit().putBoolean(key, value).commit();
    }

    public static void remove(String key){
        sp.edit().remove(key).commit();
    }

    public static void clear(){
        sp.edit().clear().commit();
    }
}
