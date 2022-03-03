package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentWifiBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WifiFragment extends Fragment {

    private FragmentWifiBinding binding;
    private String security;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWifiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.wifi));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_wifi);

        clickWPA();

        binding.textViewWPAFragmentWifi.setOnClickListener(v -> clickWPA());

        binding.textViewWepFragmentWifi.setOnClickListener(v -> clickWEP());

        binding.textViewNoneFragmentWifi.setOnClickListener(v -> clickNone());

        binding.buttonCreateWifi.setOnClickListener(v -> {
            Bundle bundle = new Bundle();

            if (
                    !binding.editTextSSIDFragmentWifi.getText().toString().equals("") &&
                            !Objects.requireNonNull(binding.editTextPasswordFragmentWifi.getText()).toString().equals("")) {
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.wifi));
                bundle.putInt("argIcon", R.drawable.ic_gen_wifi);

                ((GenerateCodeActivity) requireActivity()).navControllerGenerate.
                        navigate(R.id.action_wifiFragment_to_generatedFragment, bundle);
            } else {
                Toast.makeText(getActivity(), getString(R.string.field_empty), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //WIFI
        String s = binding.editTextSSIDFragmentWifi.getText().toString();
        String p = Objects.requireNonNull(binding.editTextPasswordFragmentWifi.getText()).toString();
        String t;
        if (security.equals("")) {
            t = "None";
        } else {
            t = security;
        }
        return "WIFI:S:" + s + ";" + "T:" + t + ";" + "P:" + p + ";;";
    }

    private void clickWPA() {
        binding.textViewWPAFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.orange));
        binding.textViewWepFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewNoneFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewWPAFragmentWifi.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewWepFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        binding.textViewNoneFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        security = "WPA";
    }

    private void clickWEP() {
        binding.textViewWepFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.orange));
        binding.textViewWPAFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewNoneFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewWPAFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        binding.textViewWepFragmentWifi.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewNoneFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        security = "WEP";
    }

    private void clickNone() {
        binding.textViewNoneFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.orange));
        binding.textViewWPAFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewWepFragmentWifi.setBackgroundColor(requireActivity().getColor(R.color.light_gray));
        binding.textViewWPAFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        binding.textViewWepFragmentWifi.setTextColor(requireActivity().getColor(R.color.gray));
        binding.textViewNoneFragmentWifi.setTextColor(requireActivity().getColor(R.color.white));
        security = "None";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}