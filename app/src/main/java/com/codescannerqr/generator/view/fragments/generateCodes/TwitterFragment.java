package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentTwitterBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class TwitterFragment extends Fragment {

    private FragmentTwitterBinding binding;
    private String typeURL;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTwitterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.twitter));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_twitter);

        clickTwitterID();

        binding.textViewTwitterID.setOnClickListener(v -> clickTwitterID());

        binding.textViewURLTwitter.setOnClickListener(v -> clickTwitterURL());


        binding.buttonCreateTwitter.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextTwitter.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.twitter));
                bundle.putInt("argIcon", R.drawable.ic_gen_twitter);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_twitterFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

    private String createDataInBundle() {
        //TWITTER
        if (typeURL.equals("URL")){
            return binding.editTextTwitter.getText().toString();
        }
        else{
            return "twitter://user?screen_name="+binding.editTextTwitter.getText().toString();
        }
    }

    private void clickTwitterID(){
        binding.textViewTwitterID.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewTwitterID.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewURLTwitter.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewURLTwitter.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextTwitter.setHint(R.string.twitter_id);
        typeURL = "ID";
    }

    private void clickTwitterURL(){
        binding.textViewURLTwitter.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewURLTwitter.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewTwitterID.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewTwitterID.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextTwitter.setHint(R.string.twitter_url);
        typeURL = "URL";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}