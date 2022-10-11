package com.guilsch.multivoc;

import android.content.Context;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class utils {

//    public static List<Integer> getFieldsIndexFromHeader(Row header) {
//        List<Integer> indexList = new ArrayList<Integer>(Param.FIELDS_NB);
//
//        for (ListIterator<String> fieldsListIterator = Param.FIELDS.listIterator(); fieldsListIterator.hasNext(); ) {
//            indexList.add(utils.getHeaderIndex(header, fieldsListIterator.next()));
//        }
//
//        return indexList;
//    }

    public static void cleanDataFile() {
        try {
            FileInputStream inputFile = new FileInputStream(new File(Param.DATA_PATH));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            List<Integer> rowsToRemoveIndex = new ArrayList<Integer>();

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();
            Row row;

            int item1Index = utils.getFieldIndex(header, Param.ITEM1_FIELD_NAME);
            int item2Index = utils.getFieldIndex(header, Param.ITEM2_FIELD_NAME);
            int rowIndex;

            while (rowIterator.hasNext()) {

                row = rowIterator.next();
                Cell item1Cell = row.getCell(item1Index);
                Cell item2Cell = row.getCell(item2Index);

                if (item1Cell == null || item2Cell == null) {
//                    System.out.println("Removed : " + row.getRowNum());
//                    sheet.removeRow(row);
                    rowsToRemoveIndex.add(row.getRowNum());
                }
            }

            Iterator<Integer> rowToRemoveIndexIterator = rowsToRemoveIndex.iterator();
            while (rowToRemoveIndexIterator.hasNext()) {
                rowIndex = rowToRemoveIndexIterator.next();
                sheet.removeRow(sheet.getRow(rowIndex));
                System.out.println("Removed : " + rowIndex);
            }

            inputFile.close();
            FileOutputStream outputStream = new FileOutputStream(Param.DATA_PATH);
            workbook.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void initParam() {

        // Set languages variables
        Param.USER_LANGUAGE_ISO = utils.getLanguageISOName(Param.USER_LANGUAGE);
        Param.TARGET_LANGUAGE_ISO = utils.getLanguageISOName(Param.TARGET_LANGUAGE);

        // Set Data path
        Param.DATA_FILE = utils.generateDataFileName();
        Param.setDataPath();

        // Set File ID
        utils.setFileID();
    }

    public static void setFileID () {
        switch (Param.TARGET_LANGUAGE) {
            case "English":
                Param.FILE_ID = Param.EN_FILE_ID;
                break;

            case "German":
                Param.FILE_ID = Param.GE_FILE_ID;
                break;

            case "French":
                Param.FILE_ID = Param.FR_FILE_ID;
                break;

            case "Italian":
                Param.FILE_ID = Param.IT_FILE_ID;
                break;

            case "Russian":
                Param.FILE_ID = Param.RU_FILE_ID;
                break;

            case "Spanish":
                Param.FILE_ID = Param.SP_FILE_ID;
                break;

            default:
                Param.FILE_ID = Param.FILE_ID_UNDEFINED;
                break;
        }
    }

    public static void resetFileID (Context context) {
        switch (Param.TARGET_LANGUAGE) {
            case "English":
                Param.EN_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.EN_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "German":
                Param.GE_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.GE_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "French":
                Param.FR_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.FR_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Italian":
                Param.IT_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.IT_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Russian":
                Param.RU_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.RU_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Spanish":
                Param.SP_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.SP_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            default:
                System.out.println("Target language error");
                break;
        }

        Param.FILE_ID = Param.FILE_ID_UNDEFINED;

    }

    public static void setAndSaveFileID (Context context, String fileID) {
        switch (Param.TARGET_LANGUAGE) {
            case "English":
                Param.EN_FILE_ID = fileID;
                Pref.savePreference(context, Param.EN_FILE_ID_KEY, fileID);
                break;

            case "German":
                Param.GE_FILE_ID = fileID;
                Pref.savePreference(context, Param.GE_FILE_ID_KEY, fileID);
                break;

            case "French":
                Param.FR_FILE_ID = fileID;
                Pref.savePreference(context, Param.FR_FILE_ID_KEY, fileID);
                break;

            case "Italian":
                Param.IT_FILE_ID = fileID;
                Pref.savePreference(context, Param.IT_FILE_ID_KEY, fileID);
                break;

            case "Russian":
                Param.RU_FILE_ID = fileID;
                Pref.savePreference(context, Param.RU_FILE_ID_KEY, fileID);
                break;

            case "Spanish":
                Param.SP_FILE_ID = fileID;
                Pref.savePreference(context, Param.SP_FILE_ID_KEY, fileID);
                break;

            default:
                System.out.println("Error : target language unknown");
                break;
        }

        Param.FILE_ID = fileID;
    }

    public static String generateDataFileName () {
        return Param.USER_LANGUAGE_ISO + "_" + Param.TARGET_LANGUAGE_ISO + ".xlsx";
    }

    public static String getLanguageISOName(String language) {
        String languageStringName;

        switch (language) {
            case "English":
                languageStringName = "en";
                break;

            case "German":
                languageStringName = "de";
                break;

            case "French":
                languageStringName = "fr";
                break;

            case "Italian":
                languageStringName = "it";
                break;

            case "Russian":
                languageStringName = "ru";
                break;

            case "Spanish":
                languageStringName = "es";
                break;

            default:
                languageStringName = "unknown";
                break;
        }

        return languageStringName;
    }

    public static void createDataFile () throws IOException {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object
        XSSFSheet sheet = workbook.createSheet(Param.USER_LANGUAGE + " - " + Param.TARGET_LANGUAGE + " vocabulary");

        // creating a row object
        XSSFRow header = sheet.createRow(0);

        int cellid = 0;

        for (Object obj : Param.FIELDS) {
            Cell cell = header.createCell(cellid++);
            cell.setCellValue((String)obj);
        }

        FileOutputStream out = new FileOutputStream(new File(Param.DATA_PATH));

        workbook.write(out);
        out.close();
    }

    public static int nextStateForButton (int currentState) {
        int nextState;

        switch (currentState) {

            case Param.INACTIVE:
                nextState = Param.TO_LEARN;
                break;

            case Param.ACTIVE:
                nextState = Param.STOP_LEARNING;
                break;

            case Param.TO_LEARN:
                nextState = Param.INACTIVE;
                break;

            case Param.STOP_LEARNING:
                nextState = Param.ACTIVE;
                break;

            default:
                nextState = Param.INACTIVE;
                break;

        }

        return nextState;
    }

    public static String getStringState (int state) {

        String stringState;

        switch (state){

            case Param.ACTIVE :
                stringState = "Learning";
                break;

            case Param.INACTIVE :
                stringState = "Inactive";
                break;

            case Param.TO_LEARN :
                stringState = "To Learn";
                break;

            case Param.INVALID:
                stringState = "Invalid";
                break;

            case Param.STOP_LEARNING:
                stringState = "On pause";
                break;

            default:
                stringState = "Error";
                break;
        }

        return stringState;
    }

    public static Date toDate(long nextPracticeTime) {
        Date nextPracticeDate = new Date(nextPracticeTime);
        return (nextPracticeDate);
    }

    public static Date giveCurrentDate() {
        long now = System.currentTimeMillis();
        Date currentDate = toDate(now);
        return (currentDate);
    }

    public static int getFieldIndex(Row header, String field) {

        Iterator<Cell> cellIterator = header.cellIterator();
        int index = -1;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (field.compareTo(cell.getStringCellValue()) == 0) {
                index = cell.getColumnIndex();
            }
        }

        if (index == -1) {
            System.out.println("No field named : " + field);
            index = header.getLastCellNum();
            header.createCell(index).setCellValue(field);
            System.out.println("Added new field : " + field);
        }

        return (index);
    }

//    public static String getHeaderName(int columnIndex) {
//
//        String columnName = "";
//
//        try {
//            FileInputStream file = new FileInputStream(new File(Param.DATA_PATH));
//            HSSFWorkbook workbook = new HSSFWorkbook(file);
//            HSSFSheet sheet = workbook.getSheetAt(0);
//
//            // Iterate through each rows one by one
//            Iterator<Row> rowIterator = sheet.iterator();
//            Row header = rowIterator.next();
//
//            Iterator<Cell> cellIterator = header.cellIterator();
//
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//
//                if (columnIndex == cell.getColumnIndex()) {
//                    columnName = cell.getStringCellValue();
//                }
//            }
//
//            if (columnName.compareTo("") == 0) {
//                System.out.println("No column at index " + columnIndex);
//            }
//
//            file.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return (columnName);
//    }

    public static void prepareDataFile() {
        try {
            FileInputStream inputFile = new FileInputStream(new File(Param.DATA_PATH));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();
            Cell currentCell;
            Cell stateCell;

            int item1Index = utils.getFieldIndex(header, Param.ITEM1_FIELD_NAME);
            int item2Index = utils.getFieldIndex(header, Param.ITEM2_FIELD_NAME);
            int stateIndex = utils.getFieldIndex(header, Param.STATE_FIELD_NAME);
            int packIndex = utils.getFieldIndex(header, Param.PACK_FIELD_NAME);
            int nextPracticeDateIndex = utils.getFieldIndex(header, Param.NEXT_DATE_FIELD_NAME);
            int repetitionsIndex = utils.getFieldIndex(header, Param.REPETITIONS_FIELD_NAME);
            int easinessFactorIndex = utils.getFieldIndex(header, Param.EF_FIELD_NAME);
            int intervalIndex = utils.getFieldIndex(header, Param.INTERVAL_FIELD_NAME);
            int uuidIndex = utils.getFieldIndex(header, Param.UUID_FIELD_NAME);

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if (checkCellEmptiness(row.getCell(item1Index), row) || checkCellEmptiness(row.getCell(item2Index), row)) {
                    stateCell = row.createCell(stateIndex);
                    stateCell.setCellValue(Param.INVALID);
                }

                currentCell = row.getCell(stateIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(stateIndex);
                    currentCell.setCellValue(Param.INACTIVE);
                }

                currentCell = row.getCell(packIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(packIndex);
                    currentCell.setCellValue(Param.DEFAULT_PACK);
                }

                currentCell = row.getCell(nextPracticeDateIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(nextPracticeDateIndex);
                    currentCell.setCellValue(Param.DEFAULT_DATE.toString());
                }

                currentCell = row.getCell(repetitionsIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(repetitionsIndex);
                    currentCell.setCellValue(Param.DEFAULT_REP);
                }

                currentCell = row.getCell(easinessFactorIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(easinessFactorIndex);
                    currentCell.setCellValue(Param.DEFAULT_EF);
                }

                currentCell = row.getCell(intervalIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(intervalIndex);
                    currentCell.setCellValue(Param.DEFAULT_INTER);
                }

                currentCell = row.getCell(uuidIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(uuidIndex);
                    currentCell.setCellValue(utils.getNewUUID());
                }
            }

        inputFile.close();
        FileOutputStream outputStream = new FileOutputStream(Param.DATA_PATH);
        workbook.write(outputStream);
        outputStream.close();

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getNewUUID() {
        return UUID.randomUUID().toString();
    }

    public static Boolean checkCellEmptiness(Cell cell, Row row) {
        if (cell == null) {
            return Boolean.TRUE;
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            row.removeCell(cell);
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    public static String URLtoID(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}