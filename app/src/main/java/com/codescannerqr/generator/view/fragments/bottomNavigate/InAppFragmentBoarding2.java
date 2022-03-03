package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.billingclient.api.SkuDetails;
import com.codescannerqr.generator.App;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.AdapterInApp2;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.FragmentInAppBoarding2Binding;
import com.codescannerqr.generator.helpers.IntentHelpers;
import com.codescannerqr.generator.helpers.SharedPreferenceManager;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.billengNew.TrivialDriveRepository;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.view.activity.OneBoardingActivity;
import com.codescannerqr.generator.view.activity.SplashScreenActivity;

import java.util.List;

public class InAppFragmentBoarding2 extends Fragment {

    private FragmentInAppBoarding2Binding binding;
    private SharedPreferenceManager sharedPreferenceManager;
    private TrivialDriveRepository trivialDriveRepository;
    private AdapterInApp2 adapter;
    private InAppFragmentBoarding2ViewModel viewModelInApp2;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInAppBoarding2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext());
        trivialDriveRepository = ((App) requireActivity().getApplication()).appContainer.
                trivialDriveRepository;
        createAdapter();

        trivialDriveRepository.getSkuPrice(Config.productID1).observe(requireActivity(), s ->
                binding.textViewDescButtonInApp2.setText(
                        getString(R.string.free_3_days_1)
                        + " "
                        + s +
                        getString(R.string.free_3_days_2)
        ));

        trivialDriveRepository.isPurchased(Config.productID1).observe(requireActivity(), aBoolean -> {
            if (aBoolean){
                closeInAppFragment();
            }
        });

        trivialDriveRepository.isPurchased(Config.productID2).observe(requireActivity(), aBoolean -> {
            if (aBoolean){
                closeInAppFragment();
            }
        });

        binding.buttonInApp2.setOnClickListener(v -> {
            trivialDriveRepository.buySku(requireActivity(), Config.productID1);
        });

        binding.textViewPrivacyPolicyInApp2.setOnClickListener(v -> {
            IntentHelpers.shareIntent(requireActivity(), Config.privacyPolicyUrl);
        });

        binding.textViewRestorePurchaseInApp2.setOnClickListener(v -> {
            //Apphud.syncPurchases();
        });

        binding.textViewTermsInApp2.setOnClickListener(v -> {
            IntentHelpers.shareIntent(requireActivity(), Config.termsUrl);
        });



        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModelInApp2 = new ViewModelProvider(requireActivity()).get(InAppFragmentBoarding2ViewModel.class);
        viewModelInApp2.getData(this).observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    startTimers();
                }

            }
        });
    }

    private void createAdapter(){
        adapter = new AdapterInApp2();
        binding.recyclerViewInApp2.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewInApp2.setAdapter(adapter);
    }

    private void closeInAppFragment() {
        Config.premium = true;
        sharedPreferenceManager.putBoolean("premium", true);
        requireActivity().startActivity(new Intent(requireActivity(), SplashScreenActivity.class));
        requireActivity().finish();
    }

    private void showInterDone() {
        InterstitialManager.showInter(requireActivity(), new InterDismiss() {
            @Override
            public void interDismiss(int count) {
                if (count == 1) {
                    ViewDialog.showDialogRemoveAds(requireActivity());
                } else {
                    nextActionInter();
                }
            }
        });

    }

    private void nextActionInter() {
        sharedPreferenceManager.putBoolean("firstLaunch", false);
        startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

    public void startTimers() {
        new CountDownTimer(3000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                binding.imageViewCloseInApp2.setVisibility(View.VISIBLE);
                binding.imageViewCloseInApp2.setOnClickListener(v -> {
                    try {
                        if (InterstitialManager.getLoaded() && !Config.premium) {
                            showInterDone();
                        } else {
                            nextActionInter();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        nextActionInter();
                    }

                });
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}