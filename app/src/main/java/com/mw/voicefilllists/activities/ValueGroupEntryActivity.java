package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.PhoneticTranscriptionInputFragment;
import com.mw.voicefilllists.R;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public abstract class ValueGroupEntryActivity extends RecognizerActivity {
    protected final int PHONETIC_TRANSCRIPTION_COUNT = 3;
    private final ArrayList<PhoneticTranscriptionInputFragment> phoneticTranscriptionInputs = new ArrayList<>();
    private PhoneticTranscriptionInputFragment activePhoneticTranscriptionInput = null;
    private EditText entryNameEdit;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_group_entry);

        // create input for phonetic transcription
        LinearLayout inputContainer = findViewById(R.id.phoneticTranscriptionInputContainer);
        for (int i = 0; i < PHONETIC_TRANSCRIPTION_COUNT; i++) {
            PhoneticTranscriptionInputFragment phoneticInput = new PhoneticTranscriptionInputFragment(this);
            phoneticInput.inflate(inputContainer);
            phoneticTranscriptionInputs.add(phoneticInput);
        }

        // add listener for name input
        entryNameEdit = findViewById(R.id.entryLabel);
        entryNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSaveButtonState();
            }
        });

        updateSaveButtonState();

        // setup cancel button
        findViewById(R.id.bottomButtonLine)
                .findViewById(R.id.bottomCancelButton)
                .setOnClickListener(v -> onBackPressed());

        // setup save button
        findViewById(R.id.bottomButtonLine)
                .findViewById(R.id.bottomSaveButton)
                .setOnClickListener(v -> onSaveButtonClicked());
    }

    protected abstract void onSaveButtonClicked();

    @Override
    public void onResult(Hypothesis hypothesis) {
        String text = hypothesis != null ? hypothesis.getHypstr() : "";
        activePhoneticTranscriptionInput.onPhoneticTranscription(text);
        activePhoneticTranscriptionInput = null;
        updateSaveButtonState();
    }

    public PhoneticTranscriptionInputFragment getActivePhoneticTranscriptionInput() {
        return activePhoneticTranscriptionInput;
    }

    public void setActivePhoneticTranscriptionInputAndListen(PhoneticTranscriptionInputFragment activePhoneticTranscriptionInput) {
        if (activePhoneticTranscriptionInput != null) stopRecording();
        this.activePhoneticTranscriptionInput = activePhoneticTranscriptionInput;
        assert activePhoneticTranscriptionInput != null;
        activePhoneticTranscriptionInput.onRecordingStarted();
        startListening();
        updateSaveButtonState();
    }

    public void stopRecording() {
        stopListening();
        updateSaveButtonState();
    }


    @Override
    public void stopListening() {
        super.stopListening();
    }

    protected String getEntryNameInputValue() {
        return entryNameEdit.getText().toString().trim();
    }

    public void updateSaveButtonState() {
        // check name input
        String name = getEntryNameInputValue();
        if (name.length() < 1) {
            setSaveButtonState(false);
            return;
        }

        // check phonetic transcription inputs
        for (PhoneticTranscriptionInputFragment phoneticTranscriptionInput : phoneticTranscriptionInputs) {
            String valueOfInput = phoneticTranscriptionInput.getPhoneticTranscription();
            if (valueOfInput.length() < 1) {
                setSaveButtonState(false);
                return;
            }
        }

        // enable save button
        setSaveButtonState(true);
    }

    private void setSaveButtonState(boolean enabled) {
        View buttonLine = findViewById(R.id.bottomButtonLine);
        buttonLine.findViewById(R.id.bottomSaveButton).setEnabled(enabled);
    }

    protected String getAllPhoneticTranscriptionsAsCommaSpreadString() {
        StringBuilder result = new StringBuilder();

        for (PhoneticTranscriptionInputFragment phoneticTranscriptionInput : phoneticTranscriptionInputs) {
            String valueOfInput = phoneticTranscriptionInput.getPhoneticTranscription();
            result.append(valueOfInput);
            result.append(",");
        }

        if (result.length() > 0) {
            String lastChar = String.valueOf(result.charAt(result.length() - 1));
            if (lastChar.equalsIgnoreCase(","))
                result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }
}
