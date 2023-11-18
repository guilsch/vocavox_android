package com.guilsch.multivoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import java.nio.file.Paths;

import ir.androidexception.filepicker.dialog.DirectoryPickerDialog;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ActivitySettings extends AppCompatActivity {

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
        importDataFileButton.setOnClickListener(view -> openDirectory());

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

    /***
     * Launch dialog box to select data folder
     */
    public void manageImportDataFile() {
        if(permissionGranted()) {
            System.out.println("Permission granted");
            DirectoryPickerDialog directoryPickerDialog = new DirectoryPickerDialog(this,
                    () -> Toast.makeText(ActivitySettings.this, "", Toast.LENGTH_SHORT),
                    files -> importDataFile(files[0].getPath() + "/")
            );
            directoryPickerDialog.show();
        }
        else{
            System.out.println("Permission not granted, request permission");
            requestPermission();
        }
    }

    /***
     * Move data path and files in new data folder. Save new path in preferences
     * @param originalDataFilePath
     */
    public void importDataFile(String originalDataFilePath) {

        System.out.println("Original Path = " + originalDataFilePath);
        System.out.println("Folder path = " + Param.FOLDER_PATH);

        originalDataFilePath = Utils.formatPath(originalDataFilePath);

        System.out.println("Original Path formatted = " + originalDataFilePath);

        try {
            Utils.isDirectoryEmpty(Paths.get(originalDataFilePath));

            // Move files
//            Utils.moveDirectory(originalDataFilePath, Param.FOLDER_PATH, true);

            // Show to user
//            Utils.showToast(ActivitySettings.this, "Path has been changed");
        }
        catch (IOException e) {
            e.printStackTrace();
            Utils.showToast(ActivitySettings.this, "Error, try again");
        }
    }

    public void openDirectory() {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();

            // Obtenez une référence DocumentFile à partir de l'URI sélectionné
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, selectedUri);

            // Liste tous les fichiers dans le répertoire sélectionné
            listFiles(pickedDir);
            copyFilesWithPrefix(pickedDir);
        }
    }

    private void listFiles(DocumentFile directory) {
        if (directory != null && directory.isDirectory()) {
            for (DocumentFile file : directory.listFiles()) {
                if (file.isFile()) {
                    System.out.println("Fichier : " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println("Répertoire : " + file.getName());
                    // Si vous voulez lister récursivement les fichiers dans les sous-répertoires, appelez récursivement listFiles
                    // listFiles(file);
                }
            }
        }
    }

    private void copyFilesWithPrefix(DocumentFile pickedDir) {
        if (pickedDir != null && pickedDir.isDirectory()) {
            // Obtenez le chemin du répertoire de l'application à partir de l'adresse
            File appDirectory = new File(Param.FOLDER_PATH);

            // Assurez-vous que le répertoire de l'application existe
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }

            for (DocumentFile file : pickedDir.listFiles()) {
                if (file.isFile() && file.getName().startsWith(Param.FILE_NAME_PREFIX)) {
                    // Construisez le chemin de destination dans le répertoire de l'application
                    File destinationFile = new File(appDirectory, file.getName());

                    // Copiez le fichier
                    copyFile(file.getUri(), destinationFile);
                }
            }
        }
    }

    private void copyFile(Uri sourceUri, File destinationFile) {
        try {
            // Obtenez un flux d'entrée à partir de l'URI source
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);

            // Obtenez un flux de sortie vers le fichier de destination
            OutputStream outputStream = new FileOutputStream(destinationFile);

            // Copiez le contenu du fichier
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Fermez les flux
            inputStream.close();
            outputStream.close();

            System.out.println("Fichier copié : " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                1);
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