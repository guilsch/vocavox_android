//package com.guilsch.multivoc;
//
//import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
//import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.urlshortener.Urlshortener;
//import com.google.api.services.urlshortener.UrlshortenerScopes;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//
//
//
///* Class to demonstrate use of Drive insert file API */
//public class Upload {
//
//    /**
//     * Upload new file.
//     *
//     * @return Inserted file metadata if successful, {@code null} otherwise.
//     * @throws IOException if service account credentials file not found.
//     */
//    public static String upload() throws IOException {
//
//        newUrlshortener();
//
//        // Load pre-authorized user credentials from the environment.
//        // TODO(developer) - See https://developers.google.com/identity for
//        // guides on implementing OAuth2 for your application.
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//                credentials);
//
//        // Build a new authorized API client service.
//        Drive service = new Drive.Builder(new NetHttpTransport(),
//                GsonFactory.getDefaultInstance(),
//                requestInitializer)
//                .setApplicationName("Multivoc Drive")
//                .build();
//        // Upload file photo.jpg on drive.
//        File fileMetadata = new File();
//        fileMetadata.setName("fr_it.xlsx");
//        // File's content.
//        java.io.File filePath = new java.io.File("storage/emulated/0/Multivoc/fr_it.xlsx");
//        // Specify media type and file-path for file.
//        FileContent mediaContent = new FileContent("spreadsheet/xlsx", filePath);
//        try {
//            File file = service.files().create(fileMetadata, mediaContent)
//                    .setFields("id")
//                    .execute();
//            System.out.println("File ID: " + file.getId());
//            return file.getId();
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            System.err.println("Unable to upload file: " + e.getDetails());
//            throw e;
//        }
//    }
//
//    static Urlshortener newUrlshortener() {
//        AppIdentityCredential credential =
//                new AppIdentityCredential(
//                        Collections.singletonList(UrlshortenerScopes.URLSHORTENER));
//        return new Urlshortener.Builder(new UrlFetchTransport(),
//                GsonFactory.getDefaultInstance(),
//                credential)
//                .build();
//    }
//}