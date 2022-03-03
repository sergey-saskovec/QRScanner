package com.codescannerqr.generator.view.fragments.generateCodes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentWebsiteBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class WebsiteFragment extends Fragment {

    private FragmentWebsiteBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebsiteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.website));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_website);

        binding.textViewWWWWebsite.setOnClickListener(v ->
                binding.editTextWebsite.setText(binding.editTextWebsite.getText().toString() +
                getString(R.string.www)));

        binding.textViewComWebsite.setOnClickListener(v ->
                binding.editTextWebsite.setText(binding.editTextWebsite.getText().toString() +
                getString(R.string.com)));

        binding.buttonCreateWebsite.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextWebsite.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.website));
                bundle.putInt("argIcon", R.drawable.ic_gen_website);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_websiteFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //WEBSITE
        return binding.editTextWebsite.getText().toString();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}