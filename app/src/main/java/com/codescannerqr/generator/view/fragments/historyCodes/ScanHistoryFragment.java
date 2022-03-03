package com.codescannerqr.generator.view.fragments.historyCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.result.ParsedResult;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.HistoryRecyclerViewAdapter2;
import com.codescannerqr.generator.databinding.FragmentScanHistoryBinding;
import com.codescannerqr.generator.helpers.BitmapHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.model.ResultList;
import com.codescannerqr.generator.presenter.GeneratePresenter;
import com.codescannerqr.generator.view.IGeneratedView;
import com.codescannerqr.generator.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class ScanHistoryFragment extends Fragment implements IGeneratedView {

    private FragmentScanHistoryBinding binding;
    private ItemViewModel itemViewModel;
    private GeneratePresenter generatePresenter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScanHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generatePresenter = new GeneratePresenter(this);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        setView();
        return root;
    }

    private void setView() {
        String key = "list_scan";
        if (ListHistoryHelpers.getListHistoryCreate(getActivity(), key).size() == 0) {
            binding.constraintNoData.setVisibility(View.VISIBLE);
            binding.recyclerViewHistoryScan.setVisibility(View.GONE);
        } else {
            binding.constraintNoData.setVisibility(View.GONE);
            binding.recyclerViewHistoryScan.setVisibility(View.VISIBLE);
            List<HistoryItem> historyItemList = ListHistoryHelpers.getListHistoryCreate(getActivity(), key);

            HistoryRecyclerViewAdapter2 childRecyclerAdapter = new HistoryRecyclerViewAdapter2(
                    ListHistoryHelpers.reverseList(historyItemList),
                    binding.recyclerViewHistoryScan,
                    getActivity(),
                    key,
                    itemViewModel);

            binding.recyclerViewHistoryScan.setLayoutManager(new LinearLayoutManager(
                    getContext(),
                    LinearLayoutManager.VERTICAL,
                    false));

            childRecyclerAdapter.setOnItemClickListener((view, position, historyItem) ->
                    generatePresenter.parserdResultQRCode(
                            BitmapHelpers.getParsedResult2(BitmapHelpers.decodeByteToBitmap(
                                    historyItem.getArrayQRCode())),
                            historyItem.getUrlHistory(),
                            getActivity()));

            binding.recyclerViewHistoryScan.setAdapter(childRecyclerAdapter);

        }
    }

    @Override
    public void resultParsedQRCode(String nameFragment, String defaultText, int iconFragment,
                                   List<ResultList> resultLists, String args,
                                   ParsedResult parsedResult) {
        Bundle bundle = new Bundle();
        bundle.putString("nameFragment", nameFragment);
        bundle.putString("defaultText", defaultText);
        bundle.putString("argsQR", args);
        bundle.putInt("iconFragment", iconFragment);
        bundle.putSerializable("keyFields", (Serializable) resultLists);

        ((MainActivity) requireActivity()).navController.navigate(
                R.id.action_navigation_history_to_resultFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}