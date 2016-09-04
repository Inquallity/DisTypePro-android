package ru.ibakaidov.distypepro.util;

import android.util.Log;

import ru.ibakaidov.distypepro.BuildConfig;

/**
 * @author Roman Bugaian on 30/08/16.
 */
public class Logger {

    public static void debug(String log) {
        if (BuildConfig.DEBUG) {
            Log.d(BuildConfig.APPLICATION_ID, log);
        }
    }
}
