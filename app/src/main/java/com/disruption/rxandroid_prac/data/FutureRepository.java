package com.disruption.rxandroid_prac.data;

import com.disruption.rxandroid_prac.data.network.ServiceGenerator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class FutureRepository {

    private static FutureRepository instance;

    public static FutureRepository getInstance() {
        if (instance == null) {
            instance = new FutureRepository();
        }
        return instance;
    }

    public Future<Observable<ResponseBody>> makeFutureQuery() {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<ResponseBody>> myNetworkCallable = () ->
                ServiceGenerator.getRequestApi().makeObservableQuery();


        return new Future<Observable<ResponseBody>>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (mayInterruptIfRunning) {
                    executor.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<ResponseBody> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<ResponseBody> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeout, unit);
            }
        };

    }

}
