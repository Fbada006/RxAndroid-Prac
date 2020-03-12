package com.disruption.rxandroid_prac;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.disruption.rxandroid_prac.data.DataSource;
import com.disruption.rxandroid_prac.models.Task;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "FilterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        //useFilter();
        //useDistinct();
        useTakeWhile();
    }

    private void useTakeWhile() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                //Emit objects from the list until this test returns false
                //It will emit task objects whose complete flag is true and stops immediately
                //the next object becomes false
                .takeWhile(Task::isComplete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useTake() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                //Takes only the first three objects
                .take(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useDistinct() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .distinct(task -> {
                    // Filter it on unique descriptions only. Duplicate descriptions will be omitted
                    return task.getDescription();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void useFilter() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .filter(task -> task.getDescription().equals("Walk the dog"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: This task matches the description: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
