package com.qays3dd.buhk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "buhk.db";
    public static final String DBLOCATION = Environment.getDataDirectory() + "/data/com.qays3dd.buhk/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public ArrayList get_All_Main_Titles() {
        ArrayList<List_itme_Index> arrayList = new ArrayList<>();
        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from Main_Titles", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex("id"));
            String main_title = res.getString(res.getColumnIndex("M_Titles"));
            arrayList.add(new List_itme_Index("",id, main_title, ""));
            res.moveToNext();
        }
        res.close();
        closeDatabase();
        return arrayList;
    }

    public ArrayList get_All_Sub_Titles(String main_titles) {
        ArrayList<List_itme_Index> arrayList = new ArrayList<>();
        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from Titles where Main_title like '" + main_titles + "'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex("id"));
            String main_title = res.getString(res.getColumnIndex("Main_title"));
            String sub_title = res.getString(res.getColumnIndex("Sub_title"));
            arrayList.add(new List_itme_Index("",id, main_title, sub_title));
            res.moveToNext();
        }
        res.close();
        closeDatabase();
        return arrayList;
    }



    public ArrayList get_Search(String sub_titles) {
        ArrayList<List_itme_Index> arrayList = new ArrayList<>();
        openDatabase();
        Cursor res = mDatabase.rawQuery("select * from Titles where Sub_title like '%" + sub_titles + "%'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            String id = res.getString(res.getColumnIndex("id"));
            String main_title = res.getString(res.getColumnIndex("Main_title"));
            String sub_title = res.getString(res.getColumnIndex("Sub_title"));
            arrayList.add(new List_itme_Index("",id, main_title, sub_title));
            res.moveToNext();
        }
        res.close();
        closeDatabase();
        return arrayList;
    }


}
