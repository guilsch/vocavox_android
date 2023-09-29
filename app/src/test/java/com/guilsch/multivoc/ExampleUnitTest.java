package com.guilsch.multivoc;

import org.junit.Test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
//        String dateString = "Mon Mar 20 21:55:57 EST 2023";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));


        try {
//            Date date = dateFormat.parse(dateString);
            ActivityTrain()

        } catch (ParseException e) {
            System.err.println("Erreur de parsing de la date : " + e.getMessage());
        }


    }

}