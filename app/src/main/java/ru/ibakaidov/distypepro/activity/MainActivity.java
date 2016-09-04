package ru.ibakaidov.distypepro.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import ru.ibakaidov.distypepro.BuildConfig;
import ru.ibakaidov.distypepro.DB;
import ru.ibakaidov.distypepro.IsOnlineVoiceController;
import ru.ibakaidov.distypepro.R;
import ru.ibakaidov.distypepro.SayButtonController;
import ru.ibakaidov.distypepro.SpeechController;
import ru.ibakaidov.distypepro.TTS;


public class MainActivity extends AppCompatActivity {

    TTS tts;

    private DB db;

    private RecyclerView rvWords;

    private RecyclerView rvCategories;

    private EditText etText;

    private IsOnlineVoiceController iovc;

    private SpeechController sc;

    private SayButtonController sbc;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        sc = new SpeechController(this, getString(R.string.new_speech), null);
//        sbc.setSC(sc);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.choose_voice) {
            final String[] voices = getResources().getStringArray(R.array.voices);
            final String[] voiceNames = new String[]{"ALYSS", "ERMIL", "JANE", "OMAZH", "ZAHAR"};


            SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            final String sCurrentVoice = getString(R.string.current_voice);
            int idCurrentVoice = sharedPref.getInt(sCurrentVoice, 4);

            final SharedPreferences.Editor editor = sharedPref.edit();

            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setSingleChoiceItems(voices, idCurrentVoice, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface d, int n) {
                    d.cancel();
                    tts.voice = voiceNames[n];
                    editor.putInt(sCurrentVoice, n);
                    editor.apply();

                }

            });
            adb.setNegativeButton(R.string.cancel, null);
            adb.setTitle(R.string.choose_voice);
            adb.show();
            return true;
        }

        if (id == R.id.clear) {
//            si.setText("");
            return true;
        }

        if (id == R.id.is_online_voice || id == R.id.say_after_word_input) {
            iovc.onMenuItemClick(item);
            return true;
        }

        if (id == R.id.selectSpeech) {
            sc.openDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initViews();


        tts = new TTS(getApplicationContext(), BuildConfig.YA_GENERAL_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tts.update();
        YandexMetrica.onResumeActivity(this);
    }

    @Override
    protected void onPause() {
        YandexMetrica.onPauseActivity(this);
        super.onPause();
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        rvWords = (RecyclerView) findViewById(R.id.rvWords);
        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);

        etText = (EditText) findViewById(R.id.etTextForSpeech);

        final TextView btnSay = (TextView) findViewById(R.id.btnSay);

//        db = new DB(MainActivity.this, getString(R.string.withoutCategory));

//        cc.setWC(wc);
//        iovc = new IsOnlineVoiceController(tts);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, db.getStatements());
//        si.setAdapter(adapter);

//        Button sb = (Button) findViewById(R.id.btnSay);
//        sbc = new SayButtonController(si, db, cc, wc, tts);

//        sb.setOnClickListener(sbc);
//
//        categoriesLV.setOnItemClickListener(cc);
//        categoriesLV.setOnItemLongClickListener(cc);
//        wordsLV.setOnItemClickListener(wc);
//        wordsLV.setOnItemLongClickListener(wc);
//
//        cc.loadCategories();
//        wc.loadStatements();
    }
}
