package com.codescannerqr.generator.helpers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.codescannerqr.generator.R;

public class IntentHelpers {

    public static void shareIntent(Activity activity, String stringData){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(stringData));
        try {
            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.no_activity_intent),Toast.LENGTH_SHORT).show();
        }
    }
}
