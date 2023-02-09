package com.guilsch.multivoc;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class FirstLaunchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_FOLDER = 1;
    private static final String TAG = "oui";

    private EditText folderPathEditText;
    private Button folderPathInterfaceButton;
    private Button saveButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        folderPathEditText = findViewById(R.id.folder_path_edit_text);
        saveButton = findViewById(R.id.folder_path_save_button);
        folderPathInterfaceButton = findViewById(R.id.folder_path_interface_button);

        folderPathEditText.setText(Param.FOLDER_PATH);
        folderPathInterfaceButton.setOnClickListener(view -> onFolderPathInterfaceButtonClick());
        saveButton.setOnClickListener(view -> onSaveClick());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onFolderPathInterfaceButtonClick() {
        Intent selectFolderIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(selectFolderIntent, REQUEST_CODE_SELECT_FOLDER);
    }

    public void onSaveClick() {
        Param.FOLDER_PATH = folderPathEditText.getText().toString();
        Pref.savePreference(FirstLaunchActivity.this, Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri selectedFolderUri = data.getData();
            if (selectedFolderUri != null) {

//                String realPath = FileUtils2.getRealPathFromURI(this, selectedFolderUri);
//                folderPathEditText.setText(realPath);

                String path = selectedFolderUri.toString() + "/";
                folderPathEditText.setText(path);

//                String path = FileUtils.getPathFromUri(this, selectedFolderUri);
//                folderPathEditText.setText(path);
//                String path = selectedFolderUri.getPath();
//                System.out.println("pathi : " + path);

//                File selectedFolder = new File(selectedFolderUri.getPath());
//                folderPathEditText.setText(selectedFolder.getAbsolutePath());


            }
        }
    }
}