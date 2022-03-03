package com.codescannerqr.generator.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.model.InAppNewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.model.ResultButtons;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListHistoryHelpers {

    public static List<HistoryItem> getListHistoryCreate(Activity activity, String key) {
        //key = list_create||list_scan||list_bookmark
        List<HistoryItem> historyItemList;
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(activity);
        if (sharedPreferenceManager.getString(key, "").equals("")){
            historyItemList = new ArrayList<>();
        }
        else{
            Gson gson = new Gson();
            String json = sharedPreferenceManager.getString(key, "");
            Type type = new TypeToken<ArrayList<HistoryItem>>() {}.getType();
            historyItemList = gson.fromJson(json, type);
        }
        return historyItemList;
    }

    public static void setListHistoryCreateAddItem(Activity activity, String title, String arg,
                                                   int icon, Bitmap bitmap, String key) {
        //key = list_create||list_scan||list_bookmark
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(activity);
        List<HistoryItem> historyItemList = getListHistoryCreate(activity, key);

        HistoryItem historyItem = new HistoryItem(
                title,
                arg,
                icon,
                BitmapHelpers.encodeBitmapToByte(bitmap));

        historyItemList.add(historyItem);

        Gson gson = new Gson();
        String json = gson.toJson(historyItemList);
        sharedPreferenceManager.putString(key, json);

    }

    public static void setListHistoryCreateAddList(Activity activity, List<HistoryItem> list,
                                                   String key) {
        //key = list_create||list_scan||list_bookmark
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(activity);

        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferenceManager.putString(key, json);

    }

    public static List<HistoryItem> reverseList(List<HistoryItem> defaultList){
        List<HistoryItem> reverseLists = new ArrayList<>();
        for (int i = defaultList.size()-1; i >= 0; i--){
            reverseLists.add(defaultList.get(i));
        }
        return reverseLists;
    }

    public static List<ResultButtons> createListButtonsResult(String nameFragment, Activity activity ){
        List<ResultButtons> list = new ArrayList<>();
        if (nameFragment.equals(activity.getString(R.string.website)) ||
                nameFragment.equals(activity.getString(R.string.product_code))) {
            list.add(new ResultButtons(
                    activity.getString(R.string.search),
                    R.drawable.ic_result_search
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.wifi))){
            list.add(new ResultButtons(
                    activity.getString(R.string.connect),
                    R.drawable.ic_result_connect
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.text))){
            list.add(new ResultButtons(
                    activity.getString(R.string.search),
                    R.drawable.ic_result_search
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.sms),
                    R.drawable.ic_result_sms
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.email),
                    R.drawable.ic_result_email
            ));

            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));

            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));

        }
        else if (nameFragment.equals(activity.getString(R.string.contact))){
            list.add(new ResultButtons(
                    activity.getString(R.string.add),
                    R.drawable.ic_result_add
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.email),
                    R.drawable.ic_result_email
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));

            list.add(new ResultButtons(
                    activity.getString(R.string.dial),
                    R.drawable.ic_result_dial
            ));

        }
        else if (nameFragment.equals(activity.getString(R.string.cellphone))){
            list.add(new ResultButtons(
                    activity.getString(R.string.dial),
                    R.drawable.ic_result_dial
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.add),
                    R.drawable.ic_result_add
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.email))){
            list.add(new ResultButtons(
                    activity.getString(R.string.email),
                    R.drawable.ic_result_email
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.add),
                    R.drawable.ic_result_add
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.sms))){
            list.add(new ResultButtons(
                    activity.getString(R.string.sms),
                    R.drawable.ic_result_sms
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.my_card))){
            list.add(new ResultButtons(
                    activity.getString(R.string.add),
                    R.drawable.ic_result_add
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.email),
                    R.drawable.ic_result_email
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.dial),
                    R.drawable.ic_result_dial
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));

        }

        else if (nameFragment.equals(activity.getString(R.string.geo))){
            list.add(new ResultButtons(
                    activity.getString(R.string.maps),
                    R.drawable.ic_result_maps
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if (nameFragment.equals(activity.getString(R.string.calendar))){
            list.add(new ResultButtons(
                    activity.getString(R.string.add),
                    R.drawable.ic_result_add
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }
        else if(nameFragment.equals(activity.getString(R.string.facebook)) ||
                nameFragment.equals(activity.getString(R.string.youtube)) ||
                nameFragment.equals(activity.getString(R.string.whatsapp)) ||
                nameFragment.equals(activity.getString(R.string.paypal)) ||
                nameFragment.equals(activity.getString(R.string.twitter)) ||
                nameFragment.equals(activity.getString(R.string.instagram)) ||
                nameFragment.equals(activity.getString(R.string.tiktok)) ||
                nameFragment.equals(activity.getString(R.string.spotify)) ||
                nameFragment.equals(activity.getString(R.string.viber))){

            list.add(new ResultButtons(
                    activity.getString(R.string.search),
                    R.drawable.ic_result_search
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.share),
                    R.drawable.ic_generated_share
            ));
            list.add(new ResultButtons(
                    activity.getString(R.string.copy),
                    R.drawable.ic_result_copy
            ));
        }

        return list;
    }

    public static void setListInAppNew(Context context){
        List<InAppNewModel> list = new ArrayList<>();
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_1, R.string.in_app_new_title_1, context.getString(R.string.in_app_new_desc_1), true));
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_2, R.string.in_app_new_title_2, context.getString(R.string.in_app_new_desc_2), true));
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_3, R.string.in_app_new_title_3, context.getString(R.string.in_app_new_desc_3), true));
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_4, R.string.in_app_new_title_4, context.getString(R.string.in_app_new_desc_4), true));
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_5, 0, context.getString(R.string.in_app_new_desc_5), false));
        list.add(new InAppNewModel(R.drawable.ic_in_app_new_6, 0, context.getString(R.string.in_app_new_desc_6), false));
        Config.listInAppNewFragment = list;
    }



}
