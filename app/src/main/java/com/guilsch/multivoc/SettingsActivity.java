package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Button dataPathButton;
    private Button dataPathDefaultButton;
    private EditText dataPathText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataPathButton = (Button) findViewById(R.id.data_path_button);
        dataPathDefaultButton = (Button) findViewById(R.id.data_path_default);
        dataPathText = (EditText) findViewById(R.id.data_path_text);

        dataPathText.setText(Constants.getDataPath());

        dataPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.setDataPath(dataPathText.getText().toString());
            }
        });

        dataPathDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.setDataPath(Constants.getDataPathDefault());
                dataPathText.setText(Constants.getDataPath());
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