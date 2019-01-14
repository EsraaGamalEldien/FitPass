package com.example.esraa.fitpass.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.esraa.fitpass.ApplicationClass;

public class SharedPreferenceManager {
    private static final SharedPreferenceManager ourInstance = new SharedPreferenceManager();
    protected Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences appSharedPrefs;

    private SharedPreferenceManager() {
        context = ApplicationClass.getContext();
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = appSharedPrefs.edit();
    }

    public static SharedPreferenceManager getInstance() {
        return ourInstance;
    }

    public String getString(String key) {
        return appSharedPrefs.getString(key, null);
    }

    public void saveString(String key, String value) {
        editor.putString(key, value).commit();
    }

}
