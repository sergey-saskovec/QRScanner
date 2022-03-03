package com.codescannerqr.generator.view.fragments.styleCodes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.FragmentStylePreviewBinding;
import com.codescannerqr.generator.helpers.BitmapHelpers;
import com.codescannerqr.generator.helpers.DateTimeHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

public class StylePreviewFragment extends Fragment implements IGeneratedView {

    private FragmentStylePreviewBinding binding;
    private GeneratePresenter generatePresenter;
    private Bitmap bitmap;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStylePreviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.style_preview));
        ((MainActivity) requireActivity()).visibleButtonBack(true, false);
        setView();
        return root;
    }

    private void setView() {
        if (getArguments() != null) {
            bitmap = getArguments().getParcelable("imageBitmap");
            binding.imageViewStylePreview.setImageBitmap(bitmap);
        }

        binding.cardViewSaveStylePreview.setOnClickListener(v -> showInterDone());
        binding.cardViewShareStylePreview.setOnClickListener(v ->
                generatePresenter.shareBitmap(bitmap, requireActivity()));
    }

    private void showInterDone() {
        if (InterstitialManager.getLoaded() && !Config.premium) {
            InterstitialManager.showInter(requireActivity(), new InterDismiss() {
                @Override
                public void interDismiss(int count) {
                    checkPerm();
                }
            });
        } else {
            checkPerm();
        }


    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogFilePermission(requireActivity(), this,
                    "stylePreview");
        }
    }

    private void saveImage() {
        String fname;
        Bitmap finalBitmap = ((BitmapDrawable) binding.imageViewStylePreview.getDrawable()).getBitmap();
        fname = "QRCODE_" + DateTimeHelpers.getDateTimeNow("ddMMyyyyhhmm");
        generatePresenter.savedImage(finalBitmap, fname, getActivity());

    }

    @Override
    public void resultUriBitmapShare(Uri uri) {
        BitmapHelpers.shareBitmap(uri, getActivity());
    }

    public void requestFilePermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveImage();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_contact_denied), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}