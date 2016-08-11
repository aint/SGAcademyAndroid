package com.github.aint.lesson7.database.entry;

import android.provider.BaseColumns;

public class MessageEntry implements BaseColumns {
    public static final String TABLE_NAME = "message";
    public static final String COLUMN_NAME_IMAGE_ID = "imageId";
    public static final String COLUMN_NAME_FIRST_NAME = "title";
    public static final String COLUMN_NAME_LAST_NAME = "body";
}
