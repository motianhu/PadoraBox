package com.smona.app.tools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smona.padora.tools.module.CompassActivity;
import com.smona.padora.tools.module.FlashLightActivity;
import com.smona.padora.tools.module.RulerActivity;
import com.smona.padora.tools.module.calculator.CalculatorActivity;
import com.smona.tools.compress.ImageCompressActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
        });

        mGrid = (GridView) findViewById(R.id.mainGrid);

        final ArrayList<ModuleInfo> mApps = new ArrayList<>();

        ModuleInfo flashlight = new ModuleInfo();
        flashlight.gotoClazz =  FlashLightActivity.class;
        flashlight.title = getResources().getString(R.string.flashlight);
        mApps.add(flashlight);

        ModuleInfo compass = new ModuleInfo();
        compass.gotoClazz =  CompassActivity.class;
        compass.title = getResources().getString(R.string.compass);
        mApps.add(compass);

        ModuleInfo ruler = new ModuleInfo();
        ruler.gotoClazz =  RulerActivity.class;
        ruler.title = getResources().getString(R.string.ruler);
        mApps.add(ruler);

        ModuleInfo calculator = new ModuleInfo();
        calculator.gotoClazz =  CalculatorActivity.class;
        calculator.title = getResources().getString(R.string.calculator);
        mApps.add(calculator);

        ModuleInfo imagecompress = new ModuleInfo();
        imagecompress.gotoClazz =  ImageCompressActivity.class;
        imagecompress.title = getResources().getString(R.string.imagecompress);
        mApps.add(imagecompress);

        ModuleAdapter adapter = new ModuleAdapter(this, mApps);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener((parent, view, position, id) -> {
            ModuleInfo module = mApps.get(position);
            gotoActivity(module);
        });
    }

    private void gotoActivity(ModuleInfo info) {
        Intent intent = new Intent();
        intent.setClass(this, info.gotoClazz);
        startActivity(intent);
    }
}
