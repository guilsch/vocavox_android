package com.guilsch.multivoc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

public class Card implements Serializable {

    private String item1;
    private String item2;
    private int repetitions;
    private float easinessFactor;
    private int interval;
    private Date nextPracticeDate;
    private Boolean active;
    private String pack;

    private int numFields;

    Card (String item1, String item2, Boolean active, String pack, Date nextPracticeDate, int repetitions, float easinessFactor, int interval){
        this.item1 = item1;
        this.item2 = item2;
        this.repetitions = repetitions;
        this.easinessFactor = easinessFactor;
        this.interval = interval;
        this.nextPracticeDate = nextPracticeDate;
        this.active = active;
        this.pack = pack;
        this.numFields = 8;
    }

    Card() {
        this.item1 = "item 1";
        this.item2 = "item 2";
        this.repetitions = 0;
        this.easinessFactor = (float) 2.3;
        this.interval = 1;
        this.nextPracticeDate = utils.giveDate();
        this.active = Boolean.TRUE;
        this.pack = "Default";
    }

    public void updateParameters(Date nextPracticeDate, int repetitions, float easinessFactor, int interval) {
        this.setNextPracticeDate(nextPracticeDate);
        this.setRepetitions(repetitions);
        this.setEasinessFactor(easinessFactor);
        this.setInterval(interval);
    }

    public void updateDatabase(String cardKey) {
        try {

            FileInputStream inputFile = new FileInputStream(new File(Constants.getDataPath()));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();
            
            int item1Index = utils.getHeaderIndex(header, "Item 1");
            int item2Index = utils.getHeaderIndex(header, "Item 2");
            int activeIndex = utils.getHeaderIndex(header, "Active");
            int packIndex = utils.getHeaderIndex(header, "Pack");
            int nextPracticeDateIndex = utils.getHeaderIndex(header, "Next Date");
            int repetitionsIndex = utils.getHeaderIndex(header, "Repetitions");
            int easinessFactorIndex = utils.getHeaderIndex(header, "Easiness Factor");
            int intervalIndex = utils.getHeaderIndex(header, "Interval");

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Cell keyCell = row.getCell(0);

                if (keyCell.getStringCellValue().compareTo(cardKey) == 0){
                    
                    row.getCell(item1Index).setCellValue(this.item1);
                    row.getCell(item2Index).setCellValue(this.item2);
                    row.getCell(activeIndex).setCellValue(this.active.toString());
                    row.getCell(packIndex).setCellValue(this.pack);
                    row.getCell(nextPracticeDateIndex).setCellValue(this.nextPracticeDate.toString());
                    row.getCell(repetitionsIndex).setCellValue(this.repetitions);
                    row.getCell(easinessFactorIndex).setCellValue(this.easinessFactor);
                    row.getCell(intervalIndex).setCellValue(this.interval);
                }
            }

            // workbook.close();
            inputFile.close();

            FileOutputStream outputStream = new FileOutputStream(Constants.getDataPath());
            workbook.write(outputStream);
//            workbook.close();
            outputStream.close();
            
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToDatabase() {
        try {

            FileInputStream inputFile = new FileInputStream(new File(Constants.getDataPath()));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            Row header = sheet.getRow(sheet.getFirstRowNum());
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);

            System.out.println("Last row num :");
            System.out.println(sheet.getLastRowNum());

            int item1Index = utils.getHeaderIndex(header, "Item 1");
            int item2Index = utils.getHeaderIndex(header, "Item 2");
            int activeIndex = utils.getHeaderIndex(header, "Active");
            int packIndex = utils.getHeaderIndex(header, "Pack");
            int nextPracticeDateIndex = utils.getHeaderIndex(header, "Next Date");
            int repetitionsIndex = utils.getHeaderIndex(header, "Repetitions");
            int easinessFactorIndex = utils.getHeaderIndex(header, "Easiness Factor");
            int intervalIndex = utils.getHeaderIndex(header, "Interval");

            for (int i = 0; i < numFields; i++) {
                newRow.createCell(i);
            }

            newRow.getCell(item1Index).setCellValue(this.item1);
            newRow.getCell(item2Index).setCellValue(this.item2);
            newRow.getCell(activeIndex).setCellValue(this.active.toString());
            newRow.getCell(packIndex).setCellValue(this.pack);
            newRow.getCell(nextPracticeDateIndex).setCellValue(this.nextPracticeDate.toString());
            newRow.getCell(repetitionsIndex).setCellValue(this.repetitions);
            newRow.getCell(easinessFactorIndex).setCellValue(this.easinessFactor);
            newRow.getCell(intervalIndex).setCellValue(this.interval);

            inputFile.close();
            FileOutputStream outputStream = new FileOutputStream(Constants.getDataPath());
            workbook.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void info() {
        System.out.println("Item 1 : " + this.item1);
        System.out.println("Item 2 : " + this.item2);
        System.out.println(String.format("Active : %b", this.active));
        System.out.println("Pack : " + this.pack);
        System.out.println(String.format("Next practice : %s", nextPracticeDate.toString()));
        System.out.println(String.format("Repetitions : %d", this.repetitions));
        System.out.println(String.format("Easiness Factor : %.2f", this.easinessFactor));
        System.out.println(String.format("Interval : %d", this.interval));
    }

    // Getters

    public String getItem1() {
        return item1;
    }

    public String getItem2() {
        return item2;
    }

    public Boolean getActive() {
        return active;
    }

    public String getPack() {
        return pack;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public float getEasinessFactor() {
        return easinessFactor;
    }

    public int getInterval() {
        return interval;
    }

    public Date getNextPracticeDate() {
        return nextPracticeDate;
    }

    // public String getNextPracticeDateInString() {
    //     SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    //     String nextPracticeDateInString = formatter.format(this.nextPracticeDate);
    //     return nextPracticeDateInString;
    // }

    // Setters

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setEasinessFactor(float easinessFactor) {
        this.easinessFactor = easinessFactor;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public void setNextPracticeDate(Date nextPracticeDate) {
        this.nextPracticeDate = nextPracticeDate;
    }

}
