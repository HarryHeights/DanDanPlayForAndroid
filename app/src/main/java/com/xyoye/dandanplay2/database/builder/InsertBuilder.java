package com.xyoye.dandanplay2.database.builder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.xyoye.dandanplay2.database.DataBaseInfo;

/**
 * Created by xyy on 2019/4/17.
 */
public class InsertBuilder{
    private SQLiteDatabase sqLiteDatabase;
        private int tablePosition;
        private ContentValues mValues;

    InsertBuilder(int tablePosition, SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
        this.tablePosition = tablePosition;
        mValues = new ContentValues();
    }

    public InsertBuilder param(int column, String value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Byte value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Short value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Integer value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Long value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Float value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Double value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, Boolean value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public InsertBuilder param(int column, byte[] value) {
        mValues.put(DataBaseInfo.getFieldNames()[tablePosition][column], value);
        return this;
    }

    public void execute(){
        if (mValues != null)
            sqLiteDatabase.insert(DataBaseInfo.getTableNames()[tablePosition], null, mValues);
    }

    public void postExecute(){
        new Thread(this::execute).start();
    }
}