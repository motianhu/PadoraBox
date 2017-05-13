package com.smona.app.tools.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.smona.app.tools.R;

/**
 * Created by motianhu on 5/13/17.
 */

public class RulerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);

        RulerView leftRuler = (RulerView) findViewById(R.id.left_ruler_view);
        leftRuler.setUnitType(RulerView.Unit.CM);
        RulerView rightRuler = (RulerView)findViewById(R.id.right_ruler_view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
