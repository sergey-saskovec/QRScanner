package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.codescannerqr.generator.adapter.MyTabPagerAdapter2;
import com.codescannerqr.generator.databinding.FragmentHistoryBottomBinding;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.R;
import java.util.Objects;

public class HistoryBottomFragment extends Fragment implements IGeneratedView {

    private FragmentHistoryBottomBinding binding;
    private GeneratePresenter generatePresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBottomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.history));
        ((MainActivity) requireActivity()).visibleButtonBack(false, false);
        ((MainActivity) requireActivity()).toolBarVisible(true);

        MyTabPagerAdapter2 tabPagerAdapter2 = new MyTabPagerAdapter2(
                getChildFragmentManager(),
                getLifecycle()
        );

        binding.tabPager2.setAdapter(tabPagerAdapter2);

        new TabLayoutMediator(binding.tabLayout, binding.tabPager2,
                (tab, position) -> tab.setText(requireActivity().getString(
                        tabPagerAdapter2.getNameFragment().get(position)))
        ).attach();

        binding.fabExportHistoryCSV.setOnClickListener(v -> checkPerm());

        return root;
    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            createFile();
        }
        else{
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogFilePermission(
                    getActivity(), this,
                    "history_bottom"
            );
        }
    }

    public void requestFilePermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    createFile();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_file_denied), Toast.LENGTH_SHORT).show();
                }
            });

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "EXPORT_HISTORY.csv");

        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = Objects.requireNonNull(result.getData()).getData();
                    generatePresenter.saveCSVFile(requireActivity(), uri);

                }
            });

    @Override
    public void resultExportHistory(String result) {
        Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}