package ru.ibakaidov.distypepro.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.ibakaidov.distypepro.sqlite.tables.CategoryTable;
import ru.ibakaidov.distypepro.sqlite.tables.WordTable;

/**
 * @author Maksim Radko
 */
public class DistypeProvider extends ContentProvider {

    public static final int DB_VERSION = 1;

    private static final String DB_NAME = "default.db";

    private DistypeDbOpenHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DistypeDbOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String where, String[] whereArgs, String orderBy) {
        final String table = uri.getLastPathSegment();
        final Cursor cursor = helper.getReadableDatabase().query(table, columns, where, whereArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + uri.getEncodedPath();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final String table = uri.getLastPathSegment();
        final long lastRowId = helper.getWritableDatabase().insertWithOnConflict(table, BaseColumns._ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, lastRowId);
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        final String table = uri.getLastPathSegment();
        final int affectedRows = helper.getWritableDatabase().delete(table, where, whereArgs);
        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        final String table = uri.getLastPathSegment();
        final int affectedRows = helper.getWritableDatabase().update(table, values, where, whereArgs);
        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    private class DistypeDbOpenHelper extends SQLiteOpenHelper {

        public DistypeDbOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CategoryTable.CREATE_SCRIPT);
            db.execSQL(CategoryTable.INITIAL_SCRIPT);
            db.execSQL(WordTable.CREATE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(WordTable.DELETE_SCRIPT);
            db.execSQL(CategoryTable.DELETE_SCRIPT);
            onCreate(db);
        }
    }
}
