//package com.guilsch.multivoc;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.util.Log;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.core.content.ContextCompat;
//import androidx.documentfile.provider.DocumentFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.channels.FileChannel;
//
//public class FileUtil {
//
//    // Code de demande pour lancer l'activité de sélection du répertoire
//    private static final int REQUEST_CODE_INTENT_GET_DIRECTORY = 123;
//
//    // Utilisé pour lancer l'activité de sélection du répertoire
//    private static final ActivityResultLauncher<Intent> selectDirectoryLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//                            if (data != null) {
//                                Uri treeUri = data.getData();
//                                copyFilesFromExternalToApp(treeUri);
//                            }
//                        }
//                    });
//
//    // Méthode pour ouvrir l'activité de sélection du répertoire
//    public static void openDirectoryChooser(Activity activity) {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        selectDirectoryLauncher.launch(intent);
//    }
//
//    // Méthode pour copier les fichiers du répertoire sélectionné vers le répertoire de l'application
//    private static void copyFilesFromExternalToApp(Uri treeUri) {
//        DocumentFile pickedDir = DocumentFile.fromTreeUri(App.getContext(), treeUri);
//        if (pickedDir != null && pickedDir.isDirectory()) {
//            // Répertoire de l'application
//            File appDirectory = App.getContext().getFilesDir();
//
//            // Parcourir les fichiers du répertoire sélectionné
//            for (DocumentFile file : pickedDir.listFiles()) {
//                if (file.isFile()) {
//                    copyFileToAppDirectory(file, appDirectory);
//                }
//            }
//        }
//    }
//
//    // Méthode pour copier un fichier vers le répertoire de l'application
//    private static void copyFileToAppDirectory(DocumentFile sourceFile, File appDirectory) {
//        try {
//            File destinationFile = new File(appDirectory, sourceFile.getName());
//            try (FileChannel source = new FileInputStream(sourceFile.getUri().getPath()).getChannel();
//                 FileChannel destination = new FileOutputStream(destinationFile).getChannel()) {
//                // Copier le fichier
//                destination.transferFrom(source, 0, source.size());
//                Log.d("FileUtil", "File copied: " + destinationFile.getAbsolutePath());
//            }
//        } catch (IOException e) {
//            Log.e("FileUtil", "Error copying file: " + e.getMessage());
//        }
//    }
//}
