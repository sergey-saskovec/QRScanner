package com.codescannerqr.generator.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.activity.result.ActivityResult;

import com.google.zxing.WriterException;
import com.google.zxing.client.result.ParsedResult;

public interface IGeneratePresenter {

    default void getAddressTest(Activity activity, Uri uri) {}

    default void getBitmapQR(String str, int width, int height, int color)
            throws WriterException {}

    default void getBitmapQR128(String str, int width, int height, int color)
            throws WriterException {}

    default void overlayBitmap(Bitmap bitmapQrCode, Context context, int drawable) {}

    default void setBorderBitmap(Bitmap bitmapQRCode, int color1, int color2, Activity activity) {}

    default void setGradient(Bitmap bitmapQR, int color1, int color2, int defColor) {}

    default void shareBitmap(Bitmap bitmap, Activity activity) {}

    default void savedImage(Bitmap bitmap, String name, Activity activity) {}

    default void parserdResultQRCode(ParsedResult parserdResult, String args, Activity activity) {}

    default void requestPermissionsScanner(ActivityResult result, Activity activity) {}

    default void saveCSVFile(Activity activity, Uri uri) {}

    default void initBillingClient(Activity activity, String productID, String nameMethod){}

}
