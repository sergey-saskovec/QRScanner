package com.codescannerqr.generator.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.ActivityFreeTrialBinding;
import com.codescannerqr.generator.view.fragments.bottomNavigate.InAppFragmentBoarding2ViewModel;

public class FreeTrialActivity extends AppCompatActivity {

    private InAppFragmentBoarding2ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFreeTrialBinding binding = ActivityFreeTrialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(InAppFragmentBoarding2ViewModel.class);
        if (!Config.premium){
            if (!InterstitialManager.getLoaded()){
                new InterstitialManager(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setData(true);
    }
}