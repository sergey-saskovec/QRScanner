package com.codescannerqr.generator.view.fragments.generateCodes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentClipBoardBinding;
import com.codescannerqr.generator.helpers.MyClipboardManager;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class ClipBoardFragment extends Fragment {

    private FragmentClipBoardBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClipBoardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.clipboard));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_clipboard);

        binding.editTextContentClipBoard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                binding.textViewCountSymbolsClipBoard.setText(
                        binding.editTextContentClipBoard.getText().length() + "/300");
            }
        });

        root.getViewTreeObserver().addOnWindowFocusChangeListener(hasFocus -> {
            try {
                String sClip = MyClipboardManager.readFromClipboard(getActivity());

                binding.editTextContentClipBoard.setText(sClip);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });

        binding.buttonCreateClipBoard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextContentClipBoard.getText().toString().equals("")) {
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.clipboard));
                bundle.putInt("argIcon", R.drawable.ic_gen_clipboard);

                ((GenerateCodeActivity) requireActivity()).navControllerGenerate.
                        navigate(R.id.action_clipBoardFragment_to_generatedFragment, bundle);
            } else {
                Toast.makeText(getActivity(), getString(R.string.field_empty), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        return binding.editTextContentClipBoard.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}