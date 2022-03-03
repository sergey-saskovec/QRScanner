package com.codescannerqr.generator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SharedPreferenceManager {

    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;

    private SharedPreferenceManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        return new SharedPreferenceManager(context.getApplicationContext());
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value).apply();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }
}
