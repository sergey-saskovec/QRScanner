package com.codescannerqr.generator.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.codescannerqr.generator.view.fragments.oneBoarding.OneFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new OneFragment(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
