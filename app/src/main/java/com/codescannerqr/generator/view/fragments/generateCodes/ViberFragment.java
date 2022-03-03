package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentViberBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class ViberFragment extends Fragment {

    private FragmentViberBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViberBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.viber));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_viber);

        binding.buttonCreateViber.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextPhoneNumberViber.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.viber));
                bundle.putInt("argIcon", R.drawable.ic_gen_viber);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_viberFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //VIBER
        String phoneNumber = binding.ccp.getSelectedCountryCodeWithPlus() +
                binding.editTextPhoneNumberViber.getText().toString();
        return "viber://add?number="+phoneNumber.replaceAll("//+", "");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}