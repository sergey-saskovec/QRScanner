package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.databinding.FragmentGenerateBottomBinding;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.MainRecyclerViewAdapter;
import com.codescannerqr.generator.model.Section;

import java.util.ArrayList;
import java.util.List;


public class GenerateCodeFragment extends Fragment {

    private FragmentGenerateBottomBinding binding;
    private final List<Section> sectionList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding =
                FragmentGenerateBottomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createList();
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(sectionList, getActivity());
        binding.recyclerGenerate.setAdapter(adapter);
        return root;
    }

    private void createList(){
        sectionList.add(new Section("", Config.listSectionOneItems));
        sectionList.add(new Section(getString(R.string.header2), Config.listSectionOneItems2));
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.tools));
        ((MainActivity) requireActivity()).visibleButtonBack(false, false);
        ((MainActivity) requireActivity()).toolBarVisible(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}