package ru.ibakaidov.distypepro.sqlite.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ibakaidov.distypepro.BuildConfig;
import ru.ibakaidov.distypepro.model.Word;

/**
 * @author Maksim Radko
 */
public class WordTable {

    public static final String TABLE_NAME = "words";

    public static final Uri TABLE_URI = Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + TABLE_NAME);

    public static final String CREATE_SCRIPT = String.format(
            "CREATE TABLE IF NOT EXISTS %1$s (_id INTEGER PRIMARY KEY, %2$s TEXT, %3$s INTEGER, UNIQUE (%2$s) ON CONFLICT REPLACE);",
            WordTable.TABLE_NAME, Columns.WORD, Columns.CATEGORY_ID);

    public static final String DELETE_SCRIPT = String.format(
            "DROP TABLE IF EXISTS %s;",
            WordTable.TABLE_NAME);

    public static List<Word> toList(@Nullable Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                final List<Word> words = new ArrayList<>(cursor.getCount());
                do {
                    final Word word = new Word();
                    word.setWordString(cursor.getString(cursor.getColumnIndex(Columns.WORD)));
                    word.setCategoryId(cursor.getLong(cursor.getColumnIndex(Columns.CATEGORY_ID)));
                    words.add(word);
                } while (cursor.moveToNext());
                return words;
            }
        }
        return Collections.emptyList();
    }

    @NonNull
    public static ContentValues toValues(@NonNull Word word) {
        final ContentValues values = new ContentValues();
        values.put(Columns.WORD, word.getWordString());
        values.put(Columns.CATEGORY_ID, word.getCategoryId());
        return values;
    }

    public interface Columns extends BaseColumns {

        String WORD = "word";

        String CATEGORY_ID = "category_id";
    }
}
