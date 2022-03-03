package com.codescannerqr.generator.view.fragments.oneBoarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codescannerqr.generator.R;

public class OneFragment extends Fragment {

    private int statePosition;

    public OneFragment(int position){
        this.statePosition = position;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (statePosition == 1){
            return inflater.inflate(R.layout.fragment_two, container, false);
        }
        else if (statePosition == 2){
            return inflater.inflate(R.layout.fragment_three, container, false);
        }
        else{
            return inflater.inflate(R.layout.fragment_one, container, false);
        }

    }
}