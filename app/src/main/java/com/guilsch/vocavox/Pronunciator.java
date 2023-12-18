package com.guilsch.vocavox;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Pronunciator {

    public static TextToSpeech initPronunciator(Context context, String languageCode) {
        final TextToSpeech[] textToSpeech = {null};

        textToSpeech[0] = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale locale = new Locale(languageCode);
                int result = textToSpeech[0].setLanguage(locale);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Language not supported of missing data");
                } else {
                    Log.i("TextToSpeech", "Language was set with succes");
                }
            } else {
                Log.e("TextToSpeech", "Can't initialize vocal synthesis");
            }
        });

        return textToSpeech[0];
    }
}