package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import ir.androidexception.filepicker.dialog.DirectoryPickerDialog;

public class SettingsActivity extends AppCompatActivity {

    private ImageView folderPathButton;
    private TextView folderPathText;
    private Button langDirectionFreqSaveButton;
    private SeekBar langDirectionFreqSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        Folder Path
        folderPathButton = findViewById(R.id.folder_path_button);
        folderPathText = findViewById(R.id.folder_path_text);
        folderPathText.setText(Param.FOLDER_PATH);
        folderPathButton.setOnClickListener(view -> managePathChange());

//      Language direction frequency
        langDirectionFreqSaveButton = findViewById(R.id.lang_direction_save_button);
        langDirectionFreqSeekBar = findViewById(R.id.lang_direction_freq_seekBar);
        langDirectionFreqSeekBar.setProgress(Param.LANG_DIRECTION_FREQ);
        langDirectionFreqSaveButton.setOnClickListener(view -> langDirectionFreqSaveClick());

    }

    /***
     * Launch dialog box to select data folder
     */
    public void managePathChange() {
        if(permissionGranted()) {
            DirectoryPickerDialog directoryPickerDialog = new DirectoryPickerDialog(this,
                    () -> Toast.makeText(SettingsActivity.this, "", Toast.LENGTH_SHORT),
                    files -> checkAndCorrectDirectoryPath(Param.FOLDER_PATH, files[0].getPath() + "/")
            );
            directoryPickerDialog.show();
        }
        else{
            requestPermission();
        }
    }

    /***
     * Check folder picker permissions
     * @return
     */
    private boolean permissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    /***
     * Check if the data folder path is valid and try to adapt it if not
     * @param originalPath
     * @param newPath
     */
    public void checkAndCorrectDirectoryPath(String originalPath, String newPath) {

        System.out.println("New path = " + newPath);
        System.out.println("Original Path = " + originalPath);

        File file = new File(newPath);

        if (!file.isAbsolute()) {
            // Try again with formatted path
            newPath = utils.formatPath(newPath);
            file = new File(newPath);

            System.out.println("2nd Path = " + newPath);
            if (!file.isAbsolute()) {
                utils.showToast(SettingsActivity.this, "Path is not valid");
                folderPathText.setText(Param.FOLDER_PATH);
                return;
            }
        }
        updateDirectoryPath(originalPath, newPath);
    }

    /***
     * Move data path and files in new data folder. Save new path in preferences
     * @param originalPath
     * @param newPath
     */
    public void updateDirectoryPath(String originalPath, String newPath) {
        try {
            // Move files
            utils.moveDirectory(originalPath, newPath);
            utils.deleteDirectoryFiles(originalPath);

            // Save new path
            Param.FOLDER_PATH = newPath;
            Pref.savePreference(SettingsActivity.this, Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
            folderPathText.setText(Param.FOLDER_PATH);

            // Show to user
//            utils.showToast(SettingsActivity.this, "Path has been changed");
        }
        catch (IOException e) {
            e.printStackTrace();
            utils.showToast(SettingsActivity.this, "Error, try again");
        }
    }

    public void langDirectionFreqSaveClick() {
        Param.LANG_DIRECTION_FREQ = langDirectionFreqSeekBar.getProgress();
        Pref.savePreference(SettingsActivity.this, Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ);
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}