package ru.ibakaidov.distypepro;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;

import ru.yandex.speechkit.SpeechKit;

/**
 * @author Maksim Radko
 */
public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetrica.activate(this, BuildConfig.YA_METRICA_KEY);
        SpeechKit.getInstance().configure(this, BuildConfig.YA_GENERAL_KEY);
    }
}
