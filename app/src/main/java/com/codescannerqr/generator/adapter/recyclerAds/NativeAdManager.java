package com.codescannerqr.generator.adapter.recyclerAds;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.codescannerqr.generator.R;

import org.jetbrains.annotations.NotNull;

public class NativeAdManager {

    public static boolean aBoolean = false;

    public static void loadNative(String nativeID, Activity activity, LinearLayout adContainer){
        ShimmerFrameLayout shimmerFrameLayout = adContainer.findViewById(R.id.shimmerLayout);
        if (aBoolean){

            shimmerFrameLayout.startShimmer();
            TemplateView templatesmall=adContainer.findViewById(R.id.my_templatesmall);
            final boolean[] loaded = {false};

            AdLoader adLoader = new AdLoader.Builder(activity, nativeID)
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle.Builder builder=new NativeTemplateStyle.Builder();
                        builder.withCallToActionTextSize(11f);

                        templatesmall.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        templatesmall.setStyles(builder.build());
                        templatesmall.setNativeAd(nativeAd);
                        loaded[0] = true;


                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.e("admobnative","error:"+loadAdError.toString());
                            adContainer.setVisibility(View.GONE);
                        }
                        /*@Override
                        public void onAdFailedToLoad(int errorCode) {
                            Log.e("admobnative","error:"+errorCode);
                            adHolder.adContainer.setVisibility(View.GONE);
                            // Handle the failure by logging, altering the UI, and so on.

                        }*/
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
        else{
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }
}
