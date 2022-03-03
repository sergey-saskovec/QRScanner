package com.codescannerqr.generator.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;

public class MyClipboardManager {

    public static void saveToClipboard(String stringSave, Activity activity){
        ClipboardManager clipboard = (ClipboardManager) activity.
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", stringSave);
        clipboard.setPrimaryClip(clip);
    }

    public static String readFromClipboard(Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            return clipboard.getPrimaryClip().toString();
        } else {
            try {
                ClipboardManager clipboard =
                        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = clipboard.getPrimaryClip();
                if (clip != null) {
                    String text;
                    ClipData.Item item = clip.getItemAt(0);
                    text = coerceToText(context, item).toString();
                    return text;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static CharSequence coerceToText(Context context, ClipData.Item item) {
        CharSequence text = item.getText();
        if (text != null) {
            return text;
        }

        Uri uri = item.getUri();
        if (uri != null) {

            FileInputStream stream = null;
            try {
                AssetFileDescriptor descr = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                stream = descr.createInputStream();
                InputStreamReader reader = new InputStreamReader(stream,
                        StandardCharsets.UTF_8);

                StringBuilder builder = new StringBuilder(128);
                char[] buffer = new char[8192];
                int len;
                while ((len = reader.read(buffer)) > 0) {
                    builder.append(buffer, 0, len);
                }
                return builder.toString();

            } catch (FileNotFoundException ignored) {} catch (IOException e) {
                Log.w("ClippedData", "Failure loading text", e);
                return e.toString();

            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            return uri.toString();
        }
        Intent intent = item.getIntent();
        if (intent != null) {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        }
        return "";
    }

}
