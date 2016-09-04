package ru.ibakaidov.distypepro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import ru.yandex.speechkit.Vocalizer;

/**
 * @author Maksim Radko
 */
public final class PrefUtils {

    private static final String KEY_SELECTED_VOICE = "SELECTED_VOICE";

    private PrefUtils() {
        throw new IllegalStateException("Final class can not be instantiated!");
    }

    @NonNull
    private static SharedPreferences getPref(@NonNull Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
    }

    @NonNull
    public static String getCurrentVoice(@NonNull Context ctx) {
        return getPref(ctx).getString(KEY_SELECTED_VOICE, Vocalizer.Voice.ZAHAR);
    }

    public static void setCurrentVoice(@NonNull Context ctx, @NonNull String voice) {
        getPref(ctx).edit().putString(KEY_SELECTED_VOICE, voice).apply();
    }
}
