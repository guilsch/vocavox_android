package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // Retrieve preferences
        Pref.retrieveAllPreferences(this);
        utils.initParam();

        if (!(new File(Param.DATA_PATH)).exists()) {
            System.out.println(Param.DATA_PATH + " doesn't exist yet");
            try {
                utils.createDataFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(Param.DATA_PATH + " already exists");
        }

        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();

    }
}