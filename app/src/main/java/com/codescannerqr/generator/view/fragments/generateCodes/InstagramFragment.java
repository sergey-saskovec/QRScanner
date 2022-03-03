package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentInstagramBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class InstagramFragment extends Fragment {

    private FragmentInstagramBinding binding;
    private String typeURL;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstagramBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.instagram));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_instagram);

        clickInstagramUsername();

        binding.textViewUsernameInstagram.setOnClickListener(v -> clickInstagramUsername());

        binding.textViewURLInstagram.setOnClickListener(v -> clickInstagramURL());

        binding.buttonCreateInstagram.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextInstagram.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.instagram));
                bundle.putInt("argIcon", R.drawable.ic_gen_instagram);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_instagramFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //INSTAGRAMM
        if (typeURL.equals("URL")){
            return binding.editTextInstagram.getText().toString();
        }
        else{
            return "instagram://user?username="+binding.editTextInstagram.getText().toString();
        }
    }

    private void clickInstagramUsername(){
        binding.textViewUsernameInstagram.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewUsernameInstagram.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewURLInstagram.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewURLInstagram.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextInstagram.setHint(R.string.instagram_username);
        typeURL = "username";
    }

    private void clickInstagramURL(){
        binding.textViewURLInstagram.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewURLInstagram.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewUsernameInstagram.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewUsernameInstagram.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextInstagram.setHint(R.string.instagram_url);
        typeURL = "URL";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}