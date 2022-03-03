package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.SectionList;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import java.util.List;

public class ChildRecyclerClick implements View.OnClickListener {

    private final RecyclerView recyclerView;
    private final List<SectionList> items;
    private final Activity activity;

    public ChildRecyclerClick(RecyclerView recyclerView,
                              List<SectionList> items,
                              Activity activity) {
        this.recyclerView = recyclerView;
        this.items = items;
        this.activity = activity;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int itemPos = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(activity, GenerateCodeActivity.class);
        switch (items.get(itemPos).getName()) {
            case R.string.clipboard:
                intent.putExtra("nameFragment", R.string.clipboard);
                break;
            case R.string.website:
                intent.putExtra("nameFragment", R.string.website);
                break;
            case R.string.wifi:
                intent.putExtra("nameFragment", R.string.wifi);
                break;
            case R.string.text:
                intent.putExtra("nameFragment", R.string.text);
                break;
            case R.string.contact:
                intent.putExtra("nameFragment", R.string.contact);
                break;
            case R.string.cellphone:
                intent.putExtra("nameFragment", R.string.cellphone);
                break;
            case R.string.email:
                intent.putExtra("nameFragment", R.string.email);
                break;
            case R.string.sms:
                intent.putExtra("nameFragment", R.string.sms);
                break;
            case R.string.my_card:
                intent.putExtra("nameFragment", R.string.my_card);
                break;
            case R.string.geo:
                intent.putExtra("nameFragment", R.string.geo);
                break;
            case R.string.calendar:
                intent.putExtra("nameFragment", R.string.calendar);
                break;
            case R.string.product_code:
                intent.putExtra("nameFragment", R.string.product_code);
                break;
            case R.string.facebook:
                intent.putExtra("nameFragment", R.string.facebook);
                break;
            case R.string.youtube:
                intent.putExtra("nameFragment", R.string.youtube);
                break;
            case R.string.whatsapp:
                intent.putExtra("nameFragment", R.string.whatsapp);
                break;
            case R.string.paypal:
                intent.putExtra("nameFragment", R.string.paypal);
                break;
            case R.string.twitter:
                intent.putExtra("nameFragment", R.string.twitter);
                break;
            case R.string.instagram:
                intent.putExtra("nameFragment", R.string.instagram);
                break;
            case R.string.spotify:
                intent.putExtra("nameFragment", R.string.spotify);
                break;
            case R.string.tiktok:
                intent.putExtra("nameFragment", R.string.tiktok);
                break;
            case R.string.viber:
                intent.putExtra("nameFragment", R.string.viber);
                break;
        }

        activity.startActivity(intent);
    }
}
