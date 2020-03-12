package com.disruption.rxandroid_prac.publisheroperator;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.disruption.rxandroid_prac.R;

import java.io.IOException;

public class PublisherActivity extends AppCompatActivity {
    private static final String TAG = "PublisherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher);

        getData();
    }

    private void getData() {
        PublisherViewModel viewModel = ViewModelProviders.of(this).get(PublisherViewModel.class);
        viewModel.makeQuery().observe(this, responseBody -> {
            Log.d(TAG, "onChanged: this is a live data response!");
            try {
                Log.d(TAG, "onChanged: " + responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
