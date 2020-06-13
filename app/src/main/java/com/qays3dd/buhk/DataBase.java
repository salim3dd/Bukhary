package com.qays3dd.buhk;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    public static final String BDname = "buhk.db";

    public DataBase(Context context) {
        super(context, BDname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Titles ( id INTEGER PRIMARY KEY AUTOINCREMENT, Main_title TEXT,Sub_title TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Titles");
        onCreate(db);
    }

    public Boolean Insert_Titles(String Main_title, String Sub_title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Main_title", Main_title);
        contentValues.put("Sub_title", Sub_title);

        long result = db.insert("Titles", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }



}
