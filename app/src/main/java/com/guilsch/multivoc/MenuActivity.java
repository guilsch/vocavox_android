package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    private ImageView revision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.revision = (ImageView) findViewById(R.id.Revision);

        System.out.println(Environment.getRootDirectory());

        revision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent revisionActivity = new Intent(getApplicationContext(), RevisionActivity.class);
                startActivity(revisionActivity);
                finish();
            }
        });
    }
}