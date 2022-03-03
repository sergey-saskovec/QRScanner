package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentEmailBinding;
import com.codescannerqr.generator.helpers.EmailHelpers;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class EmailFragment extends Fragment implements IGeneratedView {

    private FragmentEmailBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.email));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_email);

        binding.buttonCreateEmail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextEmail.getText().toString().isEmpty() &&
                    !binding.editTextSubject.getText().toString().isEmpty() &&
                    EmailHelpers.isValidEmail(binding.editTextEmail.getText())){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.email));
                bundle.putInt("argIcon", R.drawable.ic_gen_email);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_emailFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //EMAIL
        String mail = binding.editTextEmail.getText().toString();
        String subject = binding.editTextSubject.getText().toString();
        return "mailto:"+mail+"?subject="+subject.replaceAll(" ","%20");
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}