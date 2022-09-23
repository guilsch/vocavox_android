package com.guilsch.multivoc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class Pref {

    SharedPreferences prefs;
    Context context;

    public static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(Param.APP_PREF, Context.MODE_PRIVATE);
    }

    public static void saveAllPreferences (Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(Param.FOLDER_PATH_KEY, Param.FOLDER_PATH);
        editor.putString(Param.USER_LANGUAGE_KEY, Param.USER_LANGUAGE);
        editor.putString(Param.EN_FILE_ID_KEY, Param.EN_FILE_ID);
        editor.putString(Param.FR_FILE_ID_KEY, Param.FR_FILE_ID);
        editor.putString(Param.GE_FILE_ID_KEY, Param.GE_FILE_ID);
        editor.putString(Param.RU_FILE_ID_KEY, Param.RU_FILE_ID);
        editor.putString(Param.IT_FILE_ID_KEY, Param.IT_FILE_ID);
        editor.putString(Param.SP_FILE_ID_KEY, Param.SP_FILE_ID);
        editor.putString(Param.FOLDER_ID_KEY, Param.FOLDER_ID);
        editor.commit();
    }

    public static void retrieveAllPreferences (Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        Param.USER_LANGUAGE = getPrefs(context).getString(Param.USER_LANGUAGE_KEY, Param.USER_LANGUAGE_DEFAULT);
        Param.FOLDER_PATH = getPrefs(context).getString(Param.FOLDER_PATH_KEY, Param.FOLDER_PATH_DEFAULT);
        Param.EN_FILE_ID = getPrefs(context).getString(Param.EN_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.FR_FILE_ID = getPrefs(context).getString(Param.FR_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.GE_FILE_ID = getPrefs(context).getString(Param.GE_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.IT_FILE_ID = getPrefs(context).getString(Param.IT_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.RU_FILE_ID = getPrefs(context).getString(Param.RU_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.SP_FILE_ID = getPrefs(context).getString(Param.SP_FILE_ID_KEY, Param.FILE_ID_UNDEFINED);
        Param.FOLDER_ID = getPrefs(context).getString(Param.FOLDER_ID_KEY, Param.FOLDER_ID_DEFAULT);
        editor.commit();
    }

    public static void savePreference(Context context, String key, String value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
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