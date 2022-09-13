package com.guilsch.multivoc;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class utils {

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

    public static Date giveDate() {
        long now = System.currentTimeMillis();
        Date currentDate = toDate(now);
        return (currentDate);
    }

    public static int getHeaderIndex(Row header, String column) {

        Iterator<Cell> cellIterator = header.cellIterator();
        int index = -1;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            if (column.compareTo(cell.getStringCellValue()) == 0) {
                index = cell.getColumnIndex();
            }
        }

        if (index == -1) {
            System.out.println("No column named " + column);
        }

        return (index);
    }

    public static String getHeaderName(int columnIndex) {

        String columnName = "";

        try {
            FileInputStream file = new FileInputStream(new File("storage/emulated/0/Multivoc/fr_it.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();

            Iterator<Cell> cellIterator = header.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (columnIndex == cell.getColumnIndex()) {
                    columnName = cell.getStringCellValue();
                }
            }

            if (columnName.compareTo("") == 0) {
                System.out.println("No column at index " + columnIndex);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (columnName);
    }

    public static void prepareDataFile() {
        try {
            FileInputStream inputFile = new FileInputStream(new File(Param.getDataPath()));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();
            Cell currentCell;
            Cell stateCell;

            int item1Index = utils.getHeaderIndex(header, "Item 1");
            int item2Index = utils.getHeaderIndex(header, "Item 2");
            int stateIndex = utils.getHeaderIndex(header, "State");
            int packIndex = utils.getHeaderIndex(header, "Pack");
            int nextPracticeDateIndex = utils.getHeaderIndex(header, "Next Date");
            int repetitionsIndex = utils.getHeaderIndex(header, "Repetitions");
            int easinessFactorIndex = utils.getHeaderIndex(header, "Easiness Factor");
            int intervalIndex = utils.getHeaderIndex(header, "Interval");

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                if (row.getCell(item1Index) == null || row.getCell(item2Index) == null) {
                    stateCell = row.createCell(stateIndex);
                    stateCell.setCellValue(Param.INVALID);
                }

                currentCell = row.getCell(stateIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(stateIndex);
                    currentCell.setCellValue(Param.INACTIVE);
                }

                currentCell = row.getCell(packIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(packIndex);
                    currentCell.setCellValue(Param.DEFAULT_PACK);
                }

                currentCell = row.getCell(nextPracticeDateIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(nextPracticeDateIndex);
                    currentCell.setCellValue(Param.DEFAULT_DATE.toString());
                    System.out.println(Param.DEFAULT_DATE);
                }

                currentCell = row.getCell(repetitionsIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(repetitionsIndex);
                    currentCell.setCellValue(Param.DEFAULT_REP);
                }

                currentCell = row.getCell(easinessFactorIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(easinessFactorIndex);
                    currentCell.setCellValue(Param.DEFAULT_EF);
                }

                currentCell = row.getCell(intervalIndex);
                if (currentCell == null) {
                    currentCell = row.createCell(intervalIndex);
                    currentCell.setCellValue(Param.DEFAULT_INTER);
                }
            }

        inputFile.close();
        FileOutputStream outputStream = new FileOutputStream(Param.getDataPath());
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

}