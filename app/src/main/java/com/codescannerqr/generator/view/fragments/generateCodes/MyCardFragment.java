package com.codescannerqr.generator.view.fragments.generateCodes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentMyCardBinding;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.model.Contact;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyCardFragment extends Fragment implements IGeneratedView {

    private FragmentMyCardBinding binding;
    private GeneratePresenter generatePresenter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyCardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.my_card));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_mycard);
        generatePresenter = new GeneratePresenter(this);
        binding.textInputLayoutEditMyCard.setEndIconOnClickListener(v -> getContact());
        binding.editTextContentMyCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                binding.textViewCountSymbolsMyCard.setText(
                        binding.editTextContentMyCard.getText().length()+"/300");
            }
        });

        binding.buttonCreateMyCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (
                    !Objects.requireNonNull(binding.editTextNameFragmentMyCard.getText()).toString().equals("") &&
                    !Objects.requireNonNull(binding.editTextPhoneFragmentMyCard.getText()).toString().equals("") &&
                    !Objects.requireNonNull(binding.editTextEmailFragmentMyCard.getText()).toString().equals("") &&
                    !Objects.requireNonNull(binding.editTextAddressFragmentMyCard.getText()).toString().equals("") &&
                    !Objects.requireNonNull(binding.editTextOrgFragmentMyCard.getText()).toString().equals("") &&
                    !binding.editTextContentMyCard.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.my_card));
                bundle.putInt("argIcon", R.drawable.ic_gen_mycard);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_myCardFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private void getContact() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            someActivityResultLauncher.launch(contactPickerIntent);
        } else {
            ViewDialog dialogNotPermissionContact = new ViewDialog();
            dialogNotPermissionContact.showDialogContactPermission(requireActivity(),
                    this, "mycard");
        }
    }

    @Override
    public void resultFullContact(Contact contact) {
        binding.editTextNameFragmentMyCard.setText(contact.getName());
        binding.editTextPhoneFragmentMyCard.setText(contact.getPhone());
        binding.editTextEmailFragmentMyCard.setText(contact.getEmail());
        binding.editTextAddressFragmentMyCard.setText(contact.getAddress());
        binding.editTextOrgFragmentMyCard.setText(contact.getOrg());
    }

    private String createDataInBundle() {
        //MYCARD
        String N = Objects.requireNonNull(binding.editTextNameFragmentMyCard.getText()).toString();
        String ORG = Objects.requireNonNull(binding.editTextOrgFragmentMyCard.getText()).toString();
        String TEL = Objects.requireNonNull(binding.editTextPhoneFragmentMyCard.getText()).toString();
        String EMAIL = Objects.requireNonNull(binding.editTextEmailFragmentMyCard.getText()).toString();
        String BDAY = "0";
        String ADR = Objects.requireNonNull(binding.editTextAddressFragmentMyCard.getText()).toString();
        String NOTE = binding.editTextContentMyCard.getText().toString();
        return "MECARD:N:"+N+";"+
                "ORG:"+ORG+";"+
                "TEL:"+TEL+";"+
                "EMAIL:"+EMAIL+";"+
                "BDAY:"+BDAY+";"+
                "ADR:"+ADR+";"+
                "NOTE:"+NOTE;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void requestContactPermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        generatePresenter.getAddressTest(requireActivity(), data.getData());
                    }
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getContact();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_contact_denied), Toast.LENGTH_SHORT).show();
                }
            });
}