package com.disruption.rxandroid_prac.publisheroperator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.disruption.rxandroid_prac.data.PublisherRepository;

import okhttp3.ResponseBody;

public class PublisherViewModel extends ViewModel {

    private PublisherRepository publisherRepository;

    public PublisherViewModel() {
        publisherRepository = PublisherRepository.getInstance();
    }

    public LiveData<ResponseBody> makeQuery(){
        return PublisherRepository.makeReactiveQuery();
    }
}
