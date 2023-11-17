//package com.guilsch.multivoc;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.Settings;
//import android.util.Log;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class UtilsPermissions extends AppCompatActivity {
//
//    private static final int STORAGE_PERMISSION_CODE = 23;
//    private Activity activity;
//
//    public UtilsPermissions(Activity activity) {
//        this.activity = activity;
//    }
//
//    public boolean checkStoragePermissions(Activity activity){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            //Android is 11 (R) or above
//            return Environment.isExternalStorageManager();
//        }else {
//            //Below android 11
//            int write = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int read = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
//        }
//    }
//
//    public void requestForStoragePermissions(Activity activity) {
//        //Android is 11 (R) or above
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            try {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                intent.setData(uri);
//                storageActivityResultLauncher.launch(intent);
//            }catch (Exception e){
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                storageActivityResultLauncher.launch(intent);
//            }
//        }else{
//            //Below android 11
//            ActivityCompat.requestPermissions(
//                    activity,
//                    new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    },
//                    STORAGE_PERMISSION_CODE
//            );
//        }
//    }
//
////    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
////            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
////                    new ActivityResultCallback<ActivityResult>(){
////
////                        @Override
////                        public void onActivityResult(ActivityResult o) {
////                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
////                                //Android is 11 (R) or above
////                                if(Environment.isExternalStorageManager()){
////                                    //Manage External Storage Permissions Granted
////                                    Log.d("UtilsPermissions", "Storage Permissions Granted");
////                                } else {
////                                    Log.d("UtilsPermissions", "Storage Permissions Denied");
////                                }
////                            }else{
////                                //Below android 11
////
////                            }
////                        }
////                    });
//
//    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                    result -> handleStoragePermissionResult(result));
//
//    private void handleStoragePermissionResult(ActivityResult result) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Android is 11 (R) or above
//            if (Environment.isExternalStorageManager()) {
//                Log.d("UtilsPermissions", "Storage Permissions Granted");
//            } else {
//                Log.d("UtilsPermissions", "Storage Permissions Denied");
//            }
//        } else {
//            // Below android 11
//            // Handle accordingly if needed
//        }
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if(requestCode == STORAGE_PERMISSION_CODE){
////            if(grantResults.length > 0){
////                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
////                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
////
////                if(read && write){
////                    Log.d("UtilsPermissions", "Storage Permissions Granted");
////                } else {
////                    Log.d("UtilsPermissions", "Storage Permissions Denied");
////                }
////            }
////        }
////    }
//}
