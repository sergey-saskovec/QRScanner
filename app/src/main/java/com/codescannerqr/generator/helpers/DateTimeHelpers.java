package com.codescannerqr.generator.helpers;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelpers {

    public static String getDateTimeNow(String format){
        SimpleDateFormat sdfDate = new SimpleDateFormat(format, Locale.getDefault());
        return sdfDate.format(new Date());
    }

    public static String getDateTimeNow(String format, long date){
        SimpleDateFormat sdfDate = new SimpleDateFormat(format, Locale.getDefault());
        return sdfDate.format(date);
    }

    public static long convertDateToLong(String dateStr, String format){
        long milliseconds = 0;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fTime = new SimpleDateFormat(format);
        try {
            Date dTime = fTime.parse(dateStr);
            assert dTime != null;
            milliseconds = dTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}
