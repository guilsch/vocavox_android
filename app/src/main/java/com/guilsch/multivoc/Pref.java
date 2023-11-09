package com.guilsch.multivoc;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;

/**
 * This class contains all methods used to manage preferences. Preferences are saved to be retrieved
 * after the application or the device is turned down
 *
 * @author Guilhem Schena
 */
public class Pref {

    public static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(Param.APP_PREF, Context.MODE_PRIVATE);
    }

    /**
     * Retrieves all preferences saved in the parameters, uses default parameters if nothing has
     * been previously saved
     */
    public static void retrieveAllPreferences (Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        Param.FIRST_LAUNCH = getPrefs(context).getBoolean(Param.FIRST_LAUNCH_KEY, Boolean.TRUE);
        Param.FOLDER_PATH = getPrefs(context).getString(Param.FOLDER_PATH_KEY, Param.FOLDER_PATH_DEFAULT);
        Param.EN_FILE_ID = getPrefs(context).getString(Param.EN_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.FR_FILE_ID = getPrefs(context).getString(Param.FR_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.GE_FILE_ID = getPrefs(context).getString(Param.GE_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.IT_FILE_ID = getPrefs(context).getString(Param.IT_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.RU_FILE_ID = getPrefs(context).getString(Param.RU_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.SP_FILE_ID = getPrefs(context).getString(Param.SP_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.FOLDER_ID = getPrefs(context).getString(Param.FOLDER_ID_KEY, Param.FOLDER_ID_DEFAULT);
        Param.LANG_DIRECTION_FREQ = getPrefs(context).getInt(Param.LANG_DIRECTION_FREQ_KEY, Param.LANG_DIRECTION_FREQ_DEFAULT);
        Param.LAST_LANG = getPrefs(context).getInt(Param.LAST_LANG_KEY, Param.LAST_LANG_DEFAULT);
        Param.AUTOMATIC_SPEECH = getPrefs(context).getBoolean(Param.AUTOMATIC_SPEECH_KEY, Param.AUTOMATIC_SPEECH_DEFAULT);
        editor.commit();
    }

    /**
     * Saves a preference which can be a String, an Integer or a Boolean. The preference will be
     * retrieve after the application or the device is turned on
     */
    public static <T> void savePreference(Context context, String key, T value){
        SharedPreferences.Editor editor = getPrefs(context).edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }
        else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        else {
            AssertionError error = new AssertionError("Preference to be saved is not a String, an Integer or a Boolean");
            throw error;
        }

        editor.commit();
    }

    public static void insertStringSet(Context context, String key, Set<String> value){
        getPrefs(context).edit().putStringSet(key, value).commit();
    }

    public static String retrieveData(Context context, String key){
        return getPrefs(context).getString(key,"no_data_found");
    }

    public static Set<String> getStringSet(Context context, String key){
        return getPrefs(context).getStringSet(key,null);
    }

    public static void deleteData(Context context, String key){
        SharedPreferences.Editor editor=getPrefs(context).edit();
        editor.remove(key);
        editor.commit();
    }
}