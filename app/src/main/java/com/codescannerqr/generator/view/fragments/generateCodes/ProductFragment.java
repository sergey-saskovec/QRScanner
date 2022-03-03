package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentProductBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.product_code));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_product_code);

        binding.buttonCreateProduct.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextProduct.getText().toString().equals("") &&
                    binding.editTextProduct.getText().toString().length() == 12){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.product_code));
                bundle.putInt("argIcon", R.drawable.ic_gen_product_code);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_productFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.error_product_code),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //PRODUCT
        return binding.editTextProduct.getText().toString();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}