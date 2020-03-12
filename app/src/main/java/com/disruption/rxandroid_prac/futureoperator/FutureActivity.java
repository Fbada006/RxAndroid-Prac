package com.disruption.rxandroid_prac.futureoperator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.disruption.rxandroid_prac.R;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class FutureActivity extends AppCompatActivity {

    private static final String TAG = "FutureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        getData();
    }

    private void getData() {
        FutureViewModel viewModel = ViewModelProviders.of(this).get(FutureViewModel.class);
        try {
            viewModel.makeFutureQuery().get()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: called.");
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            Log.d(TAG, "onNext: got the response from server!");
                            try {
                                Log.d(TAG, "onNext: " + responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: called.");
                        }
                    });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
