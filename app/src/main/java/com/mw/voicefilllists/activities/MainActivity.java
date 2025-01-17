package com.mw.voicefilllists.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mw.voicefilllists.DictionaryModifier;
import com.mw.voicefilllists.JSGFParser;
import com.mw.voicefilllists.LoadingScreen;
import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.DataConverter;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;
import com.mw.voicefilllists.model.ColumnWithGrammar;
import com.mw.voicefilllists.model.DataListColumn;
import com.mw.voicefilllists.model.DataListPage;
import com.mw.voicefilllists.model.DataListTemplate;
import com.mw.voicefilllists.model.PhonemeValue;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends RecognizerActivity {
    private static final String TEMPLATE_GRAMMAR_SEARCH = "template_grammar";
    private static final String WAKE_UP_SEARCH = "wakeup";
    private ClickDurationData menuClickDurationData;
    private ClickDurationData settingsClickDurationData;
    private DataListPage dataListPage = null;
    private LoadingScreen loadingScreen;
    private boolean loadingDataList = false;
    private boolean loadingRecognizer = false;
    private String jsgfGrammar;
    private final String wakeUpKeyphrase = "hey";

    private abstract static class ClickDurationData {
        public final Handler handler;
        public boolean isLongClick;

        public ClickDurationData() {
            this.handler = new Handler();
            this.isLongClick = false;
        }

        public final Runnable longClickRunnable = () -> {
            isLongClick = true;
            onLongClick();
        };

        public abstract void onShortClick();

        public abstract void onLongClick();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        loadingScreen = new LoadingScreen(this);

        super.onCreate(savedInstanceState);

        // settings button
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        this.settingsClickDurationData = new ClickDurationData() {
            @Override
            public void onShortClick() {
                onSettingsButtonClicked();
            }

            @Override
            public void onLongClick() {
                onSettingsButtonLongClicked();
            }
        };
        settingsButton.setOnTouchListener((v, event) -> handleSecurityButtonClick(event, settingsClickDurationData));

        // list page menu button
        this.menuClickDurationData = new ClickDurationData() {
            @Override
            public void onShortClick() {
                onPageMenuButtonClicked();
            }

            @Override
            public void onLongClick() {
                onPageMenuButtonLongClicked();
            }
        };
        findViewById(R.id.templateSheetMenuButton).setOnTouchListener((v, event) -> handleSecurityButtonClick(event, menuClickDurationData));

        // load pageId from intent extra
        setDataListPage(null);
        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("pageId")) {
            loadList(intent.getIntExtra("pageId", -1));
        }
    }

    private void loadList(int pageId) {
        MainActivity activity = this;
        setLoadingDataList(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataListPage dataListPage = AppDatabase.getInstance(activity).loadDataListPageCompletely(activity, pageId);
                setDataListPage(dataListPage);
                setLoadingDataList(false);
            }
        }).start();
    }

    public void setDataListPage(DataListPage dataListPage) {
        this.dataListPage = dataListPage;
        boolean isListSelected = dataListPage != null;
        findViewById(R.id.noListSelectedContent).setVisibility(isListSelected ? View.GONE : View.VISIBLE);
        findViewById(R.id.listSelectedContent).setVisibility(isListSelected ? View.VISIBLE : View.GONE);
        if (isListSelected) {
            // load speech recognizer
            this.startAsyncSetup();

            // insert page and template name into button
            TextView pageTemplateTextView = findViewById(R.id.templateNameTextView);
            pageTemplateTextView.setText(dataListPage.getTemplate().name);
            TextView pageNameTextView = findViewById(R.id.pageIndicatorTextView);
            pageNameTextView.setText(dataListPage.getName());
        }
    }

    private boolean handleSecurityButtonClick(MotionEvent event, ClickDurationData clickDurationData) {
        // Button has to be held instead of clicked
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickDurationData.handler.postDelayed(clickDurationData.longClickRunnable, 1000); // 1000 milliseconds = 1 seconds
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clickDurationData.handler.removeCallbacks(clickDurationData.longClickRunnable);
                if (!clickDurationData.isLongClick) {
                    // Short click action
                    clickDurationData.onShortClick();
                }
                clickDurationData.isLongClick = false;
                break;
        }
        return true;
    }

    private void onSettingsButtonLongClicked() {
        // Handle long click action
        // Switch to the settings activity
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void onSettingsButtonClicked() {
        // Handle short click action
        Toast.makeText(this, R.string.settings_button_short_click_toast, Toast.LENGTH_SHORT).show();
    }

    private void onPageMenuButtonLongClicked() {
        // switch activity
        Intent intent = new Intent(getApplicationContext(), MyListPagesActivity.class);
        startActivity(intent);
    }

    private void onPageMenuButtonClicked() {
        // Handle short click action
        Toast.makeText(this, R.string.page_menu_button_short_click_toast, Toast.LENGTH_SHORT).show();
    }

    public void setLoadingRecognizer(boolean loadingRecognizer) {
        this.loadingRecognizer = loadingRecognizer;
        updateLoadingScreen();
    }

    public void setLoadingDataList(boolean loadingDataList) {
        this.loadingDataList = loadingDataList;
        updateLoadingScreen();
    }

    private void updateLoadingScreen() {
        runOnUiThread(() -> {
            if (loadingDataList || loadingRecognizer) loadingScreen.show();
            else loadingScreen.dismiss();
        });
    }

    @Override
    public void onStartSetup() {
        super.onStartSetup();
        setLoadingRecognizer(true);
    }

    @Override
    public void onEndSetup() {
        super.onEndSetup();
        setLoadingRecognizer(false);
    }

    @Override
    protected void setupRecognizer(File assetsDir) throws IOException {
        if (dataListPage == null) return;
        DataListTemplate template = dataListPage.getTemplate();

        // create recognizer for current data list

        // setup dictionary for active data list
        // copy existing
        File dictionaryForDataList = new File(assetsDir, "modified-dictionary.dic");
        File baseDictionary = new File(assetsDir, BASE_DICTIONARY);
        if (dictionaryForDataList.exists()) dictionaryForDataList.delete();
        DictionaryModifier.copyFile(baseDictionary, dictionaryForDataList);

        // add words and load phone values
        // TODO handle multiple transcriptions
        DictionaryModifier dictionaryModifier = new DictionaryModifier(dictionaryForDataList);
        AppDatabase db = AppDatabase.getInstance(this);
        for (DataListColumn column : template.columns) {
            if (column instanceof ValueFromGroupColumn) {
                int valueGroupId = ((ValueFromGroupColumn) column).valueGroupId;
                List<Integer> phonemeIdsInGroup = db.valueGroupAndPhonemeValueDAO().getAllPhonemeValuesIdsOfGroup(valueGroupId);
                List<PhonemeValueDatabaseEntry> phonemeValueDatabaseEntries = db.phonemeValueDao().loadAllByIds(phonemeIdsInGroup);
                List<PhonemeValue> phonemeValues = new ArrayList<>();
                for (PhonemeValueDatabaseEntry dbEntry : phonemeValueDatabaseEntries) {
                    if (dbEntry.label != null && dbEntry.phoneticTranscription != null && dbEntry.label.length() > 0 && dbEntry.phoneticTranscription.length() > 0) {
                        PhonemeValue phonemeValue = DataConverter.convert(dbEntry);
                        phonemeValues.add(phonemeValue);
                        if (!dictionaryModifier.isWordInDictionary(phonemeValue.getLabel())) {
                            // add to dictionary
                            dictionaryModifier.addWordToDictionary(phonemeValue);
                        }
                    }
                }
                ((ValueFromGroupColumn) column).phonemeValues = phonemeValues;
            }
        }

        // recognizer setup
        recognizer = SpeechRecognizerSetup
                .defaultSetup()
                .setAcousticModel(new File(assetsDir, ACOUSTIC_MODEL))
                .setDictionary(dictionaryForDataList)
                .getRecognizer();
        recognizer.addListener(this);

        // add searches
        this.jsgfGrammar = getGrammarJsgfString(template);
        recognizer.addGrammarSearch(TEMPLATE_GRAMMAR_SEARCH, this.jsgfGrammar);

        /*
        String menuGrammar = "#JSGF V1.0;\n" +
                "grammar menu;\n" +
                "public <command> = " + addEntryCommand + ";";
        recognizer.addGrammarSearch();
        */

        recognizer.addKeyphraseSearch(WAKE_UP_SEARCH, wakeUpKeyphrase);

        switchSearch(WAKE_UP_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(WAKE_UP_SEARCH)) {
            recognizer.startListening(searchName, 5000);
        } else {
            recognizer.startListening(searchName, 5000);
        }
        ((TextView) findViewById(R.id.stateTextView)).setText(searchName);
    }

    @Override
    public void onTimeout() {
        switchSearch(WAKE_UP_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String recognizedString = hypothesis.getHypstr();
        Log.d(LOG_TAG, recognizedString);
        if (recognizedString.equals(wakeUpKeyphrase))
            switchSearch(TEMPLATE_GRAMMAR_SEARCH);
        else {
            if (JSGFParser.check(recognizedString, jsgfGrammar)) {
                onSuccessfulCommand(recognizedString);
            }
        }
    }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(WAKE_UP_SEARCH))
            switchSearch(WAKE_UP_SEARCH);
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    private String getGrammarJsgfString(DataListTemplate template) {
        StringBuilder dynamicGrammar = new StringBuilder("#JSGF V1.0;\n" +
                "grammar dynamic;\n");

        // gather all sub commands
        Set<String> subCommands = new HashSet<>();  // use a set of strings to make sure that each value group is only inserted once as a grammar rule
        for (DataListColumn column : template.columns) {
            String str = getGrammarJsgfStringRowForColumn(column);
            if (str != null) {
                subCommands.add(str);
            }
        }

        // add sub commands to grammar
        for (String subCommand : subCommands) {
            dynamicGrammar.append(subCommand);
            dynamicGrammar.append("\n");
        }

        // add <command>
        dynamicGrammar.append("public <command> = ");
        for (DataListColumn column : template.columns) {
            assert (column instanceof ColumnWithGrammar);
            String commandPart = ((ColumnWithGrammar) column).getGrammarCommandName();
            dynamicGrammar.append(commandPart);
            dynamicGrammar.append(" ");
        }
        dynamicGrammar.append(";");

        Log.d(LOG_TAG, dynamicGrammar.toString());
        return dynamicGrammar.toString();
    }

    private String getGrammarJsgfStringRowForColumn(DataListColumn column) {
        assert (column instanceof ColumnWithGrammar);
        return ((ColumnWithGrammar) column).getGrammarJsgfRow();
    }

    private void onSuccessfulCommand(String command) {
        Log.d(LOG_TAG, "---> " + command);
        switchSearch(WAKE_UP_SEARCH);
    }
}

