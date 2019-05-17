package com.example.jh.memoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "diamemo.db";
    static final String MEMO_TABLE = "MEMO";
    static final String DIARY_TABLE = "DIARY";

    //text style table in memo
    static final String FONT_TABLE = "FONT";
    static final String SIZE_TABLE = "FONT_SIZE";
    static final String ROW_TABLE = "FONT_ROW";

    static final int DATABASE_VERSION = 1;


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQLite Database로 쿼리 실행
        db.execSQL("CREATE TABLE "+DIARY_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MEMO TEXT," + //메모
                "YEAR TEXT," + //년도
                "DAY TEXT," + //월 일
                "WEEK TEXT," + //요일
                "TIME TEXT," + //시간
                "HOLIDAY INTEGER)"); //주말여부

        db.execSQL("CREATE TABLE "+ MEMO_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT," +
                "MEMO TEXT," +
                "DATE DATETIME not null  DEFAULT (datetime('now','localtime')))");

        db.execSQL("CREATE TABLE "+ FONT_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SEQ INTEGER," +
                " FONT TEXT," +
                " START_INDEX INTEGER," +
                " END_INDEX INTEGER)");

        db.execSQL("CREATE TABLE "+ SIZE_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SEQ INTEGER," +
                " SIZE INTEGER," +
                " START_INDEX INTEGER," +
                " END_INDEX INTEGER)");

        db.execSQL("CREATE TABLE "+ ROW_TABLE + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SEQ INTEGER," +
                " SIZE INTEGER," +
                " START_INDEX INTEGER," +
                " END_INDEX INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void memo_insert(String title, String memo) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("MEMO", memo);

        long id = db.insert(MEMO_TABLE, null, values);
        Log.d("DATABASE","Memo Database Insert Id:"+id);

        db.close();
    }

    public void memo_update(String title, String memo, int seq) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("MEMO", memo);
        values.put("DATE", getDateTime());
        db.update(MEMO_TABLE, values, "_ID=" + seq, null);
        db.close();
    }

    public void delete(String tablename, int seq) {
        SQLiteDatabase db = getWritableDatabase();

        if(tablename.equals(MEMO_TABLE)){
            db.delete(MEMO_TABLE,  "_ID" + "=" + seq, null);
            Log.d("DATABSE", "deleteTbDayStory() id : " + seq);
        }
        else if (tablename.equals(DIARY_TABLE)){
            db.delete(DIARY_TABLE,  "_ID" + "=" + seq, null);
        }
        else if (tablename.equals(FONT_TABLE)){
            db.delete(FONT_TABLE,  "SEQ" + "=" + seq, null);
        }
        else if (tablename.equals(SIZE_TABLE)){
            db.delete(SIZE_TABLE,  "SEQ" + "=" + seq, null);
        }
        else if (tablename.equals(ROW_TABLE)){
            db.delete(ROW_TABLE,  "SEQ" + "=" + seq, null);
        }
        db.close();
    }

    public void daily_insert(String memo, String year, String day, String week, String time, int holiday) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("MEMO", memo);
        values.put("YEAR", year);
        values.put("DAY", day);
        values.put("WEEK", week);
        values.put("TIME", time);
        values.put("HOLIDAY", holiday);

        long id = db.insert(DIARY_TABLE, null, values);
        Log.d("DATABASE","Diary Database Insert Id:"+id);

        db.close();
    }

    public void daily_update(String memo, String year, String day, String week, String time, int seq, int holiday) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("MEMO", memo);
        values.put("YEAR", year);
        values.put("DAY", day);
        values.put("WEEK", week);
        values.put("TIME", time);
        values.put("HOLIDAY", holiday);

        db.update(DIARY_TABLE, values, "_ID=" + seq, null);
        db.close();
        Log.d("DATABASE","Diary Database Update");

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    public void font_insert(int id, String font, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("FONT ", font);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        long reuslt = db.insert(FONT_TABLE, null, values);
        Log.d("DATABASE","FONT_TABLE Database Insert: "+reuslt);

        db.close();
    }

    public void font_update(int id, String font, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("FONT ", font);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        db.update(FONT_TABLE , values, "SEQ=" + id, null);
        Log.d("DATABASE","FONT_TABLE  Database Update");

        db.close();
    }

    public void font_size_insert(int id, int size, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("SIZE  ", size);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        long reuslt = db.insert(SIZE_TABLE , null, values);
        Log.d("DATABASE","SIZE_TABLE  Database Insert: "+reuslt);

        db.close();
    }

    public void font_size_update(int id, int size, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("SIZE  ", size);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        db.update(SIZE_TABLE  , values, "SEQ=" + id, null);
        Log.d("DATABASE","SIZE_TABLE   Database Update");

        db.close();
    }

    public void font_row_insert(int id, int size, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("SIZE  ", size);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        long reuslt = db.insert(ROW_TABLE  , null, values);
        Log.d("DATABASE","ROW_TABLE   Database Insert: "+reuslt);

        db.close();
    }


    public void font_row_update(int id, int size, int start, int end) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        ContentValues values = new ContentValues();
        values.put("SEQ", id);
        values.put("SIZE  ", size);
        values.put("START_INDEX ", start);
        values.put("END_INDEX ", end);

        db.update(ROW_TABLE , values, "SEQ=" + id, null);
        Log.d("DATABASE","ROW_TABLE    Database Update");

        db.close();
    }


}
