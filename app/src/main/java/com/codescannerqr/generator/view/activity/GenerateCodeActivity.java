package com.codescannerqr.generator.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.ActivityGenerateCodeBinding;

public class GenerateCodeActivity extends AppCompatActivity {

    private ActivityGenerateCodeBinding binding;
    public NavController navControllerGenerate;
    private NavGraph navGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolBarGenerate.toolbar);
        if (!Config.premium){
            if (!InterstitialManager.getLoaded()){
                new InterstitialManager(this);
            }
        }
        binding.toolBarGenerate.imageViewBackGenerate.setOnClickListener(v -> onBackPressed());
        navControllerGenerate = Navigation.findNavController(this, R.id.nav_host_generate);
        navGraph = navControllerGenerate.getNavInflater().inflate(R.navigation.generate_navigation);
        checkFragments(getIntent().getIntExtra("nameFragment", 0));
    }

    @SuppressLint("NonConstantResourceId")
    private void checkFragments(int numbFragment) {
        switch (numbFragment){
            case R.string.clipboard:
                navGraph.setStartDestination(R.id.clipBoardFragment);
                break;
            case R.string.website:
                navGraph.setStartDestination(R.id.websiteFragment);
                break;
            case R.string.wifi:
                navGraph.setStartDestination(R.id.wifiFragment);
                break;
            case R.string.text:
                navGraph.setStartDestination(R.id.textFragment);
                break;
            case R.string.contact:
                navGraph.setStartDestination(R.id.contactFragment);
                break;
            case R.string.cellphone:
                navGraph.setStartDestination(R.id.cellPhoneFragment);
                break;
            case R.string.email:
                navGraph.setStartDestination(R.id.emailFragment);
                break;
            case R.string.sms:
                navGraph.setStartDestination(R.id.SMSFragment);
                break;
            case R.string.my_card:
                navGraph.setStartDestination(R.id.myCardFragment);
                break;
            case R.string.geo:
                navGraph.setStartDestination(R.id.GEOFragment);
                break;
            case R.string.calendar:
                navGraph.setStartDestination(R.id.calendarFragment);
                break;
            case R.string.product_code:
                navGraph.setStartDestination(R.id.productFragment);
                break;
            case R.string.facebook:
                navGraph.setStartDestination(R.id.facebookFragment);
                break;
            case R.string.youtube:
                navGraph.setStartDestination(R.id.youTubeFragment);
                break;
            case R.string.whatsapp:
                navGraph.setStartDestination(R.id.whatsAppFragment);
                break;
            case R.string.paypal:
                navGraph.setStartDestination(R.id.payPalFragment);
                break;
            case R.string.twitter:
                navGraph.setStartDestination(R.id.twitterFragment);
                break;
            case R.string.instagram:
                navGraph.setStartDestination(R.id.instagramFragment);
                break;
            case R.string.spotify:
                navGraph.setStartDestination(R.id.spotifyFragment);
                break;
            case R.string.tiktok:
                navGraph.setStartDestination(R.id.tikTokFragment);
                break;
            case R.string.viber:
                navGraph.setStartDestination(R.id.viberFragment);
                break;
        }
        navControllerGenerate.setGraph(navGraph);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void setTextViewToolbar(String title){
        binding.toolBarGenerate.toolbarTitleGenerate.setText(title);
    }

    public void setIconToolbar(int resourse){
        binding.toolBarGenerate.imageViewIconGenerate.setVisibility(View.VISIBLE);
        binding.toolBarGenerate.imageViewIconGenerate.setImageResource(resourse);
    }

    public void iconToolbarGone(){
        binding.toolBarGenerate.imageViewIconGenerate.setVisibility(View.GONE);
    }
}