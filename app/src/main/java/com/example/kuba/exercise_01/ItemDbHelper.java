package com.example.kuba.exercise_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class ItemDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Exercise_01.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " (" +
                    ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ItemContract.ItemEntry.NAME + " TEXT," +
                    ItemContract.ItemEntry.PRICE + " INTEGER," +
                    ItemContract.ItemEntry.QUANTITY + " INTEGER," +
                    ItemContract.ItemEntry.IS_BOUGHT + " BOOLEAN)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME;

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database Operations", "Database created...");
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("Database Operations", "Table created...");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addItem(String name, int price, int quantity, boolean isBought, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.NAME, name);
        contentValues.put(ItemContract.ItemEntry.PRICE, price);
        contentValues.put(ItemContract.ItemEntry.QUANTITY, quantity);
        contentValues.put(ItemContract.ItemEntry.IS_BOUGHT, isBought);
        db.insert(ItemContract.ItemEntry.TABLE_NAME, null, contentValues);
        Log.d("Database Operations", "One row inserted...");
    }

    public long addItem(ContentValues contentValues, SQLiteDatabase db) {
        return db.insert(ItemContract.ItemEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor readItems(SQLiteDatabase db) {
        String[] projection = {
                BaseColumns._ID,
                ItemContract.ItemEntry.NAME,
                ItemContract.ItemEntry.PRICE,
                ItemContract.ItemEntry.QUANTITY,
                ItemContract.ItemEntry.IS_BOUGHT
        };

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
                );

        return cursor;
    }

    public Cursor readItem(int id, SQLiteDatabase db) {
        String[] projection = {
                ItemContract.ItemEntry.NAME,
                ItemContract.ItemEntry.PRICE,
                ItemContract.ItemEntry.QUANTITY,
                ItemContract.ItemEntry.IS_BOUGHT
        };

        String selection = ItemContract.ItemEntry._ID + " = " + id;

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public void updateItem(int id, String name, int price, int quantity, boolean isBought, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.NAME, name);
        contentValues.put(ItemContract.ItemEntry.PRICE, price);
        contentValues.put(ItemContract.ItemEntry.QUANTITY, quantity);
        contentValues.put(ItemContract.ItemEntry.IS_BOUGHT, isBought);

        String selection = ItemContract.ItemEntry._ID + " = " + id;

        db.update(ItemContract.ItemEntry.TABLE_NAME, contentValues, selection, null);
        Log.d("Database Operations", "One row updated...");
    }

    public int updateItem(String selection, ContentValues contentValues, SQLiteDatabase db) {
        return db.update(ItemContract.ItemEntry.TABLE_NAME, contentValues, selection, null);
    }

    public void deleteItem(int id, SQLiteDatabase db) {
        String selection = ItemContract.ItemEntry._ID + " = " + id;

        db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, null);
        Log.d("Database Operations", "One row deleted...");
    }

    public int deleteItem(String selection, SQLiteDatabase db) {
        return db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, null);
    }
}