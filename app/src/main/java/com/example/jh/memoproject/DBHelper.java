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


}
