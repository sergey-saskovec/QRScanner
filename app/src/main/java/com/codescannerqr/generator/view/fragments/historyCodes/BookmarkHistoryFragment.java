package com.codescannerqr.generator.view.fragments.historyCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.adapter.HistoryRecyclerViewAdapter2;
import com.codescannerqr.generator.databinding.FragmentBookmarkHistoryBinding;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.model.HistoryItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookmarkHistoryFragment extends Fragment {

    private FragmentBookmarkHistoryBinding binding;
    private HistoryRecyclerViewAdapter2 childRecyclerAdapter;
    private ItemViewModel itemViewModel;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookmarkHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);


        childRecyclerAdapter = new HistoryRecyclerViewAdapter2(
                ListHistoryHelpers.reverseList(ListHistoryHelpers.getListHistoryCreate(
                        getActivity(),
                        "list_bookmark"
                        )),
                binding.recyclerViewHistoryBookmark,
                getActivity(),
                "list_bookmark",
                itemViewModel);

        binding.recyclerViewHistoryBookmark.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));

        if (Config.premium){
            binding.recyclerViewHistoryBookmark.setAdapter(childRecyclerAdapter);
        }
        else{
            binding.recyclerViewHistoryBookmark.setAdapter(childRecyclerAdapter);
        }

        setView();

        return root;
    }

    private void setView() {
        String key = "list_bookmark";
        if (ListHistoryHelpers.getListHistoryCreate(getActivity(), key).size() == 0) {
            binding.constraintNoData.setVisibility(View.VISIBLE);
            binding.recyclerViewHistoryBookmark.setVisibility(View.GONE);
        } else {
            binding.constraintNoData.setVisibility(View.GONE);
            binding.recyclerViewHistoryBookmark.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        itemViewModel.getSelectedItem().observe(getViewLifecycleOwner(), list -> {

            try {
                if (list == null || list.size() == 0){
                    binding.constraintNoData.setVisibility(View.VISIBLE);
                    binding.recyclerViewHistoryBookmark.setVisibility(View.GONE);
                }
                else{
                    binding.constraintNoData.setVisibility(View.GONE);
                    binding.recyclerViewHistoryBookmark.setVisibility(View.VISIBLE);

                    childRecyclerAdapter.setItems(ListHistoryHelpers.reverseList(list));
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}