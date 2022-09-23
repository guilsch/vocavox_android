package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.start = (Button) findViewById(R.id.start);
        this.spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Param.TARGET_LANGUAGES);
        spinner.setAdapter(adapter);

        // Permissions
        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage);
        }

        String[] permissionsWriteStorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int requestWriteExternalStorage = 1;
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsWriteStorage, requestWriteExternalStorage);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Param.TARGET_LANGUAGE = spinner.getSelectedItem().toString();

                Intent initActivity = new Intent(getApplicationContext(), InitActivity.class);
                startActivity(initActivity);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }

}