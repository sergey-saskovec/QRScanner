package com.codescannerqr.generator.helpers;

import android.app.Activity;

import com.codescannerqr.generator.Config;

public class SettingsHelpers {

    public static void setSettings(Activity activity){
        SharedPreferenceManager manager = SharedPreferenceManager.getInstance(activity);
        Config.settingsVibration = manager.getBoolean("settingsVibration", false);
        Config.settingsSound = manager.getBoolean("settingsSound", false);
        Config.settingsAutoCopy = manager.getBoolean("settingsAutoCopy", false);
        Config.settingsAutoSearch = manager.getBoolean("settingsAutoSearch", false);
        Config.settingsSaveHistory = manager.getBoolean("settingsSaveHistory", true);
    }

    public static void saveSettings(Activity activity){
        SharedPreferenceManager manager = SharedPreferenceManager.getInstance(activity);
        manager.putBoolean("settingsVibration", Config.settingsVibration);
        manager.putBoolean("settingsSound", Config.settingsSound);
        manager.putBoolean("settingsAutoCopy", Config.settingsAutoCopy);
        manager.putBoolean("settingsAutoSearch", Config.settingsAutoSearch);
        manager.putBoolean("settingsSaveHistory", Config.settingsSaveHistory);
    }

    public static boolean getFirstGeneratedQR(Activity activity){
        SharedPreferenceManager manager = SharedPreferenceManager.getInstance(activity);
        return manager.getBoolean("firstCreateQRCode", true);
    }

    public static void saveFirstGeneratedQR(Activity activity){
        SharedPreferenceManager manager = SharedPreferenceManager.getInstance(activity);
        manager.putBoolean("firstCreateQRCode", false);
    }


}
