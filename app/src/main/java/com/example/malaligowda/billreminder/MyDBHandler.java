package com.example.malaligowda.billreminder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bills.db";
    public static final String TABLE_BILLS = "Table2";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_date = "date";
    public static final String COLUMN_amt = "amt";
    public static final String COLUMN_currency = "currency";
    public static final String COLUMN_interval = "interval";
    public static final String COLUMN_type = "type";
    public static final String COLUMN_notify = "notify";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BILLS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_date+ " TEXT, "+
                COLUMN_amt + " INTEGER, "+
                COLUMN_currency + " TEXT, "+
                COLUMN_interval+" TEXT, "+
                COLUMN_type+" TEXT, "+
                COLUMN_notify+" TEXT" +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }
   //add new bill to the database
   public boolean addBill(Bills bills){
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,bills.get_name());
       values.put(COLUMN_amt,bills.getAmt());
       values.put(COLUMN_type, bills.get_type());
       values.put(COLUMN_currency, bills.get_currency());
       values.put(COLUMN_interval, bills.get_interval());
       values.put(COLUMN_notify, bills.get_notify());
       values.put(COLUMN_date,bills.get_date());

       db.insert(TABLE_BILLS,null,values);

       long result;
       result = db.insert(TABLE_BILLS, null, values);
       db.close();
       if (result!=-1)
       {
           return true;
       }
       else {
           return false;
       }
   }

    public void deleteBill(String billname){


        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BILLS + " WHERE "+ COLUMN_NAME + "=\"" + billname + "\";");

    }

    public ArrayList<String> namesArray()
    {
        ArrayList<String> names=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
        {
            do {
                names.add(c.getString(c.getColumnIndex(COLUMN_NAME)));

            }while (c.moveToNext());

        }

        if (names != null) {
            Log.d("billReminder", names.toString());
        }
        c.close();
        db.close();
        return names;

    }

    public ArrayList<String> amountArray()
    {
        ArrayList<String> amount=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
        {
            do {
                amount.add(c.getString(c.getColumnIndex(COLUMN_amt)));

            }while (c.moveToNext());

        }

        if (amount != null) {
            Log.d("billReminder", amount.toString());
        }        c.close();

        db.close();

        return amount;

    }

    public ArrayList<String> currencyArray()
    {
        ArrayList<String> currency=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                currency.add(c.getString(c.getColumnIndex(COLUMN_currency)));

            }while (c.moveToNext());

        }

        if (currency != null) {
            Log.d("billReminder", currency.toString());
        }        c.close();
        db.close();
        return currency;

    }

    public ArrayList<String> dateArray()
    {
        ArrayList<String> date=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                date.add(c.getString(c.getColumnIndex(COLUMN_date)));

            }while (c.moveToNext());

        }

        if (date != null) {
            Log.d("billReminder", date.toString());
        }        c.close();

        db.close();
        return date;

    }

    public ArrayList<String> typeArray()
    {
        ArrayList<String> type=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                type.add(c.getString(c.getColumnIndex(COLUMN_type)));

            }while (c.moveToNext());

        }

        if (type != null) {
            Log.d("billReminder", type.toString());
        }        c.close();

        db.close();
        return type;

    }

    public ArrayList<String> intervalArray()
    {
        ArrayList<String> interval=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                interval.add(c.getString(c.getColumnIndex(COLUMN_interval)));

            }while (c.moveToNext());

        }

        if (interval != null) {
            Log.d("billReminder", interval.toString());
        }        c.close();

        db.close();
        return interval;

    }

    public ArrayList<String> notifyArray()
    {
        ArrayList<String> notify=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BILLS+" WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                notify.add(c.getString(c.getColumnIndex(COLUMN_notify)));

            }while (c.moveToNext());

        }

        if (notify != null) {
            Log.d("billReminder", notify.toString());
        }        c.close();

        db.close();
        return notify;

    }

}
