package com.guilsch.multivoc;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Iterator;

public class utils {

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
//            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (columnName);
    }

}