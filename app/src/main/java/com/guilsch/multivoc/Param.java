package com.guilsch.multivoc;

import java.util.Date;

public class Param {

    public static String FOLDER_PATH = "storage/emulated/0/Multivoc/";
    private static final String FOLDER_PATH_DEFAULT = "storage/emulated/0/Multivoc/";

    public static String DATA_PATH;

    public static String FR_EN_DATA_FILE = "fr_en.xlsx";
    public static String FR_GE_DATA_FILE = "fr_ge.xlsx";
    public static String FR_IT_DATA_FILE = "fr_it.xlsx";
    public static String FR_RU_DATA_FILE = "fr_ru.xlsx";

    public static String[] Languages = {"English", "German", "Italian", "Russian"};

    private static String language;

    public static final int INACTIVE = 0;
    public static final int ACTIVE = 1;
    public static final int TO_LEARN = 2;
    public static final int INVALID = 3;
    public static final int STOP_LEARNING = 4;

    public static final String DEFAULT_PACK = "";
    public static final Date DEFAULT_DATE = utils.giveDate();
    public static final int DEFAULT_REP = 0;
    public static final int DEFAULT_EF = 0;
    public static final int DEFAULT_INTER = 0;



    public static String getDataPath() {
        return DATA_PATH;
    }

    public static String getFolderPathDefault() {
        return FOLDER_PATH_DEFAULT;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setFolderPath(String dataPath) {
        FOLDER_PATH = dataPath;
    }

    public static void setLanguage(String language) {
        Param.language = language;
    }

    public static void setDataPath(){
        switch (language) {
            case "English" :
                DATA_PATH = FOLDER_PATH + FR_EN_DATA_FILE;
                break;

            case "German" :
                DATA_PATH = FOLDER_PATH + FR_GE_DATA_FILE;
                break;

            case "Italian" :
                DATA_PATH = FOLDER_PATH + FR_IT_DATA_FILE;
                break;

            case "Russian" :
                DATA_PATH = FOLDER_PATH + FR_RU_DATA_FILE;
                break;
        }
    }
}
