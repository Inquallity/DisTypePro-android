package ru.ibakaidov.distypepro.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Radko
 */
public class Category {

    private String categoryName;

    @NonNull
    public static List<Category> toList(@Nullable Cursor cursor) {
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    final List<Category> categories = new ArrayList<>(cursor.getCount());
                    do {
                        final Category category = new Category();
                        category.categoryName = cursor.getString(cursor.getColumnIndex(Columns.NAME));
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @NonNull
    public ContentValues toValues() {
        final ContentValues values = new ContentValues();
        values.put(Columns.NAME, categoryName);
        return values;
    }

    public interface Columns extends BaseColumns {

        String NAME = "name";
    }
}
