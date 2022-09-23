package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Button folderPathSaveButton;
    private Button folderPathDefaultButton;
    private EditText folderPathText;

    private Button folderURLSaveButton;
    private Button folderURLDefaultButton;
    private EditText folderURLText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

//        Folder Path

        folderPathSaveButton = (Button) findViewById(R.id.folder_path_save_button);
        folderPathDefaultButton = (Button) findViewById(R.id.folder_path_default_button);
        folderPathText = (EditText) findViewById(R.id.folder_path_edit_text);
        folderPathText.setText(Param.FOLDER_PATH);

        folderPathSaveButton.setOnClickListener(view -> folderPathSaveClick());
        folderPathDefaultButton.setOnClickListener(view -> folderPathDefaultClick());


//        URL Drive Folder

        folderURLSaveButton = (Button) findViewById(R.id.folder_url_save_button);
        folderURLDefaultButton = (Button) findViewById(R.id.folder_url_default);
        folderURLText = (EditText) findViewById(R.id.folder_url_edit_text);
        folderURLText.setText(Param.FOLDER_ID);

        folderURLSaveButton.setOnClickListener(view -> folderURLSaveClick());
        folderURLDefaultButton.setOnClickListener(view -> folderURLDefaultClick());
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

    public void folderURLSaveClick() {
        Param.FOLDER_ID = utils.URLtoID(folderURLText.getText().toString());
        Pref.savePreference(SettingsActivity.this, Param.FOLDER_ID_KEY, Param.FOLDER_ID);
        System.out.println(Param.FOLDER_ID);
    }

    public void folderURLDefaultClick() {
        Param.FOLDER_ID = Param.FOLDER_ID_DEFAULT;
        Pref.savePreference(SettingsActivity.this, Param.FOLDER_ID_KEY, Param.FOLDER_ID);
        folderURLText.setText(Param.FOLDER_ID);
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}