package com.codescannerqr.generator.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.view.fragments.historyCodes.BookmarkHistoryFragment;
import com.codescannerqr.generator.view.fragments.historyCodes.CreateHistoryFragment;
import com.codescannerqr.generator.view.fragments.historyCodes.ScanHistoryFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyTabPagerAdapter2 extends FragmentStateAdapter {

    public MyTabPagerAdapter2(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ScanHistoryFragment();

            case 1:
                return new CreateHistoryFragment();

            case 2:
                return new BookmarkHistoryFragment();

            default:
                return null;

        }
    }

    @Override
    public int getItemCount() {
        return Config.listNameHistoryFragment.size();
    }

    public List<Integer> getNameFragment() {
        return Config.listNameHistoryFragment;
    }
}
