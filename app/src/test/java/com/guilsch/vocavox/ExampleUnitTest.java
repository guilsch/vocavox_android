package com.guilsch.vocavox;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        String dateString = "Tue Dec 06 01:39:54 GMT+00:00 2022";

        String formattedDate = universalToLocalDate(dateString, "rolp");

        System.out.println(formattedDate);

    }

    public String universalToLocalDate(String dateString, String languageISO) {

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
}