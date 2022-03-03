package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.zxing.WriterException;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.databinding.FragmentStyleBottomBinding;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.StyleMainRecyclerAdapter;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.model.ColorGradinet;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.model.StyleItem;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class StyleCodeBottomFragment extends Fragment implements IGeneratedView {

    private FragmentStyleBottomBinding binding;
    private GeneratePresenter generatePresenter;
    private Bitmap bitmapNew;
    private Bitmap bitmapSingleColor;
    private Bitmap bitmapGradientColor;
    private List<Bitmap> bitmapList;
    private int defColor = Color.BLACK;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStyleBottomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.style));
        ((MainActivity) requireActivity()).visibleButtonBack(false, false);
        ((MainActivity) requireActivity()).toolBarVisible(true);

        if (!checkListEmpty()){
            binding.recyclerViewStyleMain.setVisibility(View.VISIBLE);
            binding.constraintNoData.setVisibility(View.GONE);
            setRecycler();
        }
        else{
            binding.constraintNoData.setVisibility(View.VISIBLE);
            binding.recyclerViewStyleMain.setVisibility(View.GONE);
        }

        binding.textViewCreateQR.setOnClickListener(v -> (
                (MainActivity) requireActivity()).navController.navigate(R.id.navigation_generate));

        return root;
    }

    private void setRecycler() {
        List<StyleItem> styleItemList = createListStyleMain();
        StyleMainRecyclerAdapter styleMainRecyclerAdapter = new StyleMainRecyclerAdapter(
                styleItemList,
                getActivity()
                );

        binding.recyclerViewStyleMain.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                true));

        binding.recyclerViewStyleMain.setAdapter(styleMainRecyclerAdapter);
        styleMainRecyclerAdapter.notifyItemRangeInserted(0, styleItemList.size());
        binding.recyclerViewStyleMain.scrollToPosition(styleItemList.size() - 1);
    }

    private List<StyleItem> createListStyleMain() {
        List<HistoryItem> listCreate =  ListHistoryHelpers.getListHistoryCreate(
                getActivity(),
                "list_create"
        );
        List<StyleItem> styleItemList = new ArrayList<>();
        for (int i = 0; i<listCreate.size(); i++){
            styleItemList.add(new StyleItem(
                    listCreate.get(i).getIconHistory(),
                    listCreate.get(i).getTitleHistory(),
                    createBitmapList(listCreate.get(i).getUrlHistory()),
                    listCreate.get(i).getUrlHistory()));
        }
        return styleItemList;
    }

    private boolean checkListEmpty(){
        return ListHistoryHelpers.getListHistoryCreate(getActivity(), "list_create").size() == 0;
    }

    private List<Bitmap> createBitmapList(String urlHistory) {
        bitmapList = new ArrayList<>();

        try {
            //generate default
            generatePresenter.getBitmapQR(urlHistory, Config.weightQR, Config.weightQR, Color.BLACK);
            //generate random single color
            generatePresenter.getBitmapQR(urlHistory, Config.weightQR, Config.weightQR,
                    getRandomColorDefColor());
            generatePresenter.getBitmapQR(urlHistory, Config.weightQR, Config.weightQR,
                    getRandomColorDefColor());
            //generate random gradient color
            generatePresenter.setGradient(bitmapNew, getRandomColor(), getRandomColor(), defColor);
            generatePresenter.setGradient(bitmapNew, getRandomColor(), getRandomColor(), defColor);
            //generate set border
            ColorGradinet colorGradinet = Config.listColorGradientStyle.get(getRandomPosition());
            generatePresenter.setBorderBitmap(bitmapSingleColor, colorGradinet.getColor1(),
                    colorGradinet.getColor2(), requireActivity());
            ColorGradinet colorGradinet2 = Config.listColorGradientStyle.get(getRandomPosition());
            generatePresenter.setBorderBitmap(bitmapGradientColor, colorGradinet2.getColor1(),
                    colorGradinet2.getColor2(), requireActivity());
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Collections.shuffle(bitmapList);

        return bitmapList;
    }

    private int getRandomColorDefColor(){
        Random rnd = new Random();
        defColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));
        return defColor;
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256));
    }

    private int getRandomPosition(){
        Random rnd = new Random();
        return rnd.nextInt(5) + 1;
    }

    @Override
    public void resultSingleColorBitmap(Bitmap bitmap) {
        bitmapNew = bitmap;
        bitmapSingleColor = bitmap;
        bitmapList.add(bitmap);
    }

    @Override
    public void resultBitmapQR(Bitmap bitmap, String str) {
        bitmapNew = bitmap;
        bitmapList.add(bitmap);
    }

    @Override
    public void resultGradientBitmap(Bitmap bitmap) {
        bitmapGradientColor = bitmap;
        bitmapList.add(bitmap);
    }

    @Override
    public void resultBorderBitmap(Bitmap bitmap) {
        bitmapList.add(bitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}