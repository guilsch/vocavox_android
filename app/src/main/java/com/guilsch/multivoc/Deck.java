package com.guilsch.multivoc;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.Predicate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Deck extends ArrayList<Card> {

    Deck() {
        super(1);
    }

    public void deleteCard(String cardUuid) {
        try {

            FileInputStream inputFile = new FileInputStream(new File(Param.DATA_PATH));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();

            int uuidIndex = utils.getFieldIndex(header, Param.UUID_FIELD_NAME);

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Cell uuidCell = row.getCell(uuidIndex);

                if (uuidCell.getStringCellValue().compareTo(cardUuid) == 0){
                    sheet.removeRow(row);
                    break;
                }
            }

            inputFile.close();
            FileOutputStream outputStream = new FileOutputStream(Param.DATA_PATH);
            workbook.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

        utils.cleanDataFile();
        // Adapted from
        // https://howtodoinjava.com/java/library/readingwriting-excel-files-in-java-poi-tutorial/
        try {
            // Create date formater
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

            FileInputStream file = new FileInputStream(new File(Param.DATA_PATH));

            // Create Workbook instance holding reference to excel file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();

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

                Cell currentCell = row.getCell(0);

                if (currentCell != null && row.getCell(stateIndex).getNumericCellValue() != Param.INVALID) {

                    String item1 = row.getCell(item1Index).getStringCellValue();
                    String item2 = row.getCell(item2Index).getStringCellValue();
                    int state = (int) row.getCell(stateIndex).getNumericCellValue();
                    String pack = row.getCell(packIndex).getStringCellValue();
                    Date nextPracticeDate = formatter.parse(row.getCell(nextPracticeDateIndex).getStringCellValue());
                    int repetitions = (int) row.getCell(repetitionsIndex).getNumericCellValue();
                    float easinessFactor = (float) row.getCell(easinessFactorIndex).getNumericCellValue();
                    int interval = (int) row.getCell(intervalIndex).getNumericCellValue();
                    String uuid = row.getCell(uuidIndex).getStringCellValue();

                    this.add(new Card(item1, item2, state, pack, nextPracticeDate, repetitions, easinessFactor, interval, uuid));

                }
            }

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCards() {

        System.out.println("Cards in deck :");
        Iterator<Card> cardIterator = this.iterator();

        while(cardIterator.hasNext()){
            Card card = cardIterator.next();
            System.out.println("Item 1 : " + card.getItem1() + 
            "  |   Item 2 : " + card.getItem2() + 
            "  |   State : " + card.getState() +
            "  |   Pack : " + card.getPack() + 
            "  |   Next practice date : " + card.getNextPracticeDate() + 
            "  |   Repetitions : " + card.getRepetitions() + 
            "  |   Easiness factor : " + card.getEasinessFactor() + 
            "  |   Interval : " + card.getInterval() +
            "  |   UUID : " + card.getUuid());
        }
    }

    public void keepNFirst(int N) {
        if (this.size() > N) {
            this.subList(0, N);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filter() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getInterval() > 3;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterToTrain() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getNextPracticeDate().compareTo(utils.giveCurrentDate()) < 0 && x.getState() == Param.ACTIVE ;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterToLearn() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getState() == Param.TO_LEARN;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterActive() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getState() == Param.ACTIVE;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    public int getCardsWithStateSNb(int S) {
        int count = 0;
        for (Card card : this) {
            if (card.getState() == S) {
                count++;
            }
        }
        return count;
    }

    public int getCardsToReviewNb() {
        int count = 0;
        for (Card card : this) {
            if (card.getNextPracticeDate().before(utils.giveCurrentDate()) && card.getState() == 1) {
                count++;
            }
        }
        return count;
    }
}