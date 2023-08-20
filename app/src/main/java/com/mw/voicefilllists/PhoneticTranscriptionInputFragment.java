package com.mw.voicefilllists;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mw.voicefilllists.activities.PhonemeValueActivity;

public class PhoneticTranscriptionInputFragment {
    private final PhonemeValueActivity activity;
    private TextView phoneticTranscriptionView;
    private Button recordButton;
    private String phoneticTranscription;

    public PhoneticTranscriptionInputFragment(PhonemeValueActivity activity) {
        this.activity = activity;
    }

    public void inflate(@NonNull LinearLayout container) {
        // create linear layout (inner container) which holds button and text view
        LinearLayout innerContainer = new LinearLayout(activity);
        innerContainer.setOrientation(LinearLayout.HORIZONTAL);
        innerContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // create button
        recordButton = new Button(activity);
        recordButton.setText(R.string.start_record);
        recordButton.setOnClickListener(v -> {
            if (isRecording()) clickedStopRecording();
            else clickedStartRecording();
        });

        // create text view
        phoneticTranscriptionView = new TextView(activity);
        phoneticTranscriptionView.setSingleLine(false);

        // add views together
        innerContainer.addView(recordButton);
        innerContainer.addView(phoneticTranscriptionView);
        container.addView(innerContainer);

        phoneticTranscription = "";
    }

    protected boolean isRecording() {
        return activity.getActivePhoneticTranscriptionInput() == this;
    }

    private void clickedStartRecording() {
        activity.setActivePhoneticTranscriptionInputAndListen(this);
    }

    private void clickedStopRecording() {
        if (isRecording()) activity.stopRecording();
    }

    public void onRecordingStarted() {
        phoneticTranscription = "";
        recordButton.setText(R.string.stop_record);
    }

    public void onPhoneticTranscription(String text) {
        phoneticTranscriptionView.setText(text);
        recordButton.setText(R.string.start_record);

        // save value
        String processedTranscription = removeSubstringAtStartAndEnd(text, "SIL");
        setPhoneticTranscription(processedTranscription);
    }

    private String removeSubstringAtStartAndEnd(String input, String substringToRemove) {
        if (input.startsWith(substringToRemove)) {
            input = input.substring(substringToRemove.length());
        }

        if (input.endsWith(substringToRemove)) {
            input = input.substring(0, input.length() - substringToRemove.length());
        }

        return input.trim();
    }

    public String getPhoneticTranscription() {
        return phoneticTranscription;
    }

    public void setPhoneticTranscription(String phoneticTranscription) {
        this.phoneticTranscription = phoneticTranscription;
    }
}
