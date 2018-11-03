package com.example.kuba.exercise_01;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ItemProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.kuba.exercise_01.ItemProvider";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    private static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, ItemContract.ItemEntry.TABLE_NAME);
    private static final int ITEMS = 1;
    private static final int ITEM = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ItemContract.ItemEntry.TABLE_NAME, ITEMS);
        uriMatcher.addURI(AUTHORITY,  ItemContract.ItemEntry.TABLE_NAME + "/#", ITEM);
        return uriMatcher;
    }
    private ItemDbHelper itemDbHelper;

    @Override
    public boolean onCreate() {
        itemDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        int matchCode = uriMatcher.match(uri);

        if (matchCode == ITEMS) {
            return itemDbHelper.readItems(sqLiteDatabase);
        } else if (matchCode == ITEM) {
            return itemDbHelper.readItem(Integer.parseInt(uri.getPathSegments().get(1)), sqLiteDatabase);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matchCode = uriMatcher.match(uri);

        if (matchCode == ITEMS) {
            return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + ItemContract.ItemEntry.TABLE_NAME;
        } else if (matchCode == ITEM) {
            return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + ItemContract.ItemEntry.TABLE_NAME;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        int matchCode = uriMatcher.match(uri);

        if (matchCode==ITEMS || matchCode==ITEM) {
            long itemId = itemDbHelper.addItem(contentValues, sqLiteDatabase);

            return ContentUris.withAppendedId(CONTENT_URI, itemId);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        int matchCode = uriMatcher.match(uri);

        if (matchCode == ITEM) {
            String itemId = uri.getPathSegments().get(1);
            String selection = ItemContract.ItemEntry._ID + " = " + itemId;

            return itemDbHelper.deleteItem(selection, sqLiteDatabase);
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = itemDbHelper.getWritableDatabase();
        int matchCode = uriMatcher.match(uri);

        if (matchCode == ITEM) {
            String itemId = uri.getPathSegments().get(1);
            String selection = ItemContract.ItemEntry._ID + " = " + itemId;

            return itemDbHelper.updateItem(selection, contentValues, sqLiteDatabase);
        }

        return 0;
    }
}
