package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.IOException;

import ir.androidexception.filepicker.dialog.DirectoryPickerDialog;

public class ActivitySettings extends AppCompatActivity {

    private TextView langDirectionFreqSaveButton;
    private TextView langDirectionFreqIndicator;
    private DiscreteSeekBar langDirectionFreqSeekBar;
    private ConstraintLayout backLayout;
    private Switch automaticSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

//      Language direction frequency
        langDirectionFreqSaveButton = findViewById(R.id.lang_direction_save_button);
        langDirectionFreqSeekBar = findViewById(R.id.lang_direction_freq_seekBar);
        langDirectionFreqIndicator = findViewById(R.id.lang_direction_freq_indicator);

        langDirectionFreqIndicator.setText(Param.LANG_DIRECTION_FREQ + "/10");

        langDirectionFreqSeekBar.setProgress(Param.LANG_DIRECTION_FREQ);
        langDirectionFreqSaveButton.setOnClickListener(view -> langDirectionFreqSaveClick());

        // Automatic switch
        automaticSpeech = findViewById(R.id.play_speech_switch);
        automaticSpeech.setChecked(Param.AUTOMATIC_SPEECH);
        automaticSpeech.setOnClickListener(v -> automaticSpeechSwitch());

    }

    public void langDirectionFreqSaveClick() {
        Param.LANG_DIRECTION_FREQ = langDirectionFreqSeekBar.getProgress();
        Pref.savePreference(ActivitySettings.this, Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ);
        langDirectionFreqIndicator.setText(Param.LANG_DIRECTION_FREQ + "/10");
    }

    public void automaticSpeechSwitch() {
        Param.AUTOMATIC_SPEECH = !Param.AUTOMATIC_SPEECH;
        Pref.savePreference(ActivitySettings.this, Param.AUTOMATIC_SPEECH_KEY, Param.AUTOMATIC_SPEECH);
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(menuActivity);
        finish();
    }
}