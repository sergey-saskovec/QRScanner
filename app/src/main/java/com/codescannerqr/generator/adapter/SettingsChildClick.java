package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;

import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.helpers.IntentHelpers;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.model.SectionSettingsList;
import com.codescannerqr.generator.view.fragments.bottomNavigate.SettingsBottomFragment;

import java.util.List;

public class SettingsChildClick implements View.OnClickListener{

    private final RecyclerView recyclerView;
    private final List<SectionSettingsList> items;
    private final Activity activity;
    private final SettingsBottomFragment fragment;

    public SettingsChildClick(RecyclerView recyclerView, List<SectionSettingsList> items,
                              Activity activity, SettingsBottomFragment fragment) {
        this.recyclerView = recyclerView;
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        ViewDialog viewDialog = new ViewDialog();
        int itemPos = recyclerView.getChildLayoutPosition(v);
        switch (items.get(itemPos).getLogo()){
            case R.drawable.ic_settings_location:
                viewDialog.showDialogLanguage(activity, fragment);
                break;

            case R.drawable.ic_settings_restore_purchase:
                //Apphud.syncPurchases();
                break;

            case R.drawable.ic_settings_privacy_policy:
                IntentHelpers.shareIntent(activity, Config.privacyPolicyUrl);

                break;

            case R.drawable.ic_settings_share:
                ShareCompat.IntentBuilder intentBuilder = new ShareCompat.IntentBuilder(activity);
                intentBuilder.setType("text/plain")
                        .setText(activity.getString(R.string.share_settings) + " " +
                                Config.BASE_URL_GOOGLE_PLAY + activity.getPackageName())
                        .setChooserTitle("chooserTitle")
                        .startChooser();
                break;

            case R.drawable.ic_settings_rate_us:
                ViewDialog.showDialogRateUs(activity, null, "childClick");
                break;
        }
        SettingsHelpers.saveSettings(activity);
    }
}
