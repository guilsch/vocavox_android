package com.guilsch.multivoc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static androidx.core.content.ContextCompat.createDeviceProtectedStorageContext;

public class TranslationService {

    Translate translate;
    Context currentContext;

    public TranslationService(Context context) {
        this.currentContext = context;
        createDeviceProtectedStorageContext(context);
    }

    public void getTranslateService(Activity currentActivity) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        InputStream is2 = currentActivity.getResources().openRawResource(R.raw.credentials);

        try (InputStream is = currentActivity.getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
//            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(credential).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public String translate(String originalText) {
        //Get input text to be translated:
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("tr"), Translate.TranslateOption.model("base"));
        return translation.getTranslatedText();
    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) this.currentContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        Boolean connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

}
