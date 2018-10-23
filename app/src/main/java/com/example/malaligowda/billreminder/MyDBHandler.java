package com.example.malaligowda.billreminder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
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
    public static final String COLUMN_notifyDays = "days";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BILLS + " (" +
                COLUMN_ID + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_date + " TEXT, " +
                COLUMN_amt + " INTEGER, " +
                COLUMN_currency + " TEXT, " +
                COLUMN_interval + " TEXT, " +
                COLUMN_type + " TEXT, " +
                COLUMN_notify + " TEXT, " +
                COLUMN_notifyDays + " TEXT " +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    //add new bill to the database
    public boolean addBill(Bills bills) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("rohit", "type-"+bills.get_id());
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, bills.get_name());
        values.put(COLUMN_amt, bills.getAmt());
        values.put(COLUMN_type, bills.get_type());
        values.put(COLUMN_currency, bills.get_currency());
        values.put(COLUMN_interval, bills.get_interval());
        values.put(COLUMN_notify, bills.get_notify());
        values.put(COLUMN_date, bills.get_day());
        values.put(COLUMN_notifyDays, bills.get_notifyDays());
        values.put(COLUMN_ID,bills.get_id());

        db.insert(TABLE_BILLS, null, values);

        long result;
        result = db.insert(TABLE_BILLS, null, values);
        db.close();
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteBill(int billID) {
        Log.d("delete",""+ billID);
        SQLiteDatabase db = getWritableDatabase();
      //  db.execSQL("DELETE FROM " + TABLE_BILLS + " WHERE " + COLUMN_ID + "=\'" + billID + "\';");
        db.delete(TABLE_BILLS, COLUMN_ID + "=" + billID, null);


    }

    public List<Integer> idArray() {
        List<Integer> id = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT _id FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);


        if (c.moveToFirst()) {
            do {
                id.add(c.getColumnIndex(COLUMN_ID));


                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (id.size() > 1) {
                id.remove(0);
            }
            if (id.size() > 4) {
                id.remove(4);
            }
            if (id.size() > 7) {
                id.remove(7);
            }
        }

        if (id != null) {
            Log.d("billReminder", id.toString());
        }
        c.close();
        db.close();
        return id;
    }

    public ArrayList<String> namesArray() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                names.add(c.getString(c.getColumnIndex(COLUMN_NAME)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (names.size() > 1) {
                names.remove(0);
            }
            if (names.size() > 4) {
                names.remove(4);
            }
            if (names.size() > 7) {
                names.remove(7);
            }
        }

        if (names != null) {
            Log.d("billReminder", names.toString());
        }
        c.close();
        db.close();
        return names;

    }

    public ArrayList<String> amountArray() {
        ArrayList<String> amount = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT amt FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {

                amount.add(c.getString(c.getColumnIndex(COLUMN_amt)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (amount.size() > 1) {
                amount.remove(0);
            }
            if (amount.size() > 4) {
                amount.remove(4);
            }
            if (amount.size() > 7) {
                amount.remove(7);
            }
        }

        if (amount != null) {
            Log.d("billReminder", amount.toString());
        }
        c.close();

        db.close();
//        Log.d("test", "amountArray:"+amount.get(1));
        return amount;

    }

    public ArrayList<String> currencyArray() {
        ArrayList<String> currency = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT currency FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);


        if (c.moveToFirst()) {
            do {

                currency.add(c.getString(c.getColumnIndex(COLUMN_currency)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (currency.size() > 1) {
                currency.remove(0);
            }
            if (currency.size() > 4) {
                currency.remove(4);
            }
            if (currency.size() > 7) {
                currency.remove(7);
            }
        }

        if (currency != null) {
            Log.d("billReminder", currency.toString());
        }
        c.close();
        db.close();
        return currency;

    }

    public ArrayList<String> dateArray() {
        ArrayList<String> date = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT date FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst()) {
            do {
                date.add(c.getString(c.getColumnIndex(COLUMN_date)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (date.size() > 1) {
                date.remove(0);
            }
            if (date.size() > 4) {
                date.remove(4);
            }
            if (date.size() > 7) {
                date.remove(7);
            }
        }

        if (date != null) {
            Log.d("billReminder", date.toString());
        }
        c.close();

        db.close();
        return date;

    }

    public ArrayList<String> typeArray() {
        ArrayList<String> type = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT type FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst()) {
            do {
                type.add(c.getString(c.getColumnIndex(COLUMN_type)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (type.size() > 1) {
                type.remove(0);
            }
            if (type.size() > 4) {
                type.remove(4);
            }
            if (type.size() > 7) {
                type.remove(7);
            }
        }

        if (type != null) {
            Log.d("billReminder", type.toString());
        }
        c.close();

        db.close();
        return type;

    }

    public ArrayList<String> intervalArray() {
        ArrayList<String> interval = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT interval FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst()) {
            do {
                interval.add(c.getString(c.getColumnIndex(COLUMN_interval)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (interval.size() > 1) {
                interval.remove(0);
            }
            if (interval.size() > 4) {
                interval.remove(4);
            }
            if (interval.size() > 7) {
                interval.remove(7);
            }
        }

        if (interval != null) {
            Log.d("billReminder", interval.toString());
        }
        c.close();

        db.close();
        return interval;

    }

    public ArrayList<String> notifyArray() {
        ArrayList<String> notify = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT notify FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst()) {
            do {
                notify.add(c.getString(c.getColumnIndex(COLUMN_notify)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (notify.size() > 1) {
                notify.remove(0);
            }
            if (notify.size() > 4) {
                notify.remove(4);
            }
            if (notify.size() > 7) {
                notify.remove(7);
            }
        }

        if (notify != null) {
            Log.d("billReminder", notify.toString());
        }
        c.close();

        db.close();
        return notify;
    }

    public ArrayList<String> notifyDays() {
        ArrayList<String> notifyDays = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT days FROM " + TABLE_BILLS + " WHERE1";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                notifyDays.add(c.getString(c.getColumnIndex(COLUMN_notifyDays)));

                c.moveToNext();
                c.moveToNext();

            } while (c.moveToNext() && !c.isLast());
            if (notifyDays.size() > 1) {
                notifyDays.remove(0);
            }
            if (notifyDays.size() > 4) {
                notifyDays.remove(4);
            }
            if (notifyDays.size() > 7) {
                notifyDays.remove(7);
            }
        }
        return notifyDays;


    }
}
