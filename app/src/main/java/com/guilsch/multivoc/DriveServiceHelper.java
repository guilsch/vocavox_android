package com.guilsch.multivoc;

import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
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

    public Task<String> downloadFileService(String fileID) {
        return Tasks.call(mExecutor, () -> {
            File file = new File();
            try {
                file = mDriveService.files().get(fileID).execute();
                mDriveService.files().get(fileID).executeMediaAndDownloadTo(
                        new FileOutputStream(Param.DATA_PATH));
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return file.getId();

        });
    }

    public Task<Pair<Integer, String>> checkDriveFileService() {
        return  Tasks.call(mExecutor, ()  -> {

//            try {

                List<File> files = new ArrayList<>();

                String pageToken = null;

                do {

                    FileList result = mDriveService.files().list()
                            .setQ("mimeType='" + Param.DATA_MIME_TYPE + "' and '" + Param.FOLDER_ID
                                    + "' in parents and name = '" + Param.DATA_FILE + "'")
                            .setSpaces("drive")
                            .setFields("nextPageToken, items(id, title)")
                            .setPageToken(pageToken)
                            .execute();
                    // execute() make the thread crash

                    System.out.println("result = " + result);

                    for (File file : result.getFiles()) {
                        System.out.printf("Found file: %s \n",
                                file.getName());
                    }

                    files.addAll(result.getFiles());

                    pageToken = result.getNextPageToken();
                } while (pageToken != null);

                int files_nb = files.size();
                String id = "files number error";

                if (files_nb == 1) {
                    id = files.get(0).getId();
                }

                Pair<Integer, String> result2 = new Pair<>(files_nb, id);

                return result2;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
        });
    }

//    public Task<Pair<Integer, String>> checkDriveFileService() {
//        return  Tasks.call(mExecutor, () -> {
//
//            List<File> files = new ArrayList<File>();
//
//            String pageToken = null;
//
//            do {
//                FileList result = mDriveService.files().list()
//                        .setQ("mimeType='" + Param.DATA_MIME_TYPE + "' and '" + Param.FOLDER_ID
//                                + "' in parents and name = '" + Param.DATA_FILE + "'")
//                        .setSpaces("drive")
//                        .setFields("nextPageToken, items(id, title)")
//                        .setPageToken(pageToken)
//                        .execute();
//
//                for (File file : result.getFiles()) {
//                    System.out.printf("Found file: %s \n",
//                            file.getName());
//                }
//
//                files.addAll(result.getFiles());
//
//                pageToken = result.getNextPageToken();
//            } while (pageToken != null);
//
//            int files_nb = files.size();
//            String id = "files number error";
//
//            if (files_nb == 1) {
//                id = files.get(0).getId();
//            }
//
//            Pair<Integer, String> result = new Pair<>(files_nb, id);
//
//            return result;
//
//        });
//    }

    public Task<String> uploadDriveFileService() {
        return Tasks.call(mExecutor, () -> {
            // Upload excel file on drive.
            File fileMetadata = new File();
            fileMetadata.setName(Param.DATA_FILE);
            // File's content.
            java.io.File filePath = new java.io.File(Param.DATA_PATH);
            // Specify media type and file-path for file.
            FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, filePath);
            try {
                File file = mDriveService.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .execute();
                System.out.println("File ID: " + file.getId());
                return file.getId();
            } catch (GoogleJsonResponseException e) {
                // TODO(developer) - handle error appropriately
                System.err.println("Unable to upload file: " + e.getDetails());
                throw e;
            }
        });
    }

    public Task<String> updateDriveFileService(String fileId) {
        return Tasks.call(mExecutor, () -> {
            // Update excel file on drive.
            File fileMetadata = new File();
            fileMetadata.setName(Param.DATA_FILE);
            // File's content.
            java.io.File filePath = new java.io.File(Param.DATA_PATH);
            // Specify media type and file-path for file.
            FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, filePath);
            try {
                File file = mDriveService.files().update(fileId, fileMetadata, mediaContent)
                        .setFields("id")
                        .execute();
                System.out.println("File ID: " + file.getId());
                return file.getId();
            } catch (GoogleJsonResponseException e) {
                // TODO(developer) - handle error appropriately
                System.err.println("Unable to upload file: " + e.getDetails());
                throw e;
            }
        });
    }



//    public Task<String> updateOrCreate(String fileId) {
//        System.out.println("Enter updateOrCreate");
//        return Tasks.call(mExecutor, () -> {
//            System.out.println("Enter return");
//            try {
//                System.out.println("Enter try");
//                File file = mDriveService.files().get(fileId).execute();
//                System.out.println(Param.FILE_ID);
//
//                if (!file.isEmpty()) {
//                    System.out.println("Not empty");
//                    return file.getId();
//                }
//                else {
//                    System.out.println("Empty");
//                    return "File empty";
//                }
//            }
//            catch (IOException e) {
//                Log.d("tag",e.getMessage());
//                System.out.println("Catch execption...");
//                return null;
//            }
//
//        });
//    }
//
//    public Task<String> updateFile(String fileId, String filePath) {
//        return Tasks.call(mExecutor, () -> {
//            try {
//                File file = new File();
//                java.io.File fileContent = new java.io.File(filePath);
//
//                file.setName(fileContent.getName())
//                        .setMimeType(Param.DATA_MIME_TYPE);
//
//                FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, fileContent);
//
//                File updatedFile = mDriveService.files().update(fileId, file, mediaContent).execute();
//
//
//                return updatedFile.getId();
//
//            }
//            catch (IOException e) {
//                Log.d("tag",e.getMessage());
//
//                return null;
//            }
//
//        });
//    }
//
//    public Task<String> createDataFile(Context context, String filePath) {
//        return Tasks.call(mExecutor, () -> {
//
//            System.out.println("filepath = " + filePath);
//            System.out.println("Param.DATA_FILE.substring(0, 5) = " + Param.DATA_FILE.substring(0, 5));
//            System.out.println("Arrays.asList(Param.FOLDER_ID) = " + Arrays.asList(Param.FOLDER_ID));
//            System.out.println("Param.FOLDER_ID = " + Param.FOLDER_ID);
//
//
//            File fileMetaData = new File();
//            fileMetaData
//                    .setName(Param.DATA_FILE.substring(0, 5))
//                    .setParents(Arrays.asList(Param.FOLDER_ID));
//
//            java.io.File file = new java.io.File(filePath);
//
//            FileContent mediaContent = new FileContent(Param.DATA_MIME_TYPE, file);
//
//            File myFile = null;
//
//            try {
//                myFile = mDriveService.files().create(fileMetaData, mediaContent).execute();
//                utils.setAndSaveFileID(context, myFile.getId());
//                System.out.println("ID : " + myFile.getId());
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            if (myFile == null) {
//                throw new IOException("Null result when requesting file creation");
//            }
//
//            return myFile.getId();
//
//        });
//    }

}