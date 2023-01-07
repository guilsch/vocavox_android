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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class utils {

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

    public static void setUserLanguageParam(){
        Locale userLocale = Locale.getDefault();
        String localeLanguageISO = userLocale.getLanguage();

        if (Arrays.asList(Param.USER_LANGUAGES_ISO).contains(localeLanguageISO)) {
            Param.USER_LANGUAGE_ISO = localeLanguageISO;
            Param.USER_LANGUAGE = utils.getLanguageNameFromISO(Param.USER_LANGUAGE_ISO);
        }
        else {
            Param.USER_LANGUAGE_ISO = Param.USER_LANGUAGE_ISO_DEFAULT;
            Param.USER_LANGUAGE = Param.USER_LANGUAGE_DEFAULT;
        }
    }

    public static void initParam() {

        // Set user language from locale
        setUserLanguageParam();

        // Set target languages variables
        Param.TARGET_LANGUAGE_ISO = utils.getLanguageISOName(Param.TARGET_LANGUAGE);

        // Set Data path
        Param.DATA_FILE = utils.generateDataFileName();
        Param.setDataPath();

        // Set File ID
        utils.setFileID();

        // Check data file
        if (!(new File(Param.DATA_PATH)).exists()) {
            System.out.println(Param.DATA_PATH + " doesn't exist yet");
            try {
                utils.createDataFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(Param.DATA_PATH + " already exists");
        }

        // Set global deck
        Param.GLOBAL_DECK = new Deck();
        Param.GLOBAL_DECK.init();

        // Get deck data (nb of cards to review...)
        updateDeckDataVariables(Param.GLOBAL_DECK);

    }

    public static void updateDeckDataVariables(Deck deck) {
        Param.CARDS_TO_REVIEW_NB = deck.getCardsToReviewNb();
        Param.CARDS_NB = deck.size();
        Param.ACTIVE_CARDS_NB = deck.getCardsWithStateSNb(1);
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
        return "words_database_" + Param.TARGET_LANGUAGE_ISO + ".xlsx";
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

    public static String getLanguageNameFromISO(String languageISO) {
        String languageStringName;

        switch (languageISO) {
            case "en":
                languageStringName = "English";
                break;

            case "de":
                languageStringName = "German";
                break;

            case "fr":
                languageStringName = "French";
                break;

            case "it":
                languageStringName = "Italian";
                break;

            case "ru":
                languageStringName = "Russian";
                break;

            case "es":
                languageStringName = "Spanish";
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

    public static String getStringStateFromInt(int state) {

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

    public static int getIntStateFromString (String state) {

        int intState;

        switch (state){

            case  "Learning":
                intState = Param.ACTIVE;
                break;

            case "Inactive" :
                intState = Param.INACTIVE;
                break;

            case "To Learn" :
                intState = Param.TO_LEARN;
                break;

            case "Invalid":
                intState = Param.INVALID;
                break;

            case "On pause":
                intState = Param.STOP_LEARNING;
                break;

            default:
                intState = -1;
                break;
        }

        return intState;
    }

    public static Date toDate(long nextPracticeTime) {
        Date nextPracticeDate = new Date(nextPracticeTime);
        return (nextPracticeDate);
    }

    public static Date giveCurrentDate() {
        long now = System.currentTimeMillis();
        Date currentDate = toDate(now);
        return (currentDate);

        // A voir avec LocalDate.now() (java.time.LocalDate)
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

//    public static boolean tryAndCheckParseFloat(String value) {
//        try {
//            double val = Float.parseFloat(value);
//            if (val >= 0 && val <=1) {
//                return true;
//            }
//            else {
//                return false;
//            }
//
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
}