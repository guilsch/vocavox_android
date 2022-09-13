package com.guilsch.multivoc;

import java.util.Date;

public class Param {

    public static String FOLDER_PATH = "storage/emulated/0/Multivoc/";
    public static final String FOLDER_PATH_DEFAULT = "storage/emulated/0/Multivoc/";
    public static String DATA_FILE;
    public static String DATA_PATH;

    public static final String[] TARGET_LANGUAGES = {"English", "German", "French", "Italian", "Russian", "Spanish"};
    public static final String[] USER_LANGUAGES = {"English", "German", "French", "Italian", "Russian", "Spanish"};
    public static String TARGET_LANGUAGE;
    public static String USER_LANGUAGE = "French";

    public static final String ITEM1_FIELD_NAME = "Item 1";
    public static final String ITEM2_FIELD_NAME = "Item 2";
    public static final String PACK_FIELD_NAME = "Pack";
    public static final String STATE_FIELD_NAME = "State";
    public static final String NEXT_DATE_FIELD_NAME = "Next Date";
    public static final String REPETITIONS_FIELD_NAME = "Repetitions";
    public static final String EF_FIELD_NAME = "Easiness Factor";
    public static final String INTERVAL_FIELD_NAME = "Interval";

    public static final String[] FIELDS = {ITEM1_FIELD_NAME, ITEM2_FIELD_NAME, PACK_FIELD_NAME,
            STATE_FIELD_NAME, NEXT_DATE_FIELD_NAME, REPETITIONS_FIELD_NAME, EF_FIELD_NAME,
            INTERVAL_FIELD_NAME};

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


    public static void setDataPath() {
        DATA_PATH = FOLDER_PATH + DATA_FILE;
    }
}
