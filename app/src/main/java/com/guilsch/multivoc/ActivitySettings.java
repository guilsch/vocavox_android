package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import ir.androidexception.filepicker.dialog.DirectoryPickerDialog;

public class ActivitySettings extends AppCompatActivity {

    private ImageView folderPathButton;
    private EditText folderPathText;
    private TextView langDirectionFreqSaveButton;
    private TextView langDirectionFreqIndicator;
//    private DiscreteSeekBar langDirectionFreqSeekBar;
    private ConstraintLayout backLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

//        Folder Path
        folderPathButton = findViewById(R.id.folder_path_button);
        folderPathText = findViewById(R.id.folder_path_text);
        folderPathText.setText(Param.FOLDER_PATH);
        folderPathButton.setOnClickListener(view -> managePathChange());

//      Language direction frequency
        langDirectionFreqSaveButton = findViewById(R.id.lang_direction_save_button);
//        langDirectionFreqSeekBar = findViewById(R.id.lang_direction_freq_seekBar);
        langDirectionFreqIndicator = findViewById(R.id.lang_direction_freq_indicator);

        langDirectionFreqIndicator.setText(Param.LANG_DIRECTION_FREQ + "/10");

//        langDirectionFreqSeekBar.setProgress(Param.LANG_DIRECTION_FREQ);
        langDirectionFreqSaveButton.setOnClickListener(view -> langDirectionFreqSaveClick());

    }

    /***
     * Launch dialog box to select data folder
     */
    public void managePathChange() {
        if(permissionGranted()) {
            DirectoryPickerDialog directoryPickerDialog = new DirectoryPickerDialog(this,
                    () -> Toast.makeText(ActivitySettings.this, "", Toast.LENGTH_SHORT),
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
            newPath = Utils.formatPath(newPath);
            file = new File(newPath);

            System.out.println("2nd Path = " + newPath);
            if (!file.isAbsolute()) {
                Utils.showToast(ActivitySettings.this, "Path is not valid");
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
            Utils.moveDirectory(originalPath, newPath);
            Utils.deleteDirectoryFiles(originalPath);

            // Save new path
            Param.FOLDER_PATH = newPath;
            Pref.savePreference(ActivitySettings.this, Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
            folderPathText.setText(Param.FOLDER_PATH);

            // Show to user
//            Utils.showToast(ActivitySettings.this, "Path has been changed");
        }
        catch (IOException e) {
            e.printStackTrace();
            Utils.showToast(ActivitySettings.this, "Error, try again");
        }
    }

    public void langDirectionFreqSaveClick() {
//        Param.LANG_DIRECTION_FREQ = langDirectionFreqSeekBar.getProgress();
        Pref.savePreference(ActivitySettings.this, Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ);
        langDirectionFreqIndicator.setText(Param.LANG_DIRECTION_FREQ + "/10");
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(menuActivity);
        finish();
    }
}