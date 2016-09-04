package ru.ibakaidov.distypepro.util;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.yandex.speechkit.Vocalizer;

/**
 * @author Bakaidov on 27.05.16.
 */
public class TextSpeaker {

    private TextToSpeech tts;

    public TextSpeaker(@NonNull Context ctx) {
        tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Logger.debug("TTS init status: " + (status == TextToSpeech.SUCCESS ? "success" : "error"));
            }
        });
    }

    public void speakWithYandex(@Nullable String text, @NonNull String voice) {
        if (!TextUtils.isEmpty(text)) {
            Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, text, true, voice).start();
        }
    }

    @SuppressWarnings("deprecation")
    public void speakWithGoogle(@Nullable String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, "");
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stopService() {
        tts.stop();
    }

    public void shutdownService() {
        tts.shutdown();
    }
}
