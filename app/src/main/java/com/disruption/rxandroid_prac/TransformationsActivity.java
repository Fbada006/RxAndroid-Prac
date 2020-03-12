package com.disruption.rxandroid_prac;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.disruption.rxandroid_prac.data.DataSource;
import com.disruption.rxandroid_prac.models.Task;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TransformationsActivity extends AppCompatActivity {
    private static final String TAG = "TransformationsActivity";

    CompositeDisposable disposables = new CompositeDisposable();
    private SearchView searchView;
    private long timeSinceLastRequest; // for log printouts only. Not part of logic.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformations);
        searchView = findViewById(R.id.search_view);

        timeSinceLastRequest = System.currentTimeMillis();

        //useMap();
          useDebounce();
    }

    void useDebounce() {
        // create the Observable
        Observable<String> observableQueryText = Observable
                .create((ObservableOnSubscribe<String>) emitter -> {

                    // Listen for text input into the SearchView
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(final String newText) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(newText); // Pass the query to the emitter
                            }
                            return false;
                        }
                    });
                })
                .debounce(500, TimeUnit.MILLISECONDS) // Apply Debounce() operator to limit requests
                .subscribeOn(Schedulers.io());

        // Subscribe an Observer
        observableQueryText.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: time  since last request: " + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();

                // method for sending a request to the server
                sendRequestToServer(s);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    void useBufferForClicks() {
        // detect clicks to a button
        RxView.clicks(findViewById(R.id.button))
                .map(unit -> {
                    // convert the detected clicks to an integer
                    return 1;
                })
                .buffer(4, TimeUnit.SECONDS) // capture all the clicks during a 4 second interval
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d); // add to disposables to you can clear in onDestroy
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: You clicked " + integers.size() + " times in 4 seconds!");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void useBuffer() {
        // Create an Observable
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable
                .buffer(2) // Apply the Buffer() operator and emit two objects at a time
                .subscribe(new Observer<List<Task>>() { // Subscribe and view the emitted results
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Task> tasks) {
                        Log.d(TAG, "onNext: bundle results: -------------------");
                        for (Task task : tasks) {
                            Log.d(TAG, "onNext: " + task.getDescription());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    void useMap() {
        Observable<String> extractDescriptionObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io())
                //Map maintains order
                .map(task -> {
                    Log.d(TAG, "apply: doing work on thread: " + Thread.currentThread().getName());
                    return task.getDescription();
                })
                .observeOn(AndroidSchedulers.mainThread());

        extractDescriptionObservable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: extracted description: " + s);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // Fake method for sending a request to the server
    private void sendRequestToServer(String query) {
        // do nothing
    }

    // make sure to clear disposables when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
