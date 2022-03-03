package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentScannerQrBinding;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.MyClipboardManager;
import com.codescannerqr.generator.helpers.VibrationSoundHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.model.ResultList;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScannerQRFragment extends Fragment implements IGeneratedView {

    private FragmentScannerQrBinding binding;
    private boolean isFlashOn;
    private GeneratePresenter generatePresenter;
    private Bitmap bitmapNew;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScannerQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((MainActivity) requireActivity()).toolBarVisible(false);
        if (!Config.premium){
            ((MainActivity) requireActivity()).loadBanner();
        }
        isFlashOn = false;
        List<BarcodeFormat> barcodeFormatList = new ArrayList<>();

        barcodeFormatList.add(BarcodeFormat.AZTEC);
        barcodeFormatList.add(BarcodeFormat.CODABAR);
        barcodeFormatList.add(BarcodeFormat.CODE_39);
        barcodeFormatList.add(BarcodeFormat.CODE_93);
        barcodeFormatList.add(BarcodeFormat.CODE_128);
        barcodeFormatList.add(BarcodeFormat.DATA_MATRIX);
        barcodeFormatList.add(BarcodeFormat.EAN_8);
        barcodeFormatList.add(BarcodeFormat.EAN_13);
        barcodeFormatList.add(BarcodeFormat.ITF);
        barcodeFormatList.add(BarcodeFormat.MAXICODE);
        barcodeFormatList.add(BarcodeFormat.PDF_417);
        barcodeFormatList.add(BarcodeFormat.QR_CODE);
        barcodeFormatList.add(BarcodeFormat.RSS_14);
        barcodeFormatList.add(BarcodeFormat.RSS_EXPANDED);
        barcodeFormatList.add(BarcodeFormat.UPC_A);
        barcodeFormatList.add(BarcodeFormat.UPC_E);
        barcodeFormatList.add(BarcodeFormat.UPC_EAN_EXTENSION);

        Collection<BarcodeFormat> formats = new ArrayList<>(barcodeFormatList);

        binding.barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        binding.barcodeView.initializeFromIntent(requireActivity().getIntent());
        binding.barcodeView.decodeContinuous(callback);
        binding.barcodeView.setStatusText("");
        CameraSettings settings = binding.barcodeView.getBarcodeView().getCameraSettings();
        settings.setRequestedCameraId(0);
        //settings.setScanInverted(true);
        settings.setExposureEnabled(true);
        settings.setBarcodeSceneModeEnabled(true);
        settings.setAutoFocusEnabled(true);
        settings.setMeteringEnabled(true);
        settings.setContinuousFocusEnabled(true);
        settings.setFocusMode(CameraSettings.FocusMode.AUTO);
        binding.barcodeView.getBarcodeView().setCameraSettings(settings);

        if (!hasFlash()) {
            binding.imageViewScannerFlash.setImageResource(R.drawable.ic_bottom_scanner_flash_off);
        }
        binding.imageViewScannerGallery.setOnClickListener(v -> checkPerm());
        binding.imageViewScannerFlash.setOnClickListener(v -> switchFlashlight());
        binding.imageViewScannerFrontalCamera.setOnClickListener(v -> setFrontalCamera());

        return root;
    }


    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            Result result1 = result.getResult();
            ParsedResult parserdResult = ResultParser.parseResult(result1);
            resultQRCode(parserdResult, result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private void resultQRCode(ParsedResult parserdResult, String args){

        if (Config.settingsVibration){
            VibrationSoundHelpers.onVibration(requireActivity());
        }

        if (Config.settingsSound){
            VibrationSoundHelpers.onSound(requireActivity());
        }

        if (Config.settingsAutoCopy){
            MyClipboardManager.saveToClipboard(args, requireActivity());
        }

        generatePresenter.parserdResultQRCode(parserdResult, args, getActivity());
    }

    private void saveListCreateQR(String nameFragment, String codesString, int iconGragment,
                                  Bitmap bitmap) {
        ListHistoryHelpers.setListHistoryCreateAddItem(
                getActivity(),
                nameFragment,
                codesString,
                iconGragment,
                bitmap,
                "list_scan"
        );
    }

    @Override
    public void resultBitmapQR(Bitmap bitmap, String str) {
        bitmapNew = bitmap;
    }

    @Override
    public void resultParsedQRCode(String nameFragment, String defaultText, int iconFragment,
                                   List<ResultList> resultLists, String args, ParsedResult parsedResult) {
        try {
            generatePresenter.getBitmapQR(args, Config.weightQR, Config.weightQR, Color.BLACK);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (Config.settingsSaveHistory){
            saveListCreateQR(nameFragment, args, iconFragment, bitmapNew);

        }

        Bundle bundle = new Bundle();
        bundle.putString("nameFragment", nameFragment);
        bundle.putString("defaultText", defaultText);
        bundle.putString("argsQR", args);
        bundle.putString("fragmentAfter", "Scanner");
        bundle.putInt("iconFragment", iconFragment);
        bundle.putSerializable("keyFields", (Serializable) resultLists);

        ((MainActivity) requireActivity()).navController.navigate(
                R.id.action_navigation_scanner_to_resultFragment ,bundle);
    }

    @Override
    public void resultQRCodeParsedResult(ParsedResult parserdResult, String args) {
        resultQRCode(parserdResult, args);
    }

    private boolean hasFlash() {
        return requireActivity().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashOn) {
            isFlashOn = false;
            binding.imageViewScannerFlash.setImageResource(R.drawable.ic_bottom_scanner_flash_off);
            binding.barcodeView.setTorchOff();
        } else {
            isFlashOn = true;
            binding.imageViewScannerFlash.setImageResource(R.drawable.ic_bottom_scanner_flash_on);
            binding.barcodeView.setTorchOn();
        }
    }

    public void setFrontalCamera() {
        CameraSettings settings = binding.barcodeView.getBarcodeView().getCameraSettings();

        if (binding.barcodeView.getBarcodeView().isPreviewActive()) {
            binding.barcodeView.pause();
        }

        if (settings.getRequestedCameraId() == 0) {
            settings.setRequestedCameraId(1);
            binding.imageViewScannerFrontalCamera.setImageResource(R.drawable.ic_bottom_scanner_frontal_camera);
        } else {
            settings.setRequestedCameraId(0);
            binding.imageViewScannerFrontalCamera.setImageResource(R.drawable.ic_bottom_scanner_back_camera);
        }
        binding.barcodeView.getBarcodeView().setCameraSettings(settings);
        binding.barcodeView.resume();
    }

    public void requestFilePermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            getGalleryPhoto();
        }
        else{
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogFilePermission(getActivity(), this, "scanner");
        }
    }

    public void getGalleryPhoto(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        someActivityResultLauncher.launch(pickIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.barcodeView.pause();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    generatePresenter.requestPermissionsScanner(result,requireActivity());
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getGalleryPhoto();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_file_denied), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}