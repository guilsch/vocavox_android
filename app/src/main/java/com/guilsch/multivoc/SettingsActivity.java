package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.NumberFormat;

public class SettingsActivity extends AppCompatActivity {

    private Button folderPathSaveButton;
    private Button folderPathDefaultButton;
    private EditText folderPathText;

    private Button langDirectionFreqSaveButton;
    private Button langDirectionFreqDefaultButton;
    private SeekBar langDirectionFreqSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        Folder Path

        folderPathSaveButton = (Button) findViewById(R.id.folder_path_save_button);
        folderPathDefaultButton = (Button) findViewById(R.id.folder_path_default_button);
        folderPathText = (EditText) findViewById(R.id.folder_path_edit_text);
        folderPathText.setText(Param.FOLDER_PATH);

        folderPathSaveButton.setOnClickListener(view -> folderPathSaveClick());
        folderPathDefaultButton.setOnClickListener(view -> folderPathDefaultClick());

//      Language direction frequency

        langDirectionFreqSaveButton = (Button) findViewById(R.id.lang_direction_save_button);
        langDirectionFreqDefaultButton = (Button) findViewById(R.id.lang_direction_default_button);
//        langDirectionFreqText = (EditText) findViewById(R.id.lang_direction_freq_edit_text);
//        langDirectionFreqText.setText(this.nbrFormat.format(Param.LANG_DIRECTION_FREQ));

        langDirectionFreqSeekBar = findViewById(R.id.lang_direction_freq_seekBar);
        langDirectionFreqSeekBar.setProgress(Param.LANG_DIRECTION_FREQ);

        langDirectionFreqSaveButton.setOnClickListener(view -> langDirectionFreqSaveClick());
        langDirectionFreqDefaultButton.setOnClickListener(view -> langDirectionFreqDefaultClick());

    }

    public void folderPathSaveClick() {
        Param.FOLDER_PATH = folderPathText.getText().toString();
        Pref.savePreference(SettingsActivity.this, Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
    }

    public void folderPathDefaultClick() {
        Param.FOLDER_PATH = Param.FOLDER_PATH_DEFAULT;
        Pref.savePreference(SettingsActivity.this, Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
        folderPathText.setText(Param.FOLDER_PATH);
    }

    public void langDirectionFreqSaveClick() {
        Param.LANG_DIRECTION_FREQ = langDirectionFreqSeekBar.getProgress();
        Pref.savePreference(SettingsActivity.this, Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ);
    }

    public void langDirectionFreqDefaultClick() {
        Param.LANG_DIRECTION_FREQ = Param.LANG_DIRECTION_FREQ_DEFAULT;
        langDirectionFreqSeekBar.setProgress(Param.LANG_DIRECTION_FREQ);
        Pref.savePreference(SettingsActivity.this, Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ);
    }


    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}