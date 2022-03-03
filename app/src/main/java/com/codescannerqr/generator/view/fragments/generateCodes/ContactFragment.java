package com.codescannerqr.generator.view.fragments.generateCodes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentContactBinding;
import com.codescannerqr.generator.helpers.EmailHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.model.Contact;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ContactFragment extends Fragment implements
        ActivityCompat.OnRequestPermissionsResultCallback, IGeneratedView {

    private FragmentContactBinding binding;
    private GeneratePresenter generatePresenter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.contact));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_contact);
        binding.textInputLayoutEditContact.setEndIconOnClickListener(v -> getContact());

        binding.buttonCreateContact.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!Objects.requireNonNull(binding.editTextNameFragmentContact.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(binding.editTextPhoneNumberFragmentContact.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(binding.editTextEmailFragmentContact.getText()).toString().isEmpty() &&
                    EmailHelpers.isValidEmail(binding.editTextEmailFragmentContact.getText())) {


                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.contact));
                bundle.putInt("argIcon", R.drawable.ic_gen_contact);

                ((GenerateCodeActivity) requireActivity()).navControllerGenerate.
                        navigate(R.id.action_contactFragment_to_generatedFragment, bundle);
            } else {
                Toast.makeText(getActivity(), getString(R.string.field_empty), Toast.LENGTH_LONG).show();
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
            dialogNotPermissionContact.showDialogContactPermission(getActivity(),
                    this, "contact");
        }
    }


    @Override
    public void resultFullContact(Contact contact) {
        binding.editTextNameFragmentContact.setText(contact.getName());
        binding.editTextPhoneNumberFragmentContact.setText(contact.getPhone());
        binding.editTextEmailFragmentContact.setText(contact.getEmail());
    }

    private String createDataInBundle() {
        //CONTACT
        String n = Objects.requireNonNull(binding.editTextNameFragmentContact.getText()).toString();
        String TEL = Objects.requireNonNull(binding.editTextPhoneNumberFragmentContact.getText()).toString();
        String EMAIL = Objects.requireNonNull(binding.editTextEmailFragmentContact.getText()).toString();
        return "MECARD:N:" + n + ";TEL:" + TEL + ";EMAIL:" + EMAIL + ";;";
    }

    @Override
    public void onDestroyView() {
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