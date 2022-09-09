package com.guilsch.multivoc;

import android.text.Editable;

public class Constants {

    private static String DATA_PATH = "storage/emulated/0/Multivoc/fr_it.xlsx";
    private static final String DATA_PATH_DEFAULT = "storage/emulated/0/Multivoc/fr_it.xlsx";

    public static String getDataPath() {
        return DATA_PATH;
    }

    public static String getDataPathDefault() {
        return DATA_PATH_DEFAULT;
    }

    public static void setDataPath(String dataPath) {
        DATA_PATH = dataPath;
    }
}
