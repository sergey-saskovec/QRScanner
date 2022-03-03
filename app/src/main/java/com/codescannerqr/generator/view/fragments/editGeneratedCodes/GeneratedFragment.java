package com.codescannerqr.generator.view.fragments.editGeneratedCodes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.zxing.WriterException;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.FragmentGeneratedBinding;
import com.codescannerqr.generator.helpers.BitmapHelpers;
import com.codescannerqr.generator.helpers.DateTimeHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;
import org.jetbrains.annotations.NotNull;

public class GeneratedFragment extends BaseEditFragment implements IGeneratedView, InterDismiss {

    private FragmentGeneratedBinding binding;
    private GeneratePresenter generatePresenter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGeneratedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.generated_qr));
        ((GenerateCodeActivity) requireActivity()).iconToolbarGone();
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideKeyboard(requireActivity());
        setView();
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setView() {
        assert getArguments() != null;
        int argIcon = getArguments().getInt("argIcon");
        String argTitleFragment = getArguments().getString("argTitle");
        String argValue = getArguments().getString("arg");
        binding.imageViewFragmentGenerated.setImageResource(argIcon);
        binding.textViewNameFragmentGenerated.setText(argTitleFragment);
        binding.textViewTimeGenerated.setText(DateTimeHelpers.getDateTimeNow("HH:mm") + " " +
                DateTimeHelpers.getDateTimeNow("dd.MM.yyyy"));

        try {
            if (argTitleFragment.equals(getString(R.string.product_code))){
                binding.cardViewStyleGenerated.setVisibility(View.GONE);
                generatePresenter.getBitmapQR128(argValue, 800, Config.weightQR, Color.BLACK);
            }
            else{
                binding.cardViewStyleGenerated.setVisibility(View.VISIBLE);
                generatePresenter.getBitmapQR(argValue, Config.weightQR, Config.weightQR, Color.BLACK);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }


        binding.buttonDoneGenerated.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), getString(R.string.toast_done), Toast.LENGTH_SHORT).show();
            saveListCreateQR();
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                if (SettingsHelpers.getFirstGeneratedQR(requireActivity())){
                    SettingsHelpers.saveFirstGeneratedQR(requireActivity());
                    ViewDialog.showDialogRateUs(requireActivity(), this, "generated");
                }
                else{
                    showInterDone();
                    //requireActivity().finish();
                }
            }
            else{
                ViewDialog dialogNotPermissionFile = new ViewDialog();
                dialogNotPermissionFile.showDialogFilePermission(
                        getActivity(),
                        this,
                        "generated2"
                );
            }
        });

        binding.cardViewStyleGenerated.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) binding.imageViewQRCodeGenerated.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            byte[] arrayBitmap = BitmapHelpers.encodeBitmapToByte(bitmap);
            setStyleFragment(arrayBitmap);
        });

        binding.cardViewShareGenerated.setOnClickListener(v -> generatePresenter.
                shareBitmap(((BitmapDrawable)binding.imageViewQRCodeGenerated.
                getDrawable()).getBitmap(), requireActivity()));

        binding.cardViewPrintGenerated.setOnClickListener(v -> {
            PrintHelper photoPrinter = new PrintHelper(requireActivity());
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            Bitmap bitmapPrint = ((BitmapDrawable)binding.imageViewQRCodeGenerated.
                    getDrawable()).getBitmap();
            photoPrinter.printBitmap("droids.jpg - test print", bitmapPrint);
        });

        binding.cardViewSaveGenerated.setOnClickListener(v -> checkPerm());

    }

    private void showInterDone() {

        if (InterstitialManager.getLoaded() && !Config.premium){
            InterstitialManager.showInter(requireActivity(), new InterDismiss() {
                @Override
                public void interDismiss(int count) {
                    if (count == 1){
                        ViewDialog.showDialogRemoveAds(requireActivity());
                    }
                    else {
                        requireActivity().finish();
                    }
                }
            });
        }
        else{
            requireActivity().finish();
        }
    }

    public void saveListCreateQR() {
        assert getArguments() != null;
        ListHistoryHelpers.setListHistoryCreateAddItem(
                getActivity(),
                getArguments().getString("argTitle"),
                getArguments().getString("arg"),
                getArguments().getInt("argIcon"),
                ((BitmapDrawable) binding.imageViewQRCodeGenerated.getDrawable()).getBitmap(),
                "list_create");

    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            saveImage();
        }
        else{
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogFilePermission(
                    getActivity(),
                    this,
                    "generated"
            );
        }
    }

    private void saveImage(){
        Bitmap finalBitmap = ((BitmapDrawable)binding.imageViewQRCodeGenerated.getDrawable()).getBitmap();
        String fname = "QRCODE_"+DateTimeHelpers.getDateTimeNow("ddMMyyyyhhmm");
        generatePresenter.savedImage(finalBitmap, fname, getActivity());

    }

    @Override
    public void resultBitmapQR(Bitmap bitmap, String str) {
        binding.imageViewQRCodeGenerated.setImageBitmap(bitmap);
    }

    @Override
    public void resultBitmapQR128(Bitmap bitmap) {
        binding.imageViewQRCodeGenerated.setImageBitmap(bitmap);
    }

    private void setStyleFragment(byte[] byteArray){
        Bundle bundle = new Bundle();
        bundle.putByteArray("argArrayBitmap", byteArray);
        bundle.putString("argText", requireArguments().getString("arg"));
        bundle.putString("argTitle", requireArguments().getString("argTitle"));
        bundle.putInt("argIcon", requireArguments().getInt("argIcon"));

        ((GenerateCodeActivity) requireActivity()).navControllerGenerate.navigate(
                R.id.action_generatedFragment_to_styleEditFragment ,bundle);
    }

    @Override
    public void resultUriBitmapShare(Uri uri) {
        BitmapHelpers.shareBitmap(uri, getActivity());
    }

    public void requestFilePermission(String stringPermission, String str) {
        if (str.equals("generated")){
            requestPermissionLauncher.launch(
                    stringPermission);
        }
        else{
            requestPermissionLauncher2.launch(
                    stringPermission);
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveImage();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_contact_denied), Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher2 =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    saveImage();
                    if (SettingsHelpers.getFirstGeneratedQR(requireActivity())){
                        SettingsHelpers.saveFirstGeneratedQR(requireActivity());
                        ViewDialog.showDialogRateUs(requireActivity(), this, "generated");
                    }
                    else{
                        showInterDone();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_contact_denied), Toast.LENGTH_SHORT).show();
                }
            });

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}