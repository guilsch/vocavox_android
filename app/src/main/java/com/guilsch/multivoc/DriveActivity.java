package com.guilsch.multivoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DriveActivity extends AppCompatActivity {

    private static final String TAG = "DriveActivity";

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static Integer checkResultFilesNb;
    private static String checkResultFileID;

    private int filesFoundNb;
    private String fileFoundId;
    public static Pair<Integer, String> checkResult;

    private DriveServiceHelper mDriveServiceHelper;

    public static CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        // Set the onClick listeners for the button bar.
        findViewById(R.id.upload_btn).setOnClickListener(view -> uploadOrUpdate());
        findViewById(R.id.download_btn).setOnClickListener(view -> checkAndDownloadFile());
        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.reset_id_button).setOnClickListener(view -> utils.resetFileID(this));

        // Authenticate the user. For most apps, this should be done when the user performs an
        // action that requires Drive access rather than in onCreate.
        requestSignIn();
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }

    public void checkDriveFile () {
        System.out.println("Enter checkDriveFile");
        mDriveServiceHelper.checkDriveFileService()
            .addOnSuccessListener(new OnSuccessListener<Pair<Integer, String>>() {
                @Override
                public void onSuccess(Pair<Integer, String> result) {
                    System.out.println("Files found : " + result.first);
                    System.out.println("Files found ID : " + result.second);

                    DriveActivity.checkResult = new Pair<>(result.first, result.second);

                    latch.countDown();

                    return;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    filesFoundNb = -1;
                    fileFoundId = "error";
                    Toast.makeText(getApplicationContext(), "Check your Google Drive API key", Toast.LENGTH_LONG).show();
                    Pair<Integer, String> res = new Pair<>(filesFoundNb, fileFoundId);
                    return;
                }
            });
    }

    public void checkAndDownloadFile() {
        try {
            checkDriveFile();
            System.out.println("Done");

            System.out.println("Before await : " + latch.getCount());
            latch.await(); // Wait for countdown
            System.out.println("After await : " + latch.getCount());

            int filesFound = checkResult.first;
            String fileID = checkResult.second;

            if (filesFound == 0) {
                new AlertDialog.Builder(DriveActivity.this)
                        .setMessage("It seems you haven't upload anything yet")
                        .setNegativeButton(android.R.string.ok, null)
                        .show();
                return;
            }
            if (filesFound == 1) {
                downloadFile(fileID);
                return;
            }
            if (filesFound >= 2) {
                new AlertDialog.Builder(DriveActivity.this)
                        .setMessage("It seems several files are named " + Param.DATA_FILE + "'. Please manually delete the unnecessary files.")
                        .setNegativeButton(android.R.string.ok, null)
                        .show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String fileId) {
        mDriveServiceHelper.downloadFileService(fileId)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println("File downloaded with ID : " + s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Downloading failed");
                    }
                });
    }

    public void uploadOrUpdate() {

        checkDriveFile();

        int filesFound = checkResult.first;

        if (filesFound == 0) {
            uploadDriveFile();
            return;
        }
        if (filesFound == 1) {
            updateDriveFile(checkResult.second);
            return;
        }
        if (filesFound >= 2) {
            new AlertDialog.Builder(DriveActivity.this)
                    .setMessage("It seems several files are named " + Param.DATA_FILE + "'. Please manually delete the unnecessary files.")
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
        }
    }

    public void uploadDriveFile() {
        mDriveServiceHelper.uploadDriveFileService()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println("File uploaded with ID : " + s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Upload failed");
                    }
                });
    }

    public void updateDriveFile(String fileId) {
        mDriveServiceHelper.updateDriveFileService(fileId)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println("File updated with ID : " + s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Update failed");
                    }
                });
    }




//    public void download() {
//        if ((new File(Param.DATA_PATH)).exists()) {
//
//            new AlertDialog.Builder(DriveActivity.this)
//                    .setTitle("Download file")
//                    .setMessage("Are you sure you want to overwrite data file on device ?")
//
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            System.out.println("Para.fileid = : " + Param.FILE_ID);
//                            mDriveServiceHelper.downloadDataFile(Param.FILE_ID)
//                                    .addOnSuccessListener(new OnSuccessListener<String>() {
//                                        @Override
//                                        public void onSuccess(String s) {
//                                            System.out.println("Received fileid = : " + s);
//                                            Toast.makeText(getApplicationContext(), "Data file downloaded on device", Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//    }

//    public void updateOrUpload() {
//
//        System.out.println("FileID : " + Param.FILE_ID);
//
//        // First time uploading
//        if (Param.FILE_ID == Param.FILE_ID_UNDEFINED) {
//            uploadDataFile();
//            return;
//        }
//
//        // Else
//        System.out.println("FileID 2 : " + Param.FILE_ID);
//        mDriveServiceHelper.updateOrCreate(Param.FILE_ID)
//                .addOnSuccessListener(new OnSuccessListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        if (s == null) {
//                            System.out.println("FileID received : " + s);
//                            Toast.makeText(getApplicationContext(), "First time uploading", Toast.LENGTH_LONG).show();
//                            uploadDataFile();
//                        } else {
//                            System.out.println("String received : " + s);
//                            if (s.compareTo(Param.FILE_ID) == 0) {
//                                Toast.makeText(getApplicationContext(), "Data file existing", Toast.LENGTH_LONG).show();
//                                updateDataFile();
//                            } else {
//                                System.out.println("String not null but not the good one");
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Fail detecting data file", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

//    public void uploadDataFile() {
//
//        ProgressDialog progressDialog = new ProgressDialog(DriveActivity.this);
//        progressDialog.setTitle("Uploading to Google Drive");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();
//
//        System.out.println("enter uploadDataFile, datapath = " + Param.DATA_PATH);
//
//        mDriveServiceHelper.createDataFile(this, Param.DATA_PATH).addOnSuccessListener(new OnSuccessListener<String>() {
//            @Override
//            public void onSuccess(String s) {
//
//                progressDialog.dismiss();
//
//                Toast.makeText(getApplicationContext(), "Uploaded successfully", Toast.LENGTH_LONG).show();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Check your Google Drive API key", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

//    public void updateDataFile() {
//
//        ProgressDialog progressDialog = new ProgressDialog(DriveActivity.this);
//        progressDialog.setTitle("Updating file");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();
//
//        mDriveServiceHelper.updateFile(Param.FILE_ID, Param.DATA_PATH).addOnSuccessListener(new OnSuccessListener<String>() {
//            @Override
//            public void onSuccess(String s) {
//
//                progressDialog.dismiss();
//
//                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Check your Google Drive API key", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());

//                    Drive googleDriveService =
//                            new Drive.Builder(
//                                    AndroidHttp.newCompatibleTransport(),
//                                    new GsonFactory(),
//                                    credential)
//                                    .setApplicationName("Multivoc Drive")
//                                    .build();

                    Drive googleDriveService =
                            new Drive.Builder(
                                    new NetHttpTransport(),
                                    GsonFactory.getDefaultInstance(),
                                    credential)
                                    .setApplicationName("Multivoc Drive")
                                    .build();

                    //

//                    GoogleCredentials credentials = null;
//                    try {
//                        credentials = GoogleCredentials.getApplicationDefault()
//                                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//                            credentials);
//
//                    // Build a new authorized API client service.
//                    Drive service = new Drive.Builder(new NetHttpTransport(),
//                            GsonFactory.getDefaultInstance(),
//                            requestInitializer)
//                            .setApplicationName("Drive samples")
//                            .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
//                    mDriveServiceHelper = new DriveServiceHelper(service);
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }
}