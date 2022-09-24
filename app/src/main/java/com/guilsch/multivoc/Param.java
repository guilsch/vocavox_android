package com.guilsch.multivoc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Param {

    public static final String FOLDER_PATH_DEFAULT = "storage/emulated/0/Multivoc/";
    public static final String FOLDER_ID_DEFAULT = "";
    public static final String FILE_ID_UNDEFINED = "undefined";
    public static String DATA_FILE;
    public static String DATA_PATH;

    public static final String[] TARGET_LANGUAGES = {"English", "German", "French", "Italian", "Russian", "Spanish"};
    public static final String[] USER_LANGUAGES = {"English", "German", "French", "Italian", "Russian", "Spanish"};
    public static String TARGET_LANGUAGE;
    public static String USER_LANGUAGE_DEFAULT = "French";

    public static final String ITEM1_FIELD_NAME = "Item 1";
    public static final String ITEM2_FIELD_NAME = "Item 2";
    public static final String PACK_FIELD_NAME = "Pack";
    public static final String STATE_FIELD_NAME = "State";
    public static final String NEXT_DATE_FIELD_NAME = "Next Date";
    public static final String REPETITIONS_FIELD_NAME = "Repetitions";
    public static final String EF_FIELD_NAME = "Easiness Factor";
    public static final String INTERVAL_FIELD_NAME = "Interval";
    public static final String UUID_FIELD_NAME = "UUID";

    public static final List<String> FIELDS = Arrays.asList(ITEM1_FIELD_NAME, ITEM2_FIELD_NAME, PACK_FIELD_NAME,
            STATE_FIELD_NAME, NEXT_DATE_FIELD_NAME, REPETITIONS_FIELD_NAME, EF_FIELD_NAME,
            INTERVAL_FIELD_NAME, UUID_FIELD_NAME);
    public static final int FIELDS_NB = 9;


    public static final int INACTIVE = 0;
    public static final int ACTIVE = 1;
    public static final int TO_LEARN = 2;
    public static final int INVALID = 3;
    public static final int STOP_LEARNING = 4;

    public static final String DEFAULT_PACK = "";
    public static final Date DEFAULT_DATE = utils.giveCurrentDate();
    public static final int DEFAULT_REP = 0;
    public static final int DEFAULT_EF = 0;
    public static final int DEFAULT_INTER = 0;

//    public static String FOLDER_ID = "1JEIT59Bq_2zxyhDgZRuBtPtXyTufeG4-";
//    public static String FILE_ID = "12JOEr-6m5tsoLvg3RF14vLMvJhPPdohQ";
    public static String FILE_ID;

    public static String DATA_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static String APP_PREF = "app_pref";

    public static String FOLDER_PATH = "storage/emulated/0/Multivoc/";
    public static final String FOLDER_PATH_KEY = "folder_path_key";
    public static String USER_LANGUAGE = "French";
    public static final String USER_LANGUAGE_KEY = "user_language_key";
    public static String EN_FILE_ID = "Undefined";
    public static final String EN_FILE_ID_KEY = "en_file_id_key";
    public static String FR_FILE_ID = "Undefined";
    public static final String FR_FILE_ID_KEY = "fr_file_id_key";
    public static String GE_FILE_ID = "Undefined";
    public static final String GE_FILE_ID_KEY = "ge_file_id_key";
    public static String IT_FILE_ID = "Undefined";
    public static final String IT_FILE_ID_KEY = "it_file_id_key";
    public static String RU_FILE_ID = "Undefined";
    public static final String RU_FILE_ID_KEY = "ru_file_id_key";
    public static String SP_FILE_ID = "Undefined";
    public static final String SP_FILE_ID_KEY = "sp_file_id_key";
    public static String FOLDER_ID = "Undefined";
    public static final String FOLDER_ID_KEY = "folder_id_key";


    public static void setDataPath() {
        DATA_PATH = FOLDER_PATH + DATA_FILE;
    }
}
