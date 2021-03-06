package com.smona.padora.tools.module;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.util.Log;

import com.smona.padora.tools.R;


public class CompassActivity extends AppCompatActivity {

    private static final String TAG = "CompassActivity";

    private CompassView compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compass = new CompassView(this);
        compass.arrowView = (ImageView) findViewById(R.id.main_image_hands);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
