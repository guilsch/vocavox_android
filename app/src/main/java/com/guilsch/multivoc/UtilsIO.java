//package com.guilsch.multivoc;
//
//import android.Manifest;
//import android.content.Context;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileOutputStream;
//
//import permissions.dispatcher.NeedsPermission;
//import permissions.dispatcher.OnNeverAskAgain;
//import permissions.dispatcher.OnPermissionDenied;
//import permissions.dispatcher.OnShowRationale;
//import permissions.dispatcher.PermissionRequest;
//
//
//public class UtilsIO {
//
//    public static void createDataFile(Context context) {
//        // Demander la permission au moment de l'exécution
//        UtilsIOPermissionsDispatcher.createDataFileCoreWithPermissionCheck(context);
//    }
//
//    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    static void createDataFileCore(Context context) {
//        // workbook object
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        // spreadsheet object
//        XSSFSheet sheet = workbook.createSheet(Param.USER_LANGUAGE + " - " + Param.TARGET_LANGUAGE + " vocabulary");
//
//        // creating a row object
//        XSSFRow header = sheet.createRow(0);
//
//        int cellid = 0;
//
//        for (Object obj : Param.FIELDS) {
//            Cell cell = header.createCell(cellid++);
//            cell.setCellValue((String)obj);
//        }
//
//
//        // Create file with its parents folders
//        File file = new File(Param.DATA_PATH);
//
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
//
//        FileOutputStream out = new FileOutputStream(file);
//
//        workbook.write(out);
//        out.close();
//
//        System.out.println(Param.DATA_PATH + " has been created");
//    }
//
//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    static void showRationaleForStorage(PermissionRequest request) {
//        // Montrer une explication à l'utilisateur si nécessaire
//        request.proceed();
//    }
//
//    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    static void onStoragePermissionDenied() {
//        // Gérer le refus de la permission
//    }
//
//    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    static void onStorageNeverAskAgain() {
//        // Gérer le cas où "Ne plus demander" est coché lors du refus
//    }
//
//    public static void onRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResults) {
//        // Passer les résultats à PermissionsDispatcher
//        UtilsIOPermissionsDispatcher.onRequestPermissionsResult(context, requestCode, grantResults);
//    }
//}
