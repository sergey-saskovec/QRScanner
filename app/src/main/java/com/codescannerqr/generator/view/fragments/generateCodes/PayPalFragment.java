package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentPayPalBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class PayPalFragment extends Fragment {

    private FragmentPayPalBinding binding;
    private String typeURL = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPayPalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.paypal));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_paypal);

        clickPayPalLink();

        binding.textViewLinkPayPal.setOnClickListener(v -> clickPayPalLink());

        binding.textViewUsernamePayPal.setOnClickListener(v -> clickPayPalUsername());

        binding.buttonCreatePayPal.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextPayPal.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.paypal));
                bundle.putInt("argIcon", R.drawable.ic_gen_paypal);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_payPalFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //PAYPAL
        if (typeURL.equals("username")){
            return "https://paypal.me/"+binding.editTextPayPal.getText().toString();
        }
        else {
            return binding.editTextPayPal.getText().toString();
        }

    }

    private void clickPayPalLink(){
        binding.textViewLinkPayPal.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewLinkPayPal.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewUsernamePayPal.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewUsernamePayPal.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextPayPal.setHint(R.string.paypal_link);
        typeURL = "link";
    }

    private void clickPayPalUsername(){
        binding.textViewUsernamePayPal.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewUsernamePayPal.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewLinkPayPal.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewLinkPayPal.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextPayPal.setHint(R.string.paypal_username);
        typeURL = "username";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}