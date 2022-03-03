package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentWhatsAppBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class WhatsAppFragment extends Fragment {

    private FragmentWhatsAppBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWhatsAppBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.whatsapp));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_whatsapp);

        binding.buttonCreateWhatsApp.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextPhoneNumberWhatsApp.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.whatsapp));
                bundle.putInt("argIcon", R.drawable.ic_gen_whatsapp);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_whatsAppFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //WHATSAPP
        Log.d("TAGING", binding.ccp.getSelectedCountryCodeWithPlus());
        String phoneNumber = binding.ccp.getSelectedCountryCodeWithPlus() +
                binding.editTextPhoneNumberWhatsApp.getText().toString();
        return "whatsapp://send?phone"+phoneNumber.replaceAll("//+", "");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}