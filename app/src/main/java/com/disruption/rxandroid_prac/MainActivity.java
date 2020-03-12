package com.disruption.rxandroid_prac;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.disruption.rxandroid_prac.data.DataSource;
import com.disruption.rxandroid_prac.futureoperator.FutureActivity;
import com.disruption.rxandroid_prac.models.Task;
import com.disruption.rxandroid_prac.publisheroperator.PublisherActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //UI
    private TextView mCreators;
    private TextView mPublishers;
    private TextView mFuture;
    private TextView mFilter;
    private TextView mTransformations;

    //Vars
    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreators = findViewById(R.id.creators);
        mPublishers = findViewById(R.id.publisher);
        mFuture = findViewById(R.id.future);
        mFilter = findViewById(R.id.filter);
        mTransformations = findViewById(R.id.transformations);
        initListeners();

        //useFromIterableOperator();
    }

    private void useFromIterableOperator() {
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .filter(task -> {
                    Log.d(TAG, "test ===================== " + Thread.currentThread().getName());
                    return task.isComplete();
                })
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ------------------- called");
                //Add the disposable to the list
                mDisposables.add(d);
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: --------------------- " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: --------------------- " + task.getDescription());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: -----------" + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: -------------------- called");
            }
        });
    }

    private void initListeners() {
        mCreators.setOnClickListener(view -> {
            startActivity(new Intent(this, CreatorsActivity.class));
        });

        mPublishers.setOnClickListener(view -> {
            startActivity(new Intent(this, PublisherActivity.class));
        });

        mFuture.setOnClickListener(view -> {
            startActivity(new Intent(this, FutureActivity.class));
        });

        mFilter.setOnClickListener(view -> {
            startActivity(new Intent(this, FilterActivity.class));
        });

        mTransformations.setOnClickListener(view -> {
            startActivity(new Intent(this, TransformationsActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }
}
