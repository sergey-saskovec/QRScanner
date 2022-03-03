package com.codescannerqr.generator.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.helpers.SharedPreferenceManager;

import org.jetbrains.annotations.NotNull;

public class InterstitialManager {

    private static InterstitialAd sInterstitialAd;
    private static boolean loadedBool = false;
    private static SharedPreferenceManager sharedPreferenceManager;

    public InterstitialManager(Activity activity) {
        loadedBool = false;
        sInterstitialAd = loadAdInter(activity);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(activity);
    }

    public static boolean getLoaded(){
        return loadedBool;
    }

    public static InterstitialAd loadAdInter(Activity activity){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, Config.interID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        sInterstitialAd = interstitialAd;
                        loadedBool = true;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        loadedBool = false;
                        sInterstitialAd = null;

                    }
                });
        return sInterstitialAd;
    }

    public static void showInter(Activity activity, InterDismiss interDismiss){
        int iCount = 0;
        if (sharedPreferenceManager.getInt("countInter",0) == 0){
            iCount = 0;
        }
        else {
            iCount = sharedPreferenceManager.getInt("countInter",0);
        }
        sharedPreferenceManager.putInt("countInter", iCount+1);

        sInterstitialAd.show(activity);

        int finalICount = iCount;
        sInterstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        sInterstitialAd = null;
                        sInterstitialAd = loadAdInter(activity);
                        interDismiss.interDismiss(finalICount);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                        sInterstitialAd = null;
                        loadedBool = false;
                        interDismiss.interDismiss(finalICount);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {}
                });
    }
}
