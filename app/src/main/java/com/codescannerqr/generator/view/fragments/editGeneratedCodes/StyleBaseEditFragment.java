package com.codescannerqr.generator.view.fragments.editGeneratedCodes;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

public class StyleBaseEditFragment extends Fragment {

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ViewDialog viewDialog = new ViewDialog();
                viewDialog.showDialogDiscard((GenerateCodeActivity) getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}
