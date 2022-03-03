package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentFacebookBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class FacebookFragment extends Fragment {

    private FragmentFacebookBinding binding;
    private String typeURL = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFacebookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.facebook));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_facebook);

        clickFacebookID();

        binding.textViewFacebookID.setOnClickListener(v -> clickFacebookID());

        binding.textViewUrlFaceBook.setOnClickListener(v -> clickFacebookURl());

        binding.buttonCreateFacebook.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextFacebook.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.facebook));
                bundle.putInt("argIcon", R.drawable.ic_gen_facebook);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_facebookFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //FACEBOOK
        if (typeURL.equals("URL")){
            return binding.editTextFacebook.getText().toString();
        }
        else {
            return "fb://profile"+binding.editTextFacebook.getText().toString();
        }
    }

    private void clickFacebookID(){
        binding.textViewFacebookID.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewFacebookID.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewUrlFaceBook.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewUrlFaceBook.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextFacebook.setHint(R.string.facebook_id);
        typeURL = "ID";
    }

    private void clickFacebookURl(){
        binding.textViewUrlFaceBook.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewUrlFaceBook.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewFacebookID.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewFacebookID.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextFacebook.setHint(R.string.facebook_url);
        typeURL = "URL";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}