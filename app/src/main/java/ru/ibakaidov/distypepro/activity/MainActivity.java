package ru.ibakaidov.distypepro.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import java.util.List;

import ru.ibakaidov.distypepro.R;
import ru.ibakaidov.distypepro.adapter.CategoriesListAdapter;
import ru.ibakaidov.distypepro.adapter.WordsListAdapter;
import ru.ibakaidov.distypepro.dialog.VoiceChooserDialog;
import ru.ibakaidov.distypepro.model.Category;
import ru.ibakaidov.distypepro.model.Word;
import ru.ibakaidov.distypepro.sqlite.tables.CategoryTable;
import ru.ibakaidov.distypepro.sqlite.tables.WordTable;
import ru.ibakaidov.distypepro.util.Analytics;
import ru.ibakaidov.distypepro.util.ItemTouchListenerImpl;
import ru.ibakaidov.distypepro.util.Objects;
import ru.ibakaidov.distypepro.util.PrefUtils;
import ru.ibakaidov.distypepro.util.TextSpeaker;
import ru.ibakaidov.distypepro.util.TextWatcherImpl;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Initializer;
import ru.yandex.speechkit.InitializerListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, InitializerListener {

    private RecyclerView rvWords;

    private RecyclerView rvCategories;

    private EditText etTextPrimary;

    private EditText etTextSecondary;

    private FloatingActionButton fabAddCategory;

    private TextSpeaker ttsDelegate;

    private ConnectivityManager connectivityManager;

    private WordsListAdapter wordsAdapter = new WordsListAdapter();

    private CategoriesListAdapter categoriesAdapter = new CategoriesListAdapter();

    private TextWatcherImpl inputWatcher = new TextWatcherImpl() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    private TextView btnSay;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_voice:
                VoiceChooserDialog.show(getFragmentManager());
                return true;
            case R.id.clear:
                final View focus = getWindow().getCurrentFocus();
                if (focus instanceof EditText) {
                    ((EditText) focus).setText(null);
                }
                return true;
            case R.id.is_online_voice:
                // TODO: 9/4/16 Implement this feature
                //Analytics.changeOnlineValueEvent(this.mTextSpeaker.isOnline);
            case R.id.say_after_word_input:
                // TODO: 9/4/16 Implement this feature
                //Analytics.changeSayingAfterWordValueEvent(this.mTextSpeaker.isSayAfterWordInput);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSay) {
            final View currentFocus = getWindow().getCurrentFocus();
            if (currentFocus instanceof EditText) {
                final String text = ((EditText) currentFocus).getText().toString();
                if (isConnected()) {
                    ttsDelegate.speakWithYandex(text, PrefUtils.getCurrentVoice(this));
                } else {
                    ttsDelegate.speakWithGoogle(text);
                }
                saveWord(text);
            }
        } else if (v.getId() == R.id.fabAddCategory) {
            Snackbar.make(rvCategories, "Add category feature not implemented yet", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializerBegin(Initializer initializer) {
        btnSay.setText(R.string.main_speech_initialization_label);
    }

    @Override
    public void onInitializerDone(Initializer initializer) {
        btnSay.setText(R.string.main_say);
    }

    @Override
    public void onError(Initializer initializer, Error error) {
        btnSay.setText(getString(R.string.main_speech_error_label, error.getString()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        initViews();
        Initializer.create(this).start();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ttsDelegate = new TextSpeaker(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.onResumeActivity(this);
    }

    @Override
    protected void onPause() {
        YandexMetrica.onPauseActivity(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        ttsDelegate.stopService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ttsDelegate.shutdownService();
        super.onDestroy();
    }

    private void saveWord(@NonNull String text) {
        final Word word = new Word();
        word.setWordString(text);
        word.setCategoryId(CategoryTable.DEFAULT_CATEGORY_ID);
        final ContentValues values = WordTable.toValues(word);
        // TODO: 9/4/16 Add universal thread pool for asynchronous task
        getContentResolver().insert(WordTable.TABLE_URI, values);
        Analytics.trackEvent(Analytics.EVENT_CREATE_STATEMENT, text);
    }

    private boolean isConnected() {
        final NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnected();
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        rvWords = (RecyclerView) findViewById(R.id.rvWords);
        rvWords.setItemAnimator(new DefaultItemAnimator());
        rvWords.setLayoutManager(new LinearLayoutManager(this));
        rvWords.addOnItemTouchListener(new ItemTouchListenerImpl(this, new OnWordTouchListener()));
        rvWords.setAdapter(wordsAdapter);

        rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.addOnItemTouchListener(new ItemTouchListenerImpl(this, new OnCategoryTouchListener()));
        rvCategories.setAdapter(categoriesAdapter);

        etTextPrimary = (EditText) findViewById(R.id.etTextForSpeechPrimary);
        Objects.requireNonNull(etTextPrimary);
        etTextPrimary.addTextChangedListener(inputWatcher);

        etTextSecondary = (EditText) findViewById(R.id.etTextForSpeechSecondary);
        Objects.requireNonNull(etTextSecondary);
        etTextSecondary.addTextChangedListener(inputWatcher);

        btnSay = (TextView) findViewById(R.id.btnSay);
        Objects.requireNonNull(btnSay);
        btnSay.setOnClickListener(this);

        fabAddCategory = (FloatingActionButton) findViewById(R.id.fabAddCategory);
        Objects.requireNonNull(fabAddCategory);
        fabAddCategory.setOnClickListener(this);

        getLoaderManager().initLoader(R.id.l_words, Bundle.EMPTY, new CursorLoaderCallbacks());
        getLoaderManager().initLoader(R.id.l_category, Bundle.EMPTY, new CursorLoaderCallbacks());
    }

    private class CursorLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == R.id.l_words) {
                return new CursorLoader(getApplicationContext(), WordTable.TABLE_URI, null, null, null, null);
            } else if (id == R.id.l_category) {
                return new CursorLoader(getApplicationContext(), CategoryTable.TABLE_URI, null, null, null, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null || data.getCount() == 0) {
                return;
            }
            if (R.id.l_words == loader.getId()) {
                final List<Word> words = WordTable.toList(data);
                wordsAdapter.changeDataSet(words);
            }
            if (R.id.l_category == loader.getId()) {
                final List<Category> categories = CategoryTable.toList(data);
                categoriesAdapter.changeDataSet(categories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private class OnCategoryTouchListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            final View view = rvCategories.findChildViewUnder(e.getX(), e.getY());
            final int position = rvCategories.getChildAdapterPosition(view);
            if (view != null) {
                final Category category = categoriesAdapter.getItem(position);
                Snackbar.make(rvCategories, "Selected " + category.getCategoryName() + " category.", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }

    private class OnWordTouchListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            final View view = rvWords.findChildViewUnder(e.getX(), e.getY());
            final int position = rvWords.getChildAdapterPosition(view);
            if (view != null) {
                final Word category = wordsAdapter.getItem(position);
                Snackbar.make(rvWords, "Selected " + category.getWordString() + " word.", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }
}
