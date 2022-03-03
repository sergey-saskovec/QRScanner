package com.codescannerqr.generator.helpers;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationSoundHelpers {

    public static void onVibration(Activity activity) {
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE)
            );
        } else {
            v.vibrate(500);
        }
    }

    public static void onSound(Activity activity) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(
                    activity.getApplicationContext(),
                    notification
            );
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
