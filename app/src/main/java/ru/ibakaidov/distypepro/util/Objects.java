package ru.ibakaidov.distypepro.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Maksim Radko
 */
public final class Objects {

    @NonNull
    public static <T> T requireNonNull(@Nullable T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
}
