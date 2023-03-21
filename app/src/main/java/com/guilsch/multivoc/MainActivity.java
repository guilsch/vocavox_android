package com.guilsch.multivoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;

import life.sabujak.roundedbutton.RoundedButton;

public class MainActivity extends AppCompatActivity {

    private RoundedButton start;
    private Spinner spinner;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Ajouter la transition de zoom
//        Transition zoom = TransitionInflater.from(this).inflateTransition(R.transition.zoom);
//        getWindow().setEnterTransition(zoom);

        ////// Retrieve preferences variables
        Pref.retrieveAllPreferences(this);

//        if (Param.FIRST_LAUNCH) {
//            Intent intent = new Intent(this, FirstLaunchActivity.class);
//            startActivity(intent);
//        }

        ////// Setup visuals
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        spinner = findViewById(R.id.spinner);

        ////// Manage events

        // Spinner
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Param.TARGET_LANGUAGES);
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(Param.LAST_LANG);

        // Buttons
        start.setOnClickListener(v -> onStartClick());

        /////// Permissions
        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage);
        }

        String[] permissionsWriteStorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int requestWriteExternalStorage = 1;
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsWriteStorage, requestWriteExternalStorage);
        }
    }

    /**
     * Occurs when the start button has been pressed. Prepares the launch of menuActivity.
     */
    private void onStartClick() {

        // Change layout to loading layout
        transitionToLoadingScreen();

        // Get progressBar of the current layout (loading layout)
        progressBar = findViewById(R.id.progressBar);

        // Manage newly selected language
        Language selectedLanguage = (Language) spinner.getSelectedItem();
        Param.TARGET_LANGUAGE = selectedLanguage.getName();
        Pref.savePreference(this, Param.LAST_LANG_KEY, spinner.getSelectedItemPosition());

        // Start the task to prepare data and show loading
        new LoadDataTask().execute();
    }

    private void transitionToLoadingScreen() {
        ///// Make transition
        ViewGroup rootView = findViewById(android.R.id.content);
        Scene activityMainLoadingScene = Scene.getSceneForLayout(rootView, R.layout.activity_main_loading_layout, this);
        Transition transition = new Fade();
        transition.setDuration(500);

        // Start transition
        TransitionManager.go(activityMainLoadingScene, transition);
        //////
    }

    /**
     * We use a task since the initAppData() method blocks the ui thread which makes the progressBar
     * not showing instantly
     */
    private class LoadDataTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            // This method is called on the UI thread before the background task starts
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // This method is called on a background thread, so it won't block the UI thread
            initAppData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // This method is called on the UI thread when you call publishProgress()
            // TODO : add real progress to the bar
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // This method is called on the UI thread after the background task completes
            changeActivity();
        }
    }

    /**
     * Setup after target language has been chosen by user.
     * Init non-preferences parameters, global deck and flags for visual
     */
    public void initAppData() {
        // Init other static variables
        utils.initParam();

        // Clean excel data file
        utils.prepareDataFile();

        // Init global deck
        utils.initGlobalDeck();

        // Set user and target language flag
        setUserLanguageVisuals();
        setTargetLanguageVisuals();
    }

    /**
     * Depending on the user language setup in the parameters, defines the corresponding flag in
     * the parameter FLAG_ICON_USER used in the visuals
     */
    public void setUserLanguageVisuals() {
        switch (Param.USER_LANGUAGE) {
            case "English" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_gb);
                break;

            case "French" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_fr);
                break;

            case "German" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_de);
                break;

            case "Italian" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_it);
                break;

            case "Russian" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_ru);
                break;

            case "Spanish" :
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_es);
                break;

            default:
                Param.FLAG_ICON_USER = getResources().getDrawable(R.drawable.ic_lang_default);
                break;
        }
    }

    /**
     * Depending on the target language setup in the parameters, defines the corresponding flag in
     * the parameter FLAG_ICON_TARGET used in the visuals
     */
    public void setTargetLanguageVisuals() {
        switch (Param.TARGET_LANGUAGE) {
            case "English" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_gb);
                break;

            case "French" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_fr);
                break;

            case "German" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_de);
                break;

            case "Italian" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_it);
                break;

            case "Russian" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_ru);
                break;

            case "Spanish" :
                Param.FLAG_ICON_TARGET = getResources().getDrawable(R.drawable.ic_es);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + Param.TARGET_LANGUAGE);
        }
    }

    /**
     * Method to switch to MenuActivity
     */
    private void changeActivity() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}