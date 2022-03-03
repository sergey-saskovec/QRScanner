package com.codescannerqr.generator.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.core.app.ShareCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import java.io.ByteArrayOutputStream;

public class BitmapHelpers {

    public static byte[] encodeBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap decodeByteToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static ParsedResult getParsedResult2(Bitmap bitmap){
        ParsedResult parserdResult = null;
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try
        {
            Result result = reader.decode(bBitmap);
            parserdResult = ResultParser.parseResult(result);
        }
        catch (NotFoundException e)
        {
            Log.e("TAG", "decode exception", e);
        }
        return parserdResult;

    }

    public static void shareBitmap(Uri uri, Activity activity){
        ShareCompat.IntentBuilder intentBuilder = new ShareCompat.IntentBuilder(activity);
        intentBuilder.setType("image/png")
                .setStream(uri)
                .setChooserTitle("chooserTitle")
                .startChooser();
    }
}
