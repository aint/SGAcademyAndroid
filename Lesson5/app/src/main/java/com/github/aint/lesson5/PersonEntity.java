package com.github.aint.lesson5;

import android.provider.BaseColumns;

public abstract class PersonEntity implements BaseColumns {
    public static final String TABLE_NAME = "person";
    public static final String COLUMN_NAME_IMAGE_ID = "imageId";
    public static final String COLUMN_NAME_FIRST_NAME = "firstName";
    public static final String COLUMN_NAME_LAST_NAME = "lastName";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_SEX = "sex";
    public static final String COLUMN_NAME_SALARY = "salary";
    public static final String COLUMN_NAME_LOCATION = "location";
    public static final String COLUMN_NAME_OCCUPATION = "occupation";



}
