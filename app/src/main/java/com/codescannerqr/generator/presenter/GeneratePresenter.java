package com.codescannerqr.generator.presenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.codescannerqr.generator.Config;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.helpers.DateTimeHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.SingleMediaScanner;
import com.codescannerqr.generator.model.Contact;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.model.ResultList;
import com.codescannerqr.generator.view.IGeneratedView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class GeneratePresenter implements IGeneratePresenter {

    private final IGeneratedView generatedView;
    private BillingClient billingClient;
    private SkuDetails skuDetails;

    public GeneratePresenter(IGeneratedView generatedView) {
        this.generatedView = generatedView;
    }

    @Override
    public void getAddressTest(Activity activity, Uri uri) {
        Contact contact = new Contact();
        String[] projection = {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA2, ContactsContract.Data.DATA3};

        String selection = ContactsContract.Data.MIMETYPE + " IN ('" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "', '" +
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "')";
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(uri, projection, selection, null, null);

        while (cur != null && cur.moveToNext()) {
            long id = cur.getLong(0);
            contact.setId(String.valueOf(id));
            String name = cur.getString(1);
            contact.setName(name);
            String mime = cur.getString(2);
            String data = cur.getString(3);
            contact.setPhone(data);
            String data2 = getCompanyName(id, activity);
            contact.setOrg(data2);
            String data3 = getAddress(id, activity);
            contact.setAddress(data3);
            String data4 = getEmail(id, activity);
            contact.setEmail(data4);

            switch (mime) {
                case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                case ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE:
                    break;
            }
            generatedView.resultFullContact(contact);
        }

        Objects.requireNonNull(cur).close();
    }

    private String getCompanyName(long rawContactId, Activity activity) {
        try {
            String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{String.valueOf(rawContactId),
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor cursor = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);

            if (cursor == null) return null;
            String name = null;
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Organization.COMPANY
                        )
                );
            }
            cursor.close();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private String getAddress(long rawContactId, Activity activity) {
        try {
            String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{String.valueOf(rawContactId),
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            Cursor cursor = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);

            if (cursor == null) return null;
            String address = "";
            if (cursor.moveToFirst()) {
                String country = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)
                );
                String city = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY)
                );
                String region = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION)
                );
                String street = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET)
                );
                String postcode = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE)
                );
                if (!country.isEmpty()) {
                    address += country;
                } else if (!city.isEmpty()) {
                    address += city;
                } else if (!region.isEmpty()) {
                    address += region;
                } else if (!street.isEmpty()) {
                    address += street;
                } else if (!postcode.isEmpty()) {
                    address += postcode;
                }
            }
            cursor.close();
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private String getEmail(long rawContactId, Activity activity) {
        try {
            String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{String.valueOf(rawContactId),
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
            Cursor cursor = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);

            if (cursor == null) return null;
            String emailAddress = "";
            if (cursor.moveToFirst()) {
                emailAddress = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.ADDRESS)
                );
            }
            cursor.close();
            return emailAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    @Override
    public void getBitmapQR(String str, int width, int height, int color) throws WriterException {
        BitMatrix result = null;
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, height, hints);
        } catch (IllegalArgumentException ignored) {

        }
        int w = Objects.requireNonNull(result).getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? color : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        if (color == BLACK) {
            generatedView.resultBitmapQR(bitmap, str);
        } else {
            generatedView.resultSingleColorBitmap(bitmap);
        }

    }

    @Override
    public void getBitmapQR128(String str, int width, int height, int color) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(str);

        BitMatrix bm = writer.encode(finalData, BarcodeFormat.EAN_13, width, 1);
        int bmWidth = bm.getWidth();

        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bmWidth; i++) {
            int[] column = new int[height];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }

        generatedView.resultBitmapQR128(imageBitmap);
    }

    //////////////OVERLAY BITMAP//////////////
    @Override
    public void overlayBitmap(Bitmap bitmapQrCode, Context context, int drawable) {
        Bitmap yourLogo = getBitmap(context, drawable);
        Bitmap merge = overlay(bitmapQrCode, yourLogo);
        generatedView.resultOverlayBitmap(merge);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(50,
                50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap result = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(result);
        int widthBack = bmp1.getWidth();
        int widthFront = bmp2.getWidth();
        int move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(bmp1, 0f, 0f, null);
        canvas.drawBitmap(bmp2, move, move, null);
        return result;
    }

    //////////////END OVERLAY BITMAP//////////////


    ////////////BORDER BITMAP//////////////////////

    @Override
    public void setBorderBitmap(Bitmap bitmapQRCode, int color1, int color2, Activity activity) {
        int borderSize = 20;
        Bitmap bmpWithBorder = Bitmap.createBitmap(bitmapQRCode.getWidth() + borderSize * 2,
                bitmapQRCode.getHeight() + borderSize * 2, bitmapQRCode.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(
                        0,
                        0,
                        0,
                        bitmapQRCode.getHeight(),
                        color1,
                        color2,
                        Shader.TileMode.MIRROR
                )
        );
        canvas.drawBitmap(bitmapQRCode, 20, 20, null);
        final Rect rect = new Rect(20, 20, bitmapQRCode.getWidth() + 20,
                bitmapQRCode.getHeight() + 20);
        final RectF rectF = new RectF(rect);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 30);
        canvas.drawRoundRect(rectF, 20, 20, paint);
        generatedView.resultBorderBitmap(bmpWithBorder);
    }


    ////////////END BORDER BITMAP//////////////////////


    ///////////CREATE GRADIENT QR CODE/////////////////

    @Override
    public void setGradient(Bitmap bitmapQR, int color1, int color2, int defColor) {
        int w = bitmapQR.getWidth();
        int h = bitmapQR.getHeight();
        Bitmap bitmapGradient = makeRadGrad(color1, color2);

        int[] pixelGradient = getBitmapPixels(bitmapGradient, 0, 0,
                bitmapGradient.getWidth(), bitmapGradient.getHeight());
        int[] pixelQR = getBitmapPixels(bitmapQR, 0, 0,
                bitmapQR.getWidth(), bitmapQR.getHeight());
        int[] pixelQrNew = new int[bitmapQR.getWidth() * bitmapQR.getHeight()];
        for (int i = 0; i < pixelQR.length; i++) {
            if (pixelQR[i] == defColor) {
                pixelQrNew[i] = pixelGradient[i];
            } else {
                pixelQrNew[i] = pixelQR[i];
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixelQrNew, 0, w, 0, 0, w, h);
        generatedView.resultGradientBitmap(bitmap);
    }

    public static int[] getBitmapPixels(Bitmap bitmap, int x, int y, int width, int height) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), x, y,
                width, height);
        final int[] subsetPixels = new int[width * height];
        for (int row = 0; row < height; row++) {
            System.arraycopy(pixels, (row * bitmap.getWidth()),
                    subsetPixels, row * width, width);
        }
        return subsetPixels;
    }

    public Bitmap makeRadGrad(int color1, int color2) {
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, 400,
                color1, color2, Shader.TileMode.CLAMP);

        Paint p = new Paint();
        p.setDither(true);
        p.setShader(linearGradient);

        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawPaint(p);

        return bitmap;
    }

    ///////////END CREATE GRADIENT QR CODE/////////////////

    /////////////////SHARE BITMAP QR CODE////////////////////////

    @Override
    public void shareBitmap(Bitmap bitmap, Activity activity) {
        File imagesFolder = new File(activity.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(activity,
                    activity.getApplicationContext().getPackageName(), file);

        } catch (IOException e) {
            Log.d("TAG", "IOException while trying to write file for sharing: " +
                    e.getMessage());
        }
        generatedView.resultUriBitmapShare(uri);
    }

    /////////////////SHARE BITMAP QR CODE////////////////////////


    private void saveImage2(Bitmap bitmap, @NonNull String name, Activity activity) throws IOException {
        OutputStream fos;

        String IMAGES_FOLDER_NAME = "QRCODES";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
            );
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "CAMERA";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);
            new SingleMediaScanner(activity, image);
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        fos.flush();
        fos.close();
    }

    @Override
    public void savedImage(Bitmap bitmap, String name, Activity activity) {
        try {
            saveImage2(bitmap, name, activity);
            Toast.makeText(activity, activity.getString(R.string.saved_image), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parserdResultQRCode(ParsedResult parserdResult, String args, Activity activity) {
        List<ResultList> resultLists = new ArrayList<>();
        String nameFragment = "";
        String defaultText = "";
        if (args == null) {
            args = parserdResult.getDisplayResult();
        }
        int iconFragment = R.drawable.ic_back_generate;
        switch (parserdResult.getType()) {
            case ADDRESSBOOK:
                AddressBookParsedResult addressResult = (AddressBookParsedResult) parserdResult;
                if (addressResult.getOrg() == null) {
                    nameFragment = activity.getString(R.string.contact);
                    iconFragment = R.drawable.ic_gen_contact;
                    resultLists.add(new ResultList(activity.getString(R.string.name), addressResult.getNames()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.tel), addressResult.getPhoneNumbers()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.email), addressResult.getEmails()[0]));
                } else {
                    nameFragment = activity.getString(R.string.my_card);
                    iconFragment = R.drawable.ic_gen_mycard;
                    resultLists.add(new ResultList(activity.getString(R.string.name), addressResult.getNames()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.tel), addressResult.getPhoneNumbers()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.email), addressResult.getEmails()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.address), addressResult.getAddresses()[0]));
                    resultLists.add(new ResultList(activity.getString(R.string.org), addressResult.getOrg()));
                }
                break;
            case EMAIL_ADDRESS:
                nameFragment = activity.getString(R.string.email);
                iconFragment = R.drawable.ic_gen_email;
                EmailAddressParsedResult email = (EmailAddressParsedResult) parserdResult;

                if (email.getTos().length > 0)
                    resultLists.add(new ResultList(activity.getString(R.string.email), email.getTos()[0]));
                resultLists.add(new ResultList(activity.getString(R.string.subject), email.getSubject()));
                resultLists.add(new ResultList(activity.getString(R.string.message), email.getBody()));
                break;
            case PRODUCT:
                nameFragment = activity.getString(R.string.product_code);
                iconFragment = R.drawable.ic_gen_product_code;
                break;
            case URI:

                if (parserdResult.getDisplayResult().contains("fb://")) {
                    nameFragment = activity.getString(R.string.facebook);
                    iconFragment = R.drawable.ic_gen_facebook;
                } else if (parserdResult.getDisplayResult().contains("youtube.com")) {
                    nameFragment = activity.getString(R.string.youtube);
                    iconFragment = R.drawable.ic_gen_youtube;
                } else if (parserdResult.getDisplayResult().contains("whatsapp://")) {
                    nameFragment = activity.getString(R.string.whatsapp);
                    iconFragment = R.drawable.ic_gen_whatsapp;
                } else if (parserdResult.getDisplayResult().contains("paypal.me")) {
                    nameFragment = activity.getString(R.string.paypal);
                    iconFragment = R.drawable.ic_gen_paypal;
                } else if (parserdResult.getDisplayResult().contains("twitter://") ||
                        parserdResult.getDisplayResult().contains("twitter.com")) {
                    nameFragment = activity.getString(R.string.twitter);
                    iconFragment = R.drawable.ic_gen_twitter;
                } else if (parserdResult.getDisplayResult().contains("instagram://") ||
                        parserdResult.getDisplayResult().contains("instagram.com")) {
                    nameFragment = activity.getString(R.string.instagram);
                    iconFragment = R.drawable.ic_gen_instagram;
                } else if (parserdResult.getDisplayResult().contains("spotify:")) {
                    nameFragment = activity.getString(R.string.spotify);
                    iconFragment = R.drawable.ic_gen_spotify;
                } else if (parserdResult.getDisplayResult().contains("tiktok.com")) {
                    nameFragment = activity.getString(R.string.tiktok);
                    iconFragment = R.drawable.ic_gen_tiktok;
                } else if (parserdResult.getDisplayResult().contains("viber")) {
                    nameFragment = activity.getString(R.string.viber);
                    iconFragment = R.drawable.ic_gen_viber;
                } else {
                    nameFragment = activity.getString(R.string.website);
                    iconFragment = R.drawable.ic_gen_website;
                }
                defaultText = parserdResult.getDisplayResult();

                break;
            case GEO:
                nameFragment = activity.getString(R.string.geo);
                iconFragment = R.drawable.ic_gen_geo;
                GeoParsedResult geo = (GeoParsedResult) parserdResult;
                resultLists.add(new ResultList(activity.getString(R.string.latitude), String.valueOf(geo.getLatitude())));
                resultLists.add(new ResultList(activity.getString(R.string.longitude), String.valueOf(geo.getLongitude())));
                break;
            case TEL:
                nameFragment = activity.getString(R.string.cellphone);
                iconFragment = R.drawable.ic_gen_cellphone;
                TelParsedResult tel = (TelParsedResult) parserdResult;
                resultLists.add(new ResultList(activity.getString(R.string.tel), tel.getNumber()));
                break;
            case SMS:
                nameFragment = activity.getString(R.string.sms);
                iconFragment = R.drawable.ic_gen_sms;
                SMSParsedResult sms = (SMSParsedResult) parserdResult;
                resultLists.add(new ResultList(activity.getString(R.string.tel), sms.getNumbers()[0]));
                resultLists.add(new ResultList(activity.getString(R.string.subject), sms.getSubject()));
                resultLists.add(new ResultList(activity.getString(R.string.message), sms.getBody()));
                break;
            case CALENDAR:
                nameFragment = activity.getString(R.string.calendar);
                iconFragment = R.drawable.ic_gen_calendar;
                CalendarParsedResult calendarRes = (CalendarParsedResult) parserdResult;

                resultLists.add(new ResultList(activity.getString(R.string.event_title), calendarRes.getSummary()));
                resultLists.add(new ResultList(activity.getString(R.string.description), calendarRes.getDescription()));
                resultLists.add(new ResultList(activity.getString(R.string.address), calendarRes.getLocation()));
                resultLists.add(new ResultList(
                        activity.getString(R.string.start_time),
                        dateFormat(calendarRes.getStartTimestamp()))
                );
                resultLists.add(new ResultList(activity.getString(R.string.end_time), dateFormat(calendarRes.getEndTimestamp())));
                break;
            case WIFI:
                nameFragment = activity.getString(R.string.wifi);
                iconFragment = R.drawable.ic_gen_wifi;
                WifiParsedResult wifi = (WifiParsedResult) parserdResult;
                resultLists.add(new ResultList(activity.getString(R.string.ssid), wifi.getSsid()));
                resultLists.add(new ResultList(activity.getString(R.string.security), wifi.getNetworkEncryption()));
                resultLists.add(new ResultList(activity.getString(R.string.password), wifi.getPassword()));
                break;
            case TEXT:
                nameFragment = activity.getString(R.string.text);
                iconFragment = R.drawable.ic_gen_text;
                defaultText = parserdResult.getDisplayResult();
                break;
            default:
                break;
        }

        generatedView.resultParsedQRCode(
                nameFragment,
                defaultText,
                iconFragment,
                resultLists,
                args,
                parserdResult
        );

    }

    private String dateFormat(long dateCurrent) {

        return DateTimeHelpers.getDateTimeNow("HH:mm", dateCurrent) +
                " " +
                DateTimeHelpers.getDateTimeNow("dd.MM.yyyy", dateCurrent);

    }

    @Override
    public void requestPermissionsScanner(ActivityResult result, Activity activity) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data == null || data.getData() == null) {
                Log.e("TAG", "The uri is null, probably the user cancelled the image " +
                        "selection process using the back button.");
                return;
            }
            Uri uri = data.getData();
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if (bitmap == null) {
                    Log.e("TAG", "uri is not a bitmap," + uri.toString());
                    return;
                }
                int width = bitmap.getWidth(), height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                bitmap.recycle();
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                try {
                    Result resultRes = reader.decode(bBitmap);
                    ParsedResult parserdResult = ResultParser.parseResult(resultRes);
                    generatedView.resultQRCodeParsedResult(parserdResult, resultRes.getText());
                } catch (NotFoundException e) {
                    Log.e("TAG", "decode exception", e);
                    Toast.makeText(activity, activity.getString(R.string.no_codes_found), Toast.LENGTH_LONG).show();
                }
            } catch (FileNotFoundException e) {
                Log.e("TAG", "can not open file" + uri.toString(), e);
                Toast.makeText(activity, activity.getString(R.string.can_not_open), Toast.LENGTH_LONG).show();

            }
        }
    }


    @Override
    public void saveCSVFile(Activity activity, Uri uri) {
        String result;
        OutputStream outputStream;
        try {
            outputStream = activity.getContentResolver().openOutputStream(uri);

            List<String[]> dataCSV = exportCSVFile(activity);
            for (int i = 0; i < dataCSV.size(); i++) {
                for (int j = 0; j < dataCSV.get(i).length; j++) {
                    String field = "\"" + dataCSV.get(i)[j] + "\"";
                    if (j == 2) {
                        outputStream.write(field.getBytes());
                    } else {
                        outputStream.write((field + ",").getBytes());
                    }

                }
                outputStream.write(("\n").getBytes());

            }
            outputStream.close();
            result = activity.getString(R.string.export_history);
        } catch (IOException e) {
            result = e.getMessage();
            e.printStackTrace();
        }

        generatedView.resultExportHistory(result);
    }

    private List<String[]> exportCSVFile(Activity activity) {
        List<HistoryItem> listScan = ListHistoryHelpers.getListHistoryCreate(
                activity,
                "list_scan"
        );
        List<HistoryItem> listCreate = ListHistoryHelpers.getListHistoryCreate(
                activity,
                "list_create"
        );
        List<HistoryItem> listBookmark = ListHistoryHelpers.getListHistoryCreate(
                activity,
                "list_bookmark"
        );

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"type", "code", "bookmark"});
        for (int i = 0; i < listScan.size(); i++) {
            data.add(new String[]{
                            "scan",
                            listScan.get(i).getUrlHistory(),
                            checkBookmark(listScan.get(i), listBookmark)
                    }
            );
        }
        for (int i = 0; i < listCreate.size(); i++) {
            data.add(new String[]{
                    "create",
                    listCreate.get(i).getUrlHistory(),
                    checkBookmark(listCreate.get(i), listBookmark)
            }
            );
        }

        return data;


    }

    private String checkBookmark(HistoryItem historyItem, List<HistoryItem> listBookmark) {
        String stringRet = "no";
        for (int i = 0; i < listBookmark.size(); i++) {
            if (historyItem.equals(listBookmark.get(i))) {
                stringRet = "yes";
                break;
            } else {
                stringRet = "no";
            }
        }
        return stringRet;
    }

    @Override
    public void initBillingClient(Activity activity, String productID, String nameMethod) {

        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NotNull BillingResult billingResult,
                                           List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    for (Purchase purchase : purchases) {
                        handlePurchase(purchase, activity);

                    }
                }
            }
        };

        billingClient = BillingClient.newBuilder(activity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                if (nameMethod.equals("get_price")) {
                    getPriceOrBuy(Config.productID1, billingClient, nameMethod, activity);
                    getPriceOrBuy(Config.productID2, billingClient, nameMethod, activity);
                } else if (nameMethod.equals("get_price_1")) {
                    getPriceOrBuy(Config.productID1, billingClient, nameMethod, activity);
                } else if (nameMethod.equals("restore")) {
                    billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS,
                            (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    if (!list.isEmpty()) {
                                        Config.premium = true;
                                        generatedView.resultRestorePurchase("true");
                                    }
                                } else {
                                    generatedView.resultRestorePurchase(billingResult1.getDebugMessage());
                                }
                            });
                } else {
                    getPriceOrBuy(productID, billingClient, nameMethod, activity);
                }

            }
        });
    }

    public void inAppPurchase(Activity activity, BillingClient billingClient,
                              SkuDetails skuDetails) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
        int responseCode = billingResult.getResponseCode();
        String message = billingResult.getDebugMessage();
        if (responseCode == BillingClient.BillingResponseCode.OK) {//generatedView.resultInAppPurchase("true");
        } else {
            generatedView.resultInAppPurchase(message);
        }
    }


    public void getPriceOrBuy(String productID, BillingClient billingClient, String nameMethod,
                              Activity activity) {
        List<String> skuList = new ArrayList<>();
        skuList.add(productID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                (billingResult, list) -> {
                    if (nameMethod.equals("get_price")) {
                        if (productID.equals(Config.productID1)) {
                            try {
                                assert list != null;
                                String cur = list.get(0).getPrice();
                                generatedView.resultPrice(cur, productID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                assert list != null;
                                String cur = list.get(0).getPrice();
                                generatedView.resultPriceCode2(cur, productID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (nameMethod.equals("get_price_1")) {
                        if (productID.equals(Config.productID1)) {
                            try {
                                assert list != null;
                                String cur = list.get(0).getPrice();
                                generatedView.resultPrice(cur, productID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        assert list != null;
                        skuDetails = list.get(0);
                        inAppPurchase(activity, billingClient, Objects.requireNonNull(list).get(0));

                    }


                });
    }

    void handlePurchase(Purchase purchase, Activity activity) {
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //Apphud.syncPurchases();
                generatedView.resultInAppPurchase("true");
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }
}
