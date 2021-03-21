package com.example.bookshare.ui.mycart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MycartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MycartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my cart fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}