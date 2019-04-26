package com.example.jh.memoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.jh.memoproject.List_Grid_view.MemoGridViewItem;

import java.sql.Date;
import java.sql.Time;
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


    //다이어리(년, 월, 일, 요일, 시, 분, 오후(오전), )
    //메모(년월일, 시분 오후(오전), title, content))
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQLite Database로 쿼리 실행
        db.execSQL("CREATE TABLE diary (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "AGE INTEGER," +
                "PHONE TEXT)");
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
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("MEMO", memo);

        long id = db.insert(MEMO_TABLE, null, contentValues);
        Log.d("DATABASE","Memo Database Insert Id:"+id);

        db.close();
    }

    public void memo_update(String title, String memo, int position) {
        SQLiteDatabase db = getWritableDatabase();

        //db.execSQL("UPDATE " + MEMO_TABLE + " SET TITLE=" + title + ", MEMO=" + memo + ", DATE=datetime('now', 'localtime') " + " WHERE _ID=" + position + ";");

        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("MEMO", memo);
        values.put("DATE", getDateTime());
        db.update("MEMO", values, "_ID=" + position, null);
        db.close();
    }

    public void delete(String tablename, int position) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        if(tablename.equals(MEMO_TABLE))
            db.execSQL("DELETE FROM " + MEMO_TABLE + " WHERE _ID=" + position + ";");
        else if (tablename.equals(DIARY_TABLE))
            db.execSQL("DELETE FROM " + DIARY_TABLE + " WHERE _ID=" + position + ";");
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
