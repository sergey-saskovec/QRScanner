package com.codescannerqr.generator.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.codescannerqr.generator.App;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.ActivitySplashScreenBinding;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.helpers.SharedPreferenceManager;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.presenter.billengNew.TrivialDriveRepository;
import com.codescannerqr.generator.view.IGeneratedView;

public class SplashScreenActivity extends AppCompatActivity implements IGeneratedView {

    private SharedPreferenceManager manager;
    private TrivialDriveRepository trivialDriveRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        trivialDriveRepository = ((App)getApplication()).appContainer.
                trivialDriveRepository;
        setObserveSubs();
        manager = SharedPreferenceManager.getInstance(this);

        SettingsHelpers.setSettings(this);
        ListHistoryHelpers.setListInAppNew(this);
        if (!Config.premium){
            if (!manager.getBoolean("premium", false)){
                Config.premium = manager.getBoolean("premium", false);
            }
        }

        manager.putInt("countInter", 0);

        if (!Config.premium){
            if (!InterstitialManager.getLoaded()){
                new InterstitialManager(this);
            }
        }
        startTimers();
    }

    private void setObserveSubs() {
        trivialDriveRepository.isPurchased(Config.productID1).observe(this, aBoolean -> {
            if (aBoolean){
                manager.putBoolean("premium", true);
                Config.premium = true;
            }
        });

        trivialDriveRepository.isPurchased(Config.productID2).observe(this, aBoolean -> {
            if (aBoolean){
                manager.putBoolean("premium", true);
                Config.premium = true;
            }
        });
    }

    @Override
    public void resultRestorePurchase(String result) {
        if (result.equals("true")){
            manager.putBoolean("premium", true);
        }
        else{
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimers() {
        CountDownTimer countDownTimerSplashJoh = new CountDownTimer(3000, 10) {

            @Override
            public void onTick(long millisUntilFinishedPool) {}

            @Override
            public void onFinish() {
                if (manager.getBoolean("firstLaunch", true)){
                    if (!Config.premium || !manager.getBoolean("premium", false)){
                        startActivity(
                                new Intent(
                                        SplashScreenActivity.this,
                                        OneBoardingActivity.class
                                )
                        );
                    }
                    else{
                        setIntent();
                    }
                }
                else{
                    setIntent();
                }

                finish();
            }
        };
        countDownTimerSplashJoh.start();
    }

    private void setIntent(){
        if (!Config.premium || !manager.getBoolean("premium", false)){
            startActivity(new Intent(SplashScreenActivity.this, FreeTrialActivity.class));
        }
        else {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        }
    }
}