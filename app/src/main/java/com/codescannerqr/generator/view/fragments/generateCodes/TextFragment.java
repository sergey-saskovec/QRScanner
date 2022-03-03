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
import com.codescannerqr.generator.databinding.FragmentTextBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class TextFragment extends Fragment {

    private FragmentTextBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTextBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.text));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_text);
        binding.editTextContentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                binding.textViewCountSymbolsText.setText(
                        binding.editTextContentText.getText().length() + "/300");

            }
        });

        binding.buttonCreateText.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextContentText.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.text));
                bundle.putInt("argIcon", R.drawable.ic_gen_text);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_textFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        return binding.editTextContentText.getText().toString();
    }
}