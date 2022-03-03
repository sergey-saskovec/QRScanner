package com.codescannerqr.generator.view.fragments.editGeneratedCodes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.zxing.WriterException;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.LogoStyleEditAdapter;
import com.codescannerqr.generator.ads.InterDismiss;
import com.codescannerqr.generator.ads.InterstitialManager;
import com.codescannerqr.generator.databinding.FragmentStyleEditBinding;
import com.codescannerqr.generator.helpers.BitmapHelpers;
import com.codescannerqr.generator.helpers.DateTimeHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.helpers.ViewDialog;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;

import org.jetbrains.annotations.NotNull;

public class StyleEditFragment extends StyleBaseEditFragment implements IGeneratedView {

    private FragmentStyleEditBinding binding;
    private GeneratePresenter generatePresenter;
    private Bitmap bitmapNew;
    private Bitmap bitmapNewGradient;
    private Bitmap bitmapNewBorder;
    String argText = "";
    private boolean boolColorLogo;
    private boolean boolColorBG;
    private int positionLogo;
    int color1;
    int color2;
    private int defColor = Color.BLACK;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStyleEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        setView();
        color1 = ContextCompat.getColor(requireActivity(), R.color.orange);
        color2 = ContextCompat.getColor(requireActivity(), R.color.orange);
        return root;
    }

    private void setView() {
        if (getArguments() != null) {
            argText = getArguments().getString("argText");
            Bitmap bitmap = BitmapHelpers.decodeByteToBitmap(getArguments().getByteArray("argArrayBitmap"));
            bitmapNew = bitmap;
            binding.imageViewQRCodeStyleEdit.setImageBitmap(bitmap);
        }
        setSeekBar("single");

        binding.constrDefaultStyle.setOnClickListener(v -> {
            setBgLineMenu(binding.constrDefaultStyle);
            try {
                generatePresenter.getBitmapQR(argText, Config.weightQR, Config.weightQR, Color.BLACK);
                binding.imageViewLogoQRCodeStyleEdit.setVisibility(View.GONE);
                binding.imageViewBGQRCodeStyleEdit.setVisibility(View.INVISIBLE);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });

        binding.constrColorStyle.setOnClickListener(v -> setBgLineMenu(binding.constrColorStyle));
        binding.constrLogoStyle.setOnClickListener(v -> {
            setBgLineMenu(binding.constrLogoStyle);
            setLogoConstraint();
        });

        binding.constrBgStyle.setOnClickListener(v -> {
            setBgLineMenu(binding.constrBgStyle);
            setBorderConstraint();
        });
        binding.textViewSingleColorEditStyle.setOnClickListener(v ->
                setSingleGradientColor(binding.textViewSingleColorEditStyle));
        binding.textViewGradientColorEditStyle.setOnClickListener(v ->
                setSingleGradientColor(binding.textViewGradientColorEditStyle));

        binding.buttonSaveStyleEdit.setOnClickListener(v -> {
            showInterDone();
        });
    }

    private void showInterDone() {
        if (InterstitialManager.getLoaded() && !Config.premium){
            InterstitialManager.showInter(requireActivity(), new InterDismiss() {
                @Override
                public void interDismiss(int count) {
                    nextActionInter();
                }
            });
        }
        else{
            nextActionInter();
        }


    }

    private void nextActionInter(){
        if (SettingsHelpers.getFirstGeneratedQR(requireActivity())){
            SettingsHelpers.saveFirstGeneratedQR(requireActivity());
            ViewDialog.showDialogRateUs(requireActivity(), this, "style");
        }
        else{
            checkPerm();
        }
    }

    private void setSeekBar(String singleOrGradient) {
        setSeekBarUniverse2(binding.colorSeekBar, singleOrGradient);
        setSeekBarUniverse2(binding.colorSeekBar2, singleOrGradient);
    }

    private void setSeekBarUniverse2(ColorSeekBar seekBar, String singleOrGradient){
        seekBar.setOnColorChangeListener(i -> {

            if (singleOrGradient.equals("single")){
                defColor = i;
                try {
                    generatePresenter.getBitmapQR(argText, Config.weightQR, Config.weightQR, defColor);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            else{
                if (seekBar == binding.colorSeekBar){
                    color1 = i;
                }
                else{
                    color2 = i;
                }
                generatePresenter.setGradient(bitmapNew, color1, color2, defColor);


            }

        });
    }

    private void setBgLineMenu(ConstraintLayout constraintLayoutEnter) {
        ConstraintLayout[] constraintLayouts = new ConstraintLayout[]{
                binding.constrDefaultStyle,
                binding.constrColorStyle,
                binding.constrLogoStyle,
                binding.constrBgStyle};
        ConstraintLayout[] constraintLayouts1 = new ConstraintLayout[]{
                binding.tabsDefaultEditStyle,
                binding.tabsColorEditStyle,
                binding.tabsLogoEditStyle,
                binding.tabsBGEditStyle
        };

        for (int i = 0; i <constraintLayouts.length; i++){
            if (constraintLayoutEnter == constraintLayouts[i]){
                constraintLayoutEnter.setBackgroundResource(R.drawable.back_cardview_border);
                constraintLayouts1[i].setVisibility(View.VISIBLE);
            }
            else{
                constraintLayouts[i].setBackgroundResource(R.drawable.back_cardview_not_border);
                constraintLayouts1[i].setVisibility(View.GONE);
            }
        }
    }

    private void setSingleGradientColor(TextView textViewEnter) {
        if (textViewEnter == binding.textViewSingleColorEditStyle){
            binding.textViewSingleColorEditStyle.setTextColor(Color.BLACK);
            binding.viewSingleColor.setVisibility(View.VISIBLE);
            binding.colorSeekBar.setVisibility(View.VISIBLE);
            binding.colorSeekBar2.setVisibility(View.GONE);
            binding.textViewGradientColorEditStyle.setTextColor(requireActivity().getColor(R.color.gray));
            binding.viewGradientColor.setVisibility(View.GONE);
            setSeekBar("single");
        }
        else{
            binding.textViewSingleColorEditStyle.setTextColor(requireActivity().getColor(R.color.gray));
            binding.viewSingleColor.setVisibility(View.GONE);
            binding.colorSeekBar.setVisibility(View.VISIBLE);
            binding.colorSeekBar2.setVisibility(View.VISIBLE);
            binding.textViewGradientColorEditStyle.setTextColor(Color.BLACK);
            binding.viewGradientColor.setVisibility(View.VISIBLE);
            setSeekBar("gradient");
        }
    }

    private void setLogoConstraint() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LogoStyleEditAdapter adapter = new LogoStyleEditAdapter(Config.listLogo,
                "logo");
        binding.recyclerViewLogo.setLayoutManager(layoutManager);
        binding.recyclerViewLogo.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            if (position == 0){
                binding.imageViewQRCodeStyleEdit.setImageBitmap(checkBitmapGradient());
                binding.imageViewLogoQRCodeStyleEdit.setVisibility(View.GONE);
            }
            else{
                boolColorLogo = true;
                binding.imageViewLogoQRCodeStyleEdit.setVisibility(View.VISIBLE);
                positionLogo = Config.listLogo.get(position);
                binding.imageViewLogoQRCodeStyleEdit.setImageResource(Config.listLogo.get(position));
            }
        });
    }

    private void setBorderConstraint() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LogoStyleEditAdapter adapter = new LogoStyleEditAdapter(Config.listBorder,
                "BG");
        binding.recyclerViewBG.setLayoutManager(layoutManager);
        binding.recyclerViewBG.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            boolColorBG = true;
            if (position == 0){
                binding.imageViewQRCodeStyleEdit.setImageBitmap(checkBitmapGradient());
                binding.imageViewBGQRCodeStyleEdit.setVisibility(View.INVISIBLE);
            }
            else{
                color1 = Config.listColorGradientStyle.get(position).getColor1();
                color2 = Config.listColorGradientStyle.get(position).getColor2();
                binding.imageViewBGQRCodeStyleEdit.setVisibility(View.VISIBLE);
                binding.imageViewBGQRCodeStyleEdit.setImageResource(Config.listBorder.get(position));

            }
        });
    }

    private Bitmap checkBitmapGradient() {
        if (bitmapNewGradient != null){
            return bitmapNewGradient;
        }

        else{
            return bitmapNew;
        }
    }


    @Override
    public void resultBitmapQR(Bitmap bitmap, String str) {
        bitmapNew = bitmap;
        bitmapNewGradient = null;
        bitmapNewBorder = null;
        defColor = Color.BLACK;
        binding.imageViewQRCodeStyleEdit.setImageBitmap(bitmap);
    }

    private void generateAllStyle(){
        if (boolColorLogo){
            if (boolColorBG){
                generatePresenter.setBorderBitmap(checkBitmapGradient(), color1, color2,requireActivity());
                generatePresenter.overlayBitmap(bitmapNewBorder,getContext(), positionLogo);
            }
            else{
                generatePresenter.overlayBitmap(checkBitmapGradient(),getContext(), positionLogo);
            }
        }
        else if (boolColorBG){
            generatePresenter.setBorderBitmap(checkBitmapGradient(), color1, color2,requireActivity());
        }
        else{
            Bitmap bitmap = ((BitmapDrawable) binding.imageViewQRCodeStyleEdit.getDrawable()).getBitmap();
            saveImage(bitmap);
        }
    }

    public void checkPerm() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            generateAllStyle();
        }
        else{
            ViewDialog dialogNotPermissionFile = new ViewDialog();
            dialogNotPermissionFile.showDialogFilePermission(getActivity(), this, "style");
        }
    }

    private void saveImage(Bitmap bitmap){
        String fname;
        fname = "QRCODE_"+ DateTimeHelpers.getDateTimeNow("ddMMyyyyhhmm");
        generatePresenter.savedImage(bitmap, fname, getActivity());

        assert getArguments() != null;
        ListHistoryHelpers.setListHistoryCreateAddItem(
                getActivity(),
                getArguments().getString("argTitle"),
                argText,
                getArguments().getInt("argIcon"),
                bitmap,
                "list_create");

        requireActivity().finish();

    }

    @Override
    public void resultOverlayBitmap(Bitmap bitmap) {
        saveImage(bitmap);
    }

    @Override
    public void resultBorderBitmap(Bitmap bitmap) {
        bitmapNewBorder = bitmap;
        if (!boolColorLogo){
            saveImage(bitmap);
        }
    }

    @Override
    public void resultGradientBitmap(Bitmap bitmap) {
        bitmapNewGradient = bitmap;
        binding.imageViewQRCodeStyleEdit.setImageBitmap(bitmap);
    }

    @Override
    public void resultSingleColorBitmap(Bitmap bitmap) {
        bitmapNewGradient = bitmap;
        bitmapNew = bitmap;
        binding.imageViewQRCodeStyleEdit.setImageBitmap(bitmap);
    }

    public void requestFilePermission(String stringPermission) {
        requestPermissionLauncher.launch(
                stringPermission);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    generateAllStyle();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.perm_contact_denied), Toast.LENGTH_SHORT).show();
                }
            });

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}