package com.homemade.akhilez.timetable;

import android.provider.BaseColumns;

/**
 * Created by Akhilez on 1/29/2016.
 */
public class TableContract {
    public TableContract(){}

    public static abstract class Subjects implements BaseColumns {
        public static final String TABLE_NAME = "subjects";
        public static final String COLUMN_NAME_SNO = "sno";
        public static final String COLUMN_NAME_SABBR = "sabbr";
        public static final String COLUMN_NAME_SNAME = "sname";
        public static final String COLUMN_NAME_FACULTY = "faculty";

        public static final String[] COLLIST = {
            COLUMN_NAME_SNO,
                COLUMN_NAME_SABBR,
                COLUMN_NAME_SNAME,
                COLUMN_NAME_FACULTY };
    }

    public static abstract class TimeTable implements BaseColumns {
        public static final String TABLE_NAME = "timetable";
        public static final String COLUMN_NAME_PNO = "pno";
        public static final String COLUMN_NAME_STARTTIME = "startTime";
        public static final String COLUMN_NAME_ENDTIME = "endTime";
        public static final String COLUMN_NAME_MON = "mon";
        public static final String COLUMN_NAME_TUE = "tue";
        public static final String COLUMN_NAME_WED = "wed";
        public static final String COLUMN_NAME_THU = "thu";
        public static final String COLUMN_NAME_FRI = "fri";
        public static final String COLUMN_NAME_SAT = "sat";

        public static final String[] COLLIST = {
            COLUMN_NAME_PNO,
            COLUMN_NAME_STARTTIME,
            COLUMN_NAME_ENDTIME,
            COLUMN_NAME_MON,
            COLUMN_NAME_TUE,
            COLUMN_NAME_WED,
            COLUMN_NAME_THU,
            COLUMN_NAME_FRI,
            COLUMN_NAME_SAT
        };

    }
}
