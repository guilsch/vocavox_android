package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Spinner spinner;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.start = (Button) findViewById(R.id.start);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.progressBar = findViewById(R.id.progressBar);

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
                start.setTextColor(getResources().getColor(R.color.button_std_text_on_click));

                Param.TARGET_LANGUAGE = spinner.getSelectedItem().toString();

                progressBar.setVisibility(View.VISIBLE);

                initAppData();

                Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menuActivity);
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

    public void initAppData() {
        // Retrieve preferences variables
        Pref.retrieveAllPreferences(this);

        // Init other static variables
        utils.initParam();

        // Set user and target language flag
        setUserLanguageVisuals();
        setTargetLanguageVisuals();

    }

    public void setUserLanguageVisuals() {
        switch (Param.USER_LANGUAGE) {
            case "English" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_gb);
                break;

            case "French" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_fr);
                break;

            case "German" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_de);
                break;

            case "Italian" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_it);
                break;

            case "Russian" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_ru);
                break;

            case "Spanish" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_es);
                break;

            default:
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_lang_default);
                break;
        }
    }

    public void setTargetLanguageVisuals() {
        switch (Param.TARGET_LANGUAGE) {
            case "English" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_gb);
                break;

            case "French" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_fr);
                break;

            case "German" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_de);
                break;

            case "Italian" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_it);
                break;

            case "Russian" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_ru);
                break;

            case "Spanish" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_es);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + Param.TARGET_LANGUAGE);
        }
    }

}