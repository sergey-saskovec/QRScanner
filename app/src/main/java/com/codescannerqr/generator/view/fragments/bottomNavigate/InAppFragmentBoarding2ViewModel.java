package com.codescannerqr.generator.view.fragments.bottomNavigate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class InAppFragmentBoarding2ViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> visibleFragment;

    public InAppFragmentBoarding2ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getData(InAppFragmentBoarding2 fragmentBoarding2) {
        if (visibleFragment == null) {
            visibleFragment = new MutableLiveData<>();

            loadData(fragmentBoarding2);


        }
        return visibleFragment;
    }

    public void setData(boolean bool){
        visibleFragment.postValue(bool);
    }

    private void loadData(InAppFragmentBoarding2 fragmentBoarding2) {
        visibleFragment.postValue(fragmentBoarding2.isVisible());

    }
}
