package com.template.function;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePre
 */
public class SharePreferenceUntil {

    private SharedPreferences share;
    private SharedPreferences.Editor editor;

    public SharePreferenceUntil(Context context) {
        share = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        editor = share.edit();
    }

    public void setInteger(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getInteger(String key) {
        return share.getInt(key, 0);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return share.getBoolean(key, false);
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return share.getString(key, null);
    }
}
