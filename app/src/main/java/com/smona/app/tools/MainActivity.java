package com.smona.app.tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.smona.app.tools.module.CompassActivity;
import com.smona.app.tools.module.FlashLightActivity;
import com.smona.app.tools.module.RulerActivity;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
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

        ModuleAdapter adapter = new ModuleAdapter(this, mApps);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModuleInfo module = mApps.get(position);
                gotoActivity(module);
            }
        });
    }

    private void gotoActivity(ModuleInfo info) {
        Intent intent = new Intent();
        intent.setClass(this, info.gotoClazz);
        startActivity(intent);
    }
}
