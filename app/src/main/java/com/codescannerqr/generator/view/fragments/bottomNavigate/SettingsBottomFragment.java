package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blongho.country_data.World;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.adapter.SettingsMainRecyclerViewAdapter;
import com.codescannerqr.generator.databinding.FragmentSettingsBottomBinding;
import com.codescannerqr.generator.helpers.LocaleHelper;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.model.SectionSettings;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SettingsBottomFragment extends Fragment {

    private FragmentSettingsBottomBinding binding;
    private final List<SectionSettings> sectionList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBottomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        World.init(requireActivity().getApplicationContext());
        ((MainActivity) requireActivity()).setTextViewToolbar(getString(R.string.settings));
        ((MainActivity) requireActivity()).visibleButtonBack(false, false);
        ((MainActivity) requireActivity()).toolBarVisible(true);
        SettingsHelpers.setSettings(getActivity());
        createList();
        SettingsMainRecyclerViewAdapter adapter = new SettingsMainRecyclerViewAdapter(sectionList,
                getActivity(), this);
        binding.recyclerViewMainSettings.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewMainSettings.setAdapter(adapter);
        return root;
    }

    private void createList(){
        String currentLanguage = Locale.getDefault().getLanguage();
        final int flag = World.getFlagOf(currentLanguage);
        Config.listSectionOneItemsSettings.get(0).setImage(flag);
        sectionList.add(new SectionSettings(getString(R.string.display_language), Config.listSectionOneItemsSettings));
        sectionList.add(new SectionSettings(getString(R.string.settings), Config.listSectionTwoItemsSettings));
        sectionList.add(new SectionSettings(getString(R.string.general), Config.listSectionThreeItemsSettings));
    }

    public void updateViews(String languageCode){
        LocaleHelper.setLocaleNew(requireActivity(), languageCode);
        ((MainActivity)requireActivity()).navController.popBackStack();
        ((MainActivity)requireActivity()).navController.navigate(R.id.navigation_settings);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}