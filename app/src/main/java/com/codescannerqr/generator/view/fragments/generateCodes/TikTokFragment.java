package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentTikTokBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class TikTokFragment extends Fragment {

    private FragmentTikTokBinding binding;
    private String typeURL = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTikTokBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.tiktok));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_tiktok);

        clickTikTokID();

        binding.textViewTikTokID.setOnClickListener(v -> clickTikTokID());

        binding.textViewURLTikTok.setOnClickListener(v -> {
            binding.textViewURLTikTok.setTextColor(requireActivity().getColor(R.color.white));
            binding.textViewURLTikTok.setBackgroundResource(R.drawable.shape_oval_long_text);
            binding.textViewTikTokID.setTextColor(requireActivity().getColor(R.color.orange));
            binding.textViewTikTokID.setBackgroundResource(R.drawable.shape_oval);
            binding.textInputEditTextTikTok.setHint(R.string.tiktok_url);
            typeURL = "URL";
        });

        binding.buttonCreateTikTok.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextTikTok.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.tiktok));
                bundle.putInt("argIcon", R.drawable.ic_gen_tiktok);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_tikTokFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //TIKTOK
        if (typeURL.equals("URL")){
            return binding.editTextTikTok.getText().toString();
        }
        else{
            return "https://tiktok.com/@"+binding.editTextTikTok.getText().toString();
        }
    }

    private void clickTikTokID(){
        binding.textViewTikTokID.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewTikTokID.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewURLTikTok.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewURLTikTok.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextTikTok.setHint(R.string.tiktok_id);
        typeURL = "ID";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}