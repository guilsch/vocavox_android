package com.guilsch.multivoc;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public Task<String> updateOrCreate(String fileId) {
        System.out.println("Enter updateOrCreate");
        return Tasks.call(mExecutor, () -> {
            System.out.println("Enter return");
            try {
                System.out.println("Enter try");
                File file = mDriveService.files().get(fileId).execute();
                System.out.println(Param.FILE_ID);

                if (!file.isEmpty()) {
                    System.out.println("Not empty");
                    return file.getId();
                }
                else {
                    System.out.println("Empty");
                    return "File empty";
                }
            }
            catch (IOException e) {
                Log.d("tag",e.getMessage());
                System.out.println("Catch execption...");
                return null;
            }

        });
    }

    public Task<String> updateFile(String fileId, String filePath) {
        return Tasks.call(mExecutor, () -> {
            try {
                File file = new File();
                java.io.File fileContent = new java.io.File(filePath);

                file.setName(fileContent.getName())
                        .setMimeType(Param.DATA_MIME_TYPE);

                FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, fileContent);

                File updatedFile = mDriveService.files().update(fileId, file, mediaContent).execute();


                return updatedFile.getId();

            }
            catch (IOException e) {
                Log.d("tag",e.getMessage());

                return null;
            }

        });
    }

    public Task<String> downloadDataFile(String fileID) {
        return Tasks.call(mExecutor, () -> {
            File file = new File();
            try {
                file = mDriveService.files().get(fileID).execute();
                mDriveService.files().get(fileID).executeMediaAndDownloadTo(new FileOutputStream(Param.DATA_PATH));
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return file.getId();

        });
    }

    public Task<String> createDataFile(String filePath) {
        return Tasks.call(mExecutor, () -> {
            File fileMetaData = new File();
            fileMetaData
                    .setName(Param.DATA_FILE.substring(0, 5))
                    .setParents(Arrays.asList(Param.FOLDER_ID));

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, file);

            File myFile = null;

            try {
                myFile = mDriveService.files().create(fileMetaData, mediaContent).execute();
                utils.setFileID(myFile.getId());
                System.out.println("ID : " + myFile.getId());
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            if (myFile == null) {
                throw new IOException("Null result when requesting file creation");
            }

            return myFile.getId();

        });
    }
}