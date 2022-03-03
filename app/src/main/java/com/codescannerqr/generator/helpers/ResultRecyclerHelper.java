package com.codescannerqr.generator.helpers;

import android.app.Activity;

import com.codescannerqr.generator.R;

public class ResultRecyclerHelper {

    public static int getRowColumns(String nameFragment, Activity activity){
        if (nameFragment.equals(activity.getString(R.string.sms))){
            return 2;
        }
        else if (nameFragment.equals(activity.getString(R.string.contact)) ||
                nameFragment.equals(activity.getString(R.string.cellphone)) ||
                nameFragment.equals(activity.getString(R.string.email))
        ){
            return 4;
        }
        else if (nameFragment.equals(activity.getString(R.string.text)) ||
                nameFragment.equals(activity.getString(R.string.my_card))
        ){
            return 5;
        }
        else{
            return 3;
        }
    }
}
