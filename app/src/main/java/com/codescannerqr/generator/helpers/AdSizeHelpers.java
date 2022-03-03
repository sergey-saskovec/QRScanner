package com.codescannerqr.generator.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.WindowManager;
import com.google.android.gms.ads.AdSize;

public class AdSizeHelpers {

    public static AdSize adSizeCustom(Activity activity){
        float a;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            a = windowManager.getCurrentWindowMetrics().getBounds().width();
        }
        else{
            a = Resources.getSystem().getDisplayMetrics().widthPixels;
        }

        float aD = activity.getResources().getDisplayMetrics().density;
        int adWidth2 = (int) (a / aD);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth2);
    }
}
