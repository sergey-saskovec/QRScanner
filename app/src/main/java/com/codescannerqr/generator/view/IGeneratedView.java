package com.codescannerqr.generator.view;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.zxing.client.result.ParsedResult;
import com.codescannerqr.generator.model.Contact;
import com.codescannerqr.generator.model.ResultList;

import java.util.List;

public interface IGeneratedView {

    default void resultFullContact(Contact contact){}

    default void resultBitmapQR(Bitmap bitmap, String str){}

    default void resultBitmapQR128(Bitmap bitmap){}

    default void resultOverlayBitmap(Bitmap bitmap){}

    default void resultBorderBitmap(Bitmap bitmap){}

    default void resultGradientBitmap(Bitmap bitmap){}

    default void resultUriBitmapShare(Uri uri){}

    default void resultSingleColorBitmap(Bitmap bitmap){}

    default void resultParsedQRCode(String nameFragment, String defaultText,
                                    int iconFragment, List<ResultList> resultLists,
                                    String args, ParsedResult parsedResult){}

    default void resultQRCodeParsedResult(ParsedResult parserdResult, String args){}

    default void resultExportHistory(String result){}

    default void resultInAppPurchase(String result){}

    default void resultPrice(String result, String code){}

    default void resultPriceCode2(String result, String code){}

    default void resultRestorePurchase(String result){}
}
