package com.disruption.rxandroid_prac.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.disruption.rxandroid_prac.data.network.ServiceGenerator;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PublisherRepository {

    private static PublisherRepository instance;

    public static PublisherRepository getInstance(){
        if(instance == null){
            instance = new PublisherRepository();
        }
        return instance;
    }


    public static LiveData<ResponseBody> makeReactiveQuery(){
        return LiveDataReactiveStreams.fromPublisher(ServiceGenerator.getRequestApi()
                .makeQuery()
                .subscribeOn(Schedulers.io()));
    }
}
