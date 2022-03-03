package com.codescannerqr.generator;

import android.annotation.SuppressLint;
import android.app.Application;

import com.codescannerqr.generator.presenter.billengNew.BillingDataSource;
import com.codescannerqr.generator.presenter.billengNew.TrivialDriveRepository;
import com.google.android.gms.ads.MobileAds;
import com.codescannerqr.generator.ads.AppOpenManager;


public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static AppOpenManager appOpenManager;
    public AppContainer appContainer;

    public class AppContainer {
        final BillingDataSource billingDataSource = BillingDataSource.getInstance(
                App.this,
                TrivialDriveRepository.INAPP_SKUS,
                TrivialDriveRepository.SUBSCRIPTION_SKUS,
                TrivialDriveRepository.AUTO_CONSUME_SKUS);
        final public TrivialDriveRepository trivialDriveRepository = new TrivialDriveRepository(
                billingDataSource);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer();
        MobileAds.initialize(this, initializationStatus -> {});

        if (!Config.premium){
            appOpenManager = new AppOpenManager(this);
        }
    }
}
