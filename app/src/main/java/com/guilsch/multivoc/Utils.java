package com.guilsch.multivoc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class Utils {

    public static void printNBCards() {
        System.out.println("Cards to train :" + Param.GLOBAL_DECK.getCardsToReviewNb());
    }

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

            int item1Index = Utils.getFieldIndex(header, Param.ITEM1_FIELD_NAME);
            int item2Index = Utils.getFieldIndex(header, Param.ITEM2_FIELD_NAME);
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
            Param.USER_LANGUAGE = Utils.getLanguageNameFromISO(Param.USER_LANGUAGE_ISO);
        }
        else {
            Param.USER_LANGUAGE_ISO = Param.USER_LANGUAGE_ISO_DEFAULT;
            Param.USER_LANGUAGE = Param.USER_LANGUAGE_DEFAULT;
        }
    }

    /**
     * Initializes the global deck that will be used everywhere in the application
     */
    public static void initGlobalDeck() {
        // Set global deck
        Param.GLOBAL_DECK = new Deck();
        Param.GLOBAL_DECK.init();

        // Get deck data (nb of cards to review...)
        Param.GLOBAL_DECK.updateDeckDataVariables();
    }

    public static void setFileID () {
        switch (Param.TARGET_LANGUAGE) {
            case "English":
                Param.FILE_ID = Param.EN_FILE_ID;
                break;

            case "Deutsch":
                Param.FILE_ID = Param.GE_FILE_ID;
                break;

            case "Français":
                Param.FILE_ID = Param.FR_FILE_ID;
                break;

            case "Italiano":
                Param.FILE_ID = Param.IT_FILE_ID;
                break;

            case "Русский":
                Param.FILE_ID = Param.RU_FILE_ID;
                break;

            case "Español":
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

            case "Deutsch":
                Param.GE_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.GE_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Français":
                Param.FR_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.FR_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Italiano":
                Param.IT_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.IT_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Русский":
                Param.RU_FILE_ID = Param.FILE_ID_UNDEFINED;
                Pref.savePreference(context, Param.RU_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
                break;

            case "Español":
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

            case "Deutsch":
                Param.GE_FILE_ID = fileID;
                Pref.savePreference(context, Param.GE_FILE_ID_KEY, fileID);
                break;

            case "Français":
                Param.FR_FILE_ID = fileID;
                Pref.savePreference(context, Param.FR_FILE_ID_KEY, fileID);
                break;

            case "Italiano":
                Param.IT_FILE_ID = fileID;
                Pref.savePreference(context, Param.IT_FILE_ID_KEY, fileID);
                break;

            case "Русский":
                Param.RU_FILE_ID = fileID;
                Pref.savePreference(context, Param.RU_FILE_ID_KEY, fileID);
                break;

            case "Español":
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
        return Param.FILE_NAME_PREFIX + Param.TARGET_LANGUAGE_ISO + ".xlsx";
    }

    public static String getLanguageISOName(String language) {
        String languageStringName;

        switch (language) {
            case "English":
                languageStringName = "en";
                break;

            case "Deutsch":
                languageStringName = "de";
                break;

            case "Français":
                languageStringName = "fr";
                break;

            case "Italiano":
                languageStringName = "it";
                break;

            case "Русский":
                languageStringName = "ru";
                break;

            case "Español":
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
                languageStringName = "Deutsch";
                break;

            case "fr":
                languageStringName = "Français";
                break;

            case "it":
                languageStringName = "Italiano";
                break;

            case "ru":
                languageStringName = "Русский";
                break;

            case "es":
                languageStringName = "Español";
                break;

            default:
                languageStringName = "unknown";
                break;
        }

        return languageStringName;
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

    public static String universalToLocalDate(String dateString, String languageISO) {

        String formattedDate = dateString;

        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            SimpleDateFormat localFormat = new SimpleDateFormat("d MMMM yyyy", new Locale(languageISO));
            formattedDate = localFormat.format(originalFormat.parse(dateString));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;
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

    public static void initParam() {

        // Set user language from locale
        Utils.setUserLanguageParam();

        // Set target languages variables
        Param.TARGET_LANGUAGE_ISO = Utils.getLanguageISOName(Param.TARGET_LANGUAGE);

        // Set Data path
        Param.DATA_FILE = Utils.generateDataFileName();
        Param.setDataPath();

        // Set File ID
        Utils.setFileID();

        // Check data file
        if (!(new File(Param.DATA_PATH)).exists()) {
            System.out.println(Param.DATA_PATH + " doesn't exist yet");
            createDataFile();
        }
        else {
            System.out.println(Param.DATA_PATH + " already exists");
        }
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

            int item1Index = Utils.getFieldIndex(header, Param.ITEM1_FIELD_NAME);
            int item2Index = Utils.getFieldIndex(header, Param.ITEM2_FIELD_NAME);
            int stateIndex = Utils.getFieldIndex(header, Param.STATE_FIELD_NAME);
            int packIndex = Utils.getFieldIndex(header, Param.PACK_FIELD_NAME);
            int nextPracticeDateIndex = Utils.getFieldIndex(header, Param.NEXT_DATE_FIELD_NAME);
            int creationDateIndex = Utils.getFieldIndex(header, Param.CREATION_DATE_FIELD_NAME);
            int repetitionsIndex = Utils.getFieldIndex(header, Param.REPETITIONS_FIELD_NAME);
            int easinessFactorIndex = Utils.getFieldIndex(header, Param.EF_FIELD_NAME);
            int intervalIndex = Utils.getFieldIndex(header, Param.INTERVAL_FIELD_NAME);
            int uuidIndex = Utils.getFieldIndex(header, Param.UUID_FIELD_NAME);

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if (checkCellEmptiness(row.getCell(item1Index), row) || checkCellEmptiness(row.getCell(item2Index), row)) {
                    stateCell = row.createCell(stateIndex);
                    stateCell.setCellValue(Param.INVALID);
                }

                currentCell = row.getCell(stateIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(stateIndex);
                    currentCell.setCellValue(Param.DEFAULT_STATE);
                }

                currentCell = row.getCell(packIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(packIndex);
                    currentCell.setCellValue(Param.DEFAULT_PACK);
                }

                currentCell = row.getCell(nextPracticeDateIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(nextPracticeDateIndex);
                    currentCell.setCellValue(Param.DEFAULT_NEXT_DATE.toString());
                }

                currentCell = row.getCell(creationDateIndex);
                if (checkCellEmptiness(currentCell, row)) {
                    currentCell = row.createCell(creationDateIndex);
                    currentCell.setCellValue(Param.DEFAULT_CREATION_DATE.toString());
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
                    currentCell.setCellValue(Utils.getNewUUID());
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

    static void createDataFile() {
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


        // Create file with its parents folders
        File file = new File(Param.DATA_PATH);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);
            out.close();

            System.out.println(Param.DATA_PATH + " has been created");
        } catch (IOException e) {
            e.printStackTrace();

            // TODO inform the user
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

    /**
     * Returns a list containing all uuid from the cards in the provided queue
     */
    public static List<String> getUUIDListFromCardsQueue(Queue<Card> queue) {
        List<String> uuidList = new ArrayList<String>();

        Iterator<Card> iterator = queue.iterator();
        while (iterator.hasNext()) {
            uuidList.add(iterator.next().getUuid());
        }

        return uuidList;
    }

    /**
     * Get a list of cards containing all the cards in the queue
     * @param cardsQueue
     * @return
     */
    public static List<Card> getCardsListFromCardsQueue(Queue<Card> cardsQueue) {
        return Arrays.asList(cardsQueue.toArray(new Card[0]));
    }

    static void showToast(Context context, String message) {

        Toast toast = new Toast(context);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.toast_layout, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static Boolean checkConnexion(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static String formatPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /***
     * When a new card is created in translation or newCard activities, this method add it to the
     * deck and makes the necessary updates
     * @param newCard
     */
    public static void manageCardCreation(Card newCard) {
        Param.GLOBAL_DECK.add(newCard);
        newCard.addToDatabaseOnSeparateThread();
        newCard.info();


        // Update deck data
        Param.CARDS_NB = Param.CARDS_NB + 1;
        if(newCard.getState() == Param.TO_LEARN) {
            Param.CARDS_TO_LEARN_NB = Param.CARDS_TO_LEARN_NB + 1;
        }
    }

    // Thread management

    /**
     * Update the card in the database on a seperate thread. Don't use it repeatedly (not in train
     * or learn session)
     * @param card
     */
    public static void updateInDatabaseOnSeparateThreadOneShot(Card card) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                card.updateInDatabase();
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    /**
     * Same as previous but use one thread for different updates. Use this method in train or learn
     * activities. Don't forget to close the singleThreadExecutor (use thread shutdown) after that.
     * @param singleThreadExecutor
     * @param card
     */
    public static void updateInDatabaseOnSeparateThreadMultiShot(ExecutorService singleThreadExecutor, Card card) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                card.updateInDatabase();
            }
        });
    }

    public static void threadShutdown(ExecutorService singleThreadExecutor) {
        singleThreadExecutor.shutdown();
    }

    // File save

    /**
     * Create a copy of the data file in case of file corruption
     */
    public static void saveTempDataFile() {

        String tempSavedFilePath = Param.DATA_PATH
                .replace(".xlsx", "_temp.xlsx")
                .replace(Param.FILE_NAME_PREFIX, "." + Param.FILE_NAME_PREFIX);

        File originalFile = new File(Param.DATA_PATH);
        File tempSavedFile = new File(tempSavedFilePath);

        try {
            if (originalFile.exists()) {
                FileChannel sourceChannel = new FileInputStream(originalFile).getChannel();
                FileChannel destinationChannel = new FileOutputStream(tempSavedFile).getChannel();
                destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

                sourceChannel.close();
                destinationChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Debug

    public static void writeDebugLine(String line) {
        try {
            FileWriter writer = new FileWriter(Param.FOLDER_PATH + Param.DEBUG_FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.newLine();
            bufferedWriter.write(line);

            bufferedWriter.close();
            writer.close();

            System.out.println("Successfully written in debug file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Manage text color on click
    public static void setTextViewTextColorChangeOnTouch(final TextView button, final int pressedColor, final int normalColor) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setTextColor(ContextCompat.getColor(button.getContext(), pressedColor));
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setTextColor(ContextCompat.getColor(button.getContext(), normalColor));
                }
                return false;
            }
        });
    }
}