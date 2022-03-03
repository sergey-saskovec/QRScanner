package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentGeoBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class GEOFragment extends Fragment {

    private FragmentGeoBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGeoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.geo));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_geo);

        binding.buttonCreateGeo.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextGeoLatitude.getText().toString().equals("") &&
                    !binding.editTextGeoLongitude.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.geo));
                bundle.putInt("argIcon", R.drawable.ic_gen_geo);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_GEOFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //GEO
        return "geo:"+binding.editTextGeoLatitude.getText().toString() + "," +
                binding.editTextGeoLongitude.getText().toString();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}