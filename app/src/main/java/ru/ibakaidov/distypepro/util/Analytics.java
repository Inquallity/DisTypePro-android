package ru.ibakaidov.distypepro.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.yandex.metrica.YandexMetrica;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kolenyov on 31/08/16.
 */
public final class Analytics {

    public static final String EVENT_SAID = "said";

    public static final String EVENT_CREATE_CATEGORY = "create category";

    public static final String EVENT_CREATE_STATEMENT = "create_statement";

    public static final String EVENT_CHANGE_CATEGORY = "change category";

    public static final String EVENT_CHANGE_ONLINE_CHOICE = "change online voice status";

    public static final String EVENT_CHANGE_AFTER_EACH_WORD_FLAG = "change say after each word status";

    private static final String DEFAULT_KEY = "value";

    private Analytics() {
        throw new IllegalStateException("Final class can not be instantiated!");
    }

    public static void trackEvent(@Event String event, @NonNull String value) {
        YandexMetrica.reportEvent(event, packIntoMap(value));
    }

    @NonNull
    private static Map<String, Object> packIntoMap(String value) {
        final Map<String, Object> map = new HashMap<>();
        map.put(DEFAULT_KEY, value);
        return map;
    }

    @StringDef({
            EVENT_SAID, EVENT_CREATE_CATEGORY, EVENT_CREATE_STATEMENT, EVENT_CREATE_CATEGORY,
            EVENT_CHANGE_CATEGORY, EVENT_CHANGE_ONLINE_CHOICE, EVENT_CHANGE_AFTER_EACH_WORD_FLAG
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Event {

    }
}
