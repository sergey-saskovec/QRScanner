package com.codescannerqr.generator.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codescannerqr.generator.helpers.SharedPreferenceManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.ActivityMain2Binding;
import com.codescannerqr.generator.helpers.AdSizeHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity{

    private ActivityMain2Binding binding;
    public NavController navController;
    private AdView adView;
    public static boolean aBoolean = false;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);
        setSupportActionBar(binding.toolBarMain.toolbar);

        if (!Config.premium || !sharedPreferenceManager.getBoolean("premium", false)){
            if (!InterstitialManager.getLoaded()){
                new InterstitialManager(this);
            }
        }else {
            visiblePremium(false);
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setSelectedItemId(R.id.navigation_generate);
        binding.fab.setOnClickListener(v -> checkPerm());
        binding.toolBarMain.buttonToolbar.setOnClickListener(v -> {
            if (!Config.premium || !sharedPreferenceManager.getBoolean("premium", false)){
                startActivity(new Intent(MainActivity.this,FreeTrialActivity.class));
            }
            else {
                Toast.makeText(this, getString(R.string.no_ads), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCameraScanner(){
        navController.navigate(R.id.navigation_scanner);
    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
            setCameraScanner();
        }
        else{
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogCameraPermission(this);
        }
    }

    public void requestCameraPermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    public void requestFilePermission(String stringPermission) {
        requestPermissionFileLauncher.launch(
                stringPermission);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    requestFilePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                else {
                    Toast.makeText(
                            this,
                            getString(R.string.perm_camera_denied),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

    private final ActivityResultLauncher<String> requestPermissionFileLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setCameraScanner();
                }
                else {
                    Toast.makeText(this, getString(R.string.perm_file_denied), Toast.LENGTH_SHORT).show();
                }
            });

    public void setTextViewToolbar(String title){
        binding.toolBarMain.toolbarTitle.setText(title);
    }

    public void visiblePremium(boolean b){
        if (b){
            binding.toolBarMain.buttonToolbar.setVisibility(View.VISIBLE);
        }
        else{
            binding.toolBarMain.buttonToolbar.setVisibility(View.GONE);
        }

    }

    public void visibleButtonBack(boolean a, boolean adsBools){
        if (a && adsBools){
            binding.toolBarMain.imageViewBackMain.setVisibility(View.VISIBLE);
            binding.toolBarMain.imageViewBackMain.setOnClickListener(v -> {
                try {
                    if (InterstitialManager.getLoaded() && !Config.premium){
                        showInterDone();
                    }
                    else{
                        nextActionInter();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    nextActionInter();
                }

            });
        }
        else if (a && !adsBools){
            binding.toolBarMain.imageViewBackMain.setVisibility(View.VISIBLE);
            binding.toolBarMain.imageViewBackMain.setOnClickListener(v -> nextActionInter());

        }
        else{
            binding.toolBarMain.imageViewBackMain.setVisibility(View.GONE);
        }
    }

    private void showInterDone() {
        InterstitialManager.showInter(this, new InterDismiss() {
            @Override
            public void interDismiss(int countInter) {
                if (countInter == 1){
                    ViewDialog.showDialogRemoveAds(MainActivity.this);
                }
                else{
                    nextActionInter();
                }
            }
        });

    }

    private void nextActionInter(){
        onBackPressed();
    }

    public void toolBarVisible(boolean a){
        if (a){
            binding.toolBarMain.toolbar.setVisibility(View.VISIBLE);
            binding.bannerTop.adViewContainer.setVisibility(View.GONE);
        }
        else {
            if (Config.premium){
                toolBarVisible(true);
            }
            else{
                binding.toolBarMain.toolbar.setVisibility(View.GONE);
                binding.bannerTop.adViewContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    public void loadBanner(){
        //LOAD BANNER
        if (aBoolean){
            adView = new AdView(this);
            adView.setAdUnitId(Config.bannerID);
            binding.bannerTop.adViewContainer.removeAllViews();
            binding.bannerTop.adViewContainer.addView(adView);

            adView.setAdSize(AdSizeHelpers.adSizeCustom(this));

            AdRequest adRequest = new AdRequest.Builder().build();

            adView.loadAd(adRequest);
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}