package com.codescannerqr.generator.view.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.ScreenSlidePagerAdapter;
import com.codescannerqr.generator.databinding.ActivityOneBoardingBinding;
import com.codescannerqr.generator.helpers.DepthPageTransformer;
import com.codescannerqr.generator.view.fragments.bottomNavigate.InAppFragmentBoarding2;
import com.codescannerqr.generator.view.fragments.bottomNavigate.InAppFragmentBoarding2ViewModel;
import com.tbuonomo.viewpagerdotsindicator.OnPageChangeListenerHelper;

public class OneBoardingActivity extends FragmentActivity {

    private ActivityOneBoardingBinding binding;
    private static final int NUM_PAGES = 3;
    private ScreenSlidePagerAdapter pagerAdapter;
    public int currentPosition = 0;
    private InAppFragmentBoarding2ViewModel viewModelInApp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOneBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pagerAdapter = new ScreenSlidePagerAdapter(this);
        binding.pagerOneBoarding.setAdapter(pagerAdapter);
        binding.pagerOneBoarding.setPageTransformer(new DepthPageTransformer());
        binding.dotsIndicator.setViewPager2(binding.pagerOneBoarding);

        viewModelInApp2 = new ViewModelProvider(this).get(InAppFragmentBoarding2ViewModel.class);

        binding.pagerOneBoarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
            }
        });

        binding.buttonOneFragment.setOnClickListener(v -> {
            if (currentPosition < 2){
                binding.pagerOneBoarding.setCurrentItem(currentPosition+1);
            }
            else{
                binding.pagerOneBoarding.setVisibility(View.GONE);
                binding.buttonOneFragment.setVisibility(View.GONE);
                binding.dotsIndicator.setVisibility(View.GONE);
                binding.fragmentInAppBoarding.setVisibility(View.VISIBLE);
                viewModelInApp2.setData(true);
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (binding.pagerOneBoarding.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binding.pagerOneBoarding.setCurrentItem(binding.pagerOneBoarding.getCurrentItem() - 1);
        }
    }
}