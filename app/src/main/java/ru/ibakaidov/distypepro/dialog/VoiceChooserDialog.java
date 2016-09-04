package ru.ibakaidov.distypepro.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import ru.ibakaidov.distypepro.R;
import ru.ibakaidov.distypepro.util.PrefUtils;
import ru.yandex.speechkit.Vocalizer;

/**
 * @author Maksim Radko
 */
public class VoiceChooserDialog extends DialogFragment {

    // TODO: 9/4/16 Implement mapping this values on localized names
    private static final String[] VOICES = new String[]{
            Vocalizer.Voice.ZAHAR,
            Vocalizer.Voice.ALYSS,
            Vocalizer.Voice.ERMIL,
            Vocalizer.Voice.JANE,
            Vocalizer.Voice.OMAZH
    };

    public static void show(@NonNull FragmentManager fm) {
        final VoiceChooserDialog dialog = new VoiceChooserDialog();
        dialog.show(fm, VoiceChooserDialog.class.getName());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.voice_chooser_choose_voice)
                .setSingleChoiceItems(VOICES, findCheckedItem(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefUtils.setCurrentVoice(getActivity(), VOICES[which]);
                    }
                })
                .create();
    }

    private int findCheckedItem() {
        final String currentVoice = PrefUtils.getCurrentVoice(getActivity());
        for (int i = 0; i < VOICES.length; i++) {
            if (TextUtils.equals(VOICES[i], currentVoice)) {
                return i;
            }
        }
        return 0;
    }
}
