package com.qays3dd.buhk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Sqlit extends SQLiteOpenHelper {
    public static final String BDname = "mdata.db";

    public DB_Sqlit(Context context) {
        super(context, BDname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table favorite ( id INTEGER PRIMARY KEY AUTOINCREMENT, Main_title TEXT,Sub_title TEXT,page_id TEXT)");
        db.execSQL("create table settingtable ( id INTEGER primary key, fontsize TEXT, text_color TEXT)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("fontsize", "110");
        contentValues.put("text_color", "-9109599");

        db.insert("settingtable", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorite");
        db.execSQL("DROP TABLE IF EXISTS settingtable");
        onCreate(db);
    }

    public Boolean Insert_to_favorite(String Main_title, String Sub_title, String page_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Main_title", Main_title);
        contentValues.put("Sub_title", Sub_title);
        contentValues.put("page_id", page_id);
        long result = db.insert("favorite", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }


    public ArrayList getAllList_Favorite() {
        ArrayList<List_itme_Index> arraylist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from favorite", null);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            String id = rs.getString(rs.getColumnIndex("id"));
            String Main_title = rs.getString(rs.getColumnIndex("Main_title"));
            String Sub_title = rs.getString(rs.getColumnIndex("Sub_title"));
            String page_id = rs.getString(rs.getColumnIndex("page_id"));
            arraylist.add(new List_itme_Index(id,page_id, Main_title, Sub_title));
            rs.moveToNext();
        }
        return arraylist;
    }

    public ArrayList get_check_List_Favorite() {
        ArrayList arraylist = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from favorite", null);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
//String Main_title = rs.getString(rs.getColumnIndex("Main_title"));
            String Sub_title = rs.getString(rs.getColumnIndex("Sub_title"));
            arraylist.add(Sub_title);
            rs.moveToNext();
        }
        return arraylist;
    }


    public String get_fontsize() {
        String fontsize;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select fontsize from settingtable ", null);
        res.moveToFirst();
        fontsize = res.getString(res.getColumnIndex("fontsize"));
        return fontsize;
    }

    public String get_text_color() {
        String select_color;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select text_color from settingtable ", null);
        res.moveToFirst();
        select_color = res.getString(res.getColumnIndex("text_color"));
        return select_color;
    }


    public boolean update_setting_fontsize(String fontsize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fontsize", fontsize);
        db.update("settingtable", contentValues, "id= ?", new String[]{"1"});

        return true;
    }

    public boolean update_setting_text_color(String select_color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("text_color", select_color);
        db.update("settingtable", contentValues, "id= ?", new String[]{"1"});

        return true;
    }


    public Integer Delete(String id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName, "id = ?", new String[]{id});
    }

}
