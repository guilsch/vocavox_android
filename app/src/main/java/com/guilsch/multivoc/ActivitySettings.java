package com.guilsch.multivoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ActivitySettings extends AppCompatActivity {

    private static final int REQUEST_CODE_IMPORT_DATA_FILE = 123;
    private ImageView importDataFileButton;
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

//       Import data file
        importDataFileButton = findViewById(R.id.import_data_file_button);
        importDataFileButton.setOnClickListener(view -> importDataFile());

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

    public void importDataFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_IMPORT_DATA_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMPORT_DATA_FILE && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedUri);
            listFiles(pickedDir);
            copyFilesWithPrefix(pickedDir);

            // TODO manage case no file etc...

            Utils.showToast(ActivitySettings.this, getString(R.string.toast_msg_data_file_imported));
        }
    }

    private void listFiles(DocumentFile directory) {
        if (directory != null && directory.isDirectory()) {
            for (DocumentFile file : directory.listFiles()) {
                if (file.isFile()) {
                    System.out.println("File : " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println("Directory : " + file.getName());
                }
            }
        }
    }

    private void copyFilesWithPrefix(DocumentFile pickedDir) {
        if (pickedDir != null && pickedDir.isDirectory()) {
            File appDirectory = new File(Param.FOLDER_PATH);

            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }

            for (DocumentFile file : pickedDir.listFiles()) {
                if (file.isFile() && file.getName().startsWith(Param.FILE_NAME_PREFIX)) {
                    File destinationFile = new File(appDirectory, file.getName());
                    copyFile(file.getUri(), destinationFile);
                }
            }
        }
    }

    private void copyFile(Uri sourceUri, File destinationFile) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            OutputStream outputStream = new FileOutputStream(destinationFile);

            // Copy content of the file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("File copied : " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
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