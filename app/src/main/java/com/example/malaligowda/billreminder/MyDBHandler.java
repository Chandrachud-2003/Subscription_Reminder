package com.example.malaligowda.billreminder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
public class MyDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bills.db";
    public static final String TABLE_BILLS = "bills";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_day = "day";
    public static final String COLUMN_amt = "amt";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BILLS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_day + "INTEGER, "+ COLUMN_amt + "INTEGER"+");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }
   //add new bill to the database
   public void addBill(Bills bills){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,bills.get_name());
        values.put(COLUMN_day,bills.getDay());
       values.put(COLUMN_amt,bills.getAmt());
       SQLiteDatabase db = getWritableDatabase();
       db.insert(TABLE_BILLS,null,values);
       db.close();
   }

    public void DeleteBill(String billname){


        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BILLS + " WHERE "+ COLUMN_NAME + "=\"" + billname + "\";");
    }

}
