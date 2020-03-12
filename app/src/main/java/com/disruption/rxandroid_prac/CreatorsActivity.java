package com.disruption.rxandroid_prac;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.disruption.rxandroid_prac.data.DataSource;
import com.disruption.rxandroid_prac.models.Task;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CreatorsActivity extends AppCompatActivity {
    private static final String TAG = "CreatorsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creators);

        //        useCreateOperator();
        //        useJustOperator();
        //        useRangeOperator();
        //        useRepeatOperator();
        //        useIntervalOperator();
        //        useFromCallableOperator();
    }

    private void useFromCallableOperator() {
        //Particularly useful for db transactions
        // create Observable (method will not execute yet)
        Observable<Task> callable = Observable
                .fromCallable((Callable<Task>) () -> {
                    //return your data from your db
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        // method will be executed since now something has subscribed
        callable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: : " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useFromArrayOperator() {
        Observable<Task> taskObservable = Observable
                //This takes in an array
                .fromArray((Task[]) DataSource.createTasksList().toArray())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: : " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useTimerOperator() {
        // emit single observable after a given delay
        Observable<Long> timeObservable = Observable
                .timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        timeObservable.subscribe(new Observer<Long>() {

            long time = 0; // variable for demonstrating how much time has passed

            @Override
            public void onSubscribe(Disposable d) {
                time = System.currentTimeMillis() / 1000;
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: " + ((System.currentTimeMillis() / 1000) - time) + " seconds have elapsed.");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useIntervalOperator() {
        // emit an observable every time interval
        Observable<Long> intervalObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile(aLong -> {
                    // stop the process if more than 5 seconds passes
                    return aLong <= 5;
                })
                .observeOn(AndroidSchedulers.mainThread());

        intervalObservable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext: interval: " + aLong);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useRepeatOperator() {
        Observable<Integer> observable = Observable
                .range(0, 3)
                .subscribeOn(Schedulers.io())
                .repeat(3)
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: ---------------------- " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useRangeOperator() {
        Observable<Task> observable = Observable
                .range(0, 10)
                .subscribeOn(Schedulers.io())
                .map(integer -> {
                    //This will run on a background thread
                    return new Task("The aliens are dead? Confirm " + integer,
                            true, integer);
                })
                .takeWhile(task -> (task.getPriority() < 10))
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Task>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: ---------------------- " + task.getPriority());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useJustOperator() {
        Task task = new Task("Go to the moon and defeat the aliens there", true, 5);

        //Just operator takes in a maximum of 10 items in an array
        Observable<Task> taskObservable = Observable
                .just(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: -------------------- " + task.getDescription());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useCreateOperator() {
        // Task task = new Task("Go to the moon and defeat the aliens there", true, 5);
        List<Task> tasks = DataSource.createTasksList();

        Observable<Task> taskObservable = Observable
                .create((ObservableOnSubscribe<Task>) emitter -> {

                    for (Task task : tasks) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(task);
                        }
                    }

                    //Since it is out of the loop, complete it if it is not
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                    /*if (!emitter.isDisposed()) {
                        emitter.onNext(task);
                        //Since there is only one task object call complete
                        emitter.onComplete();
                    }*/
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: -------------------- " + task.getDescription());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
