package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Spinner spinner;
    private String[] languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.start = (Button) findViewById(R.id.start);
        this.spinner = (Spinner) findViewById(R.id.spinner);

        languages = new String[]{"English", "German", "Italian", "Russian"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.languages);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

}