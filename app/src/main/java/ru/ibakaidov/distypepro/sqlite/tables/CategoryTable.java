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
import ru.ibakaidov.distypepro.model.Category;

/**
 * @author Maksim Radko
 */
public class CategoryTable {

    public static final String TABLE_NAME = "categories";

    public static final Uri TABLE_URI = Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + TABLE_NAME);

    public static final String CREATE_SCRIPT = String.format(
            "CREATE TABLE IF NOT EXISTS %1$s (_id INTEGER PRIMARY KEY, %2$s TEXT, UNIQUE (%2$s) ON CONFLICT REPLACE);" +
                    "CREATE TRIGGER IF NOT EXISTS tr_cascade_to_words BEFORE DELETE ON %3$s FOR EACH ROW " +
                    "BEGIN" +
                    "DELETE FROM %4$s WHERE %5$s=OLD._id;" +
                    "END",
            TABLE_NAME, Columns.NAME,
            TABLE_NAME, WordTable.TABLE_NAME, WordTable.Columns.CATEGORY_ID
    );

    public static final String INITIAL_SCRIPT = String.format(
            "INSERT INTO %1$s VALUES (0, \"Default category\");",
            TABLE_NAME
    );

    public static final String DELETE_SCRIPT = String.format(
            "DROP TABLE IF EXISTS %s;",
            TABLE_NAME
    );

    public static final long DEFAULT_CATEGORY_ID = 0L;

    @NonNull
    public static List<Category> toList(@Nullable Cursor cursor) {
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    final List<Category> categories = new ArrayList<>(cursor.getCount());
                    do {
                        final Category category = new Category();
                        category.setCategoryName(cursor.getString(cursor.getColumnIndex(Columns.NAME)));
                        categories.add(category);
                    } while (cursor.moveToNext());
                    return categories;
                }
            } finally {
                cursor.close();
            }
        }
        return Collections.emptyList();
    }

    @NonNull
    public static ContentValues toValues(@NonNull Category category) {
        final ContentValues values = new ContentValues();
        values.put(Columns.NAME, category.getCategoryName());
        return values;
    }

    public interface Columns extends BaseColumns {

        String NAME = "name";
    }
}
