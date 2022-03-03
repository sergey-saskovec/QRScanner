package com.codescannerqr.generator.view.fragments.historyCodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codescannerqr.generator.model.HistoryItem;

import java.util.List;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<List<HistoryItem>> selectedItem = new MutableLiveData<>();

    public void selectItem(List<HistoryItem> items) {
        selectedItem.setValue(items);
    }

    public LiveData<List<HistoryItem>> getSelectedItem() {
        return selectedItem;
    }
}
