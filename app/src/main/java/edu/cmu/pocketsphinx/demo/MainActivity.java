package edu.cmu.pocketsphinx.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Hypothesis;

public class MainActivity extends PhoneRecognizerActivity {

    private TextView firstRowText, secondRowText, thirdRowText;
    private Button firstRowButton, secondRowButton;
    private PhoneStringComparator comparator;

    private String firstValue, secondValue;
    private int recordingIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstRowText = findViewById(R.id.firstRowText);
        secondRowText = findViewById(R.id.secondRowText);
        thirdRowText = findViewById(R.id.thirdRowText);

        firstRowButton = findViewById(R.id.firstRowButton);
        secondRowButton = findViewById(R.id.secondRowButton);

        comparator = new PhoneStringComparator();

        // reset values
        firstValue = "";
        secondValue = "";
        recordingIndex = -1;

        firstRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the first row is clicked
                if (recordingIndex == 0) stopRecording();
                else {
                    if (recordingIndex == 1) stopRecording();
                    startRecording(0);
                }
            }
        });

        secondRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the second row is clicked
                if (recordingIndex == 1) stopRecording();
                else {
                    if (recordingIndex == 0) stopRecording();
                    startRecording(1);
                }
            }
        });
    }

    private void stopRecording() {
        stopListening();
    }

    @Override
    public void stopListening() {
        super.stopListening();
        if (recordingIndex != -1) {
            TextView textView = (recordingIndex == 0) ? firstRowText : secondRowText;
            textView.setText("");
            textView.setTypeface(null, Typeface.NORMAL);

            Button button = (recordingIndex == 0) ? firstRowButton : secondRowButton;
            button.setText("Record");
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (recordingIndex != -1) {
            if (hypothesis != null) {
                String text = hypothesis.getHypstr();
                handleRecordingResult(recordingIndex, text);
            } else handleRecordingResult(recordingIndex, "");
        }
    }

    private void handleRecordingResult(int index, String result) {
        TextView textView = (index == 0) ? firstRowText : secondRowText;
        textView.setText(result);
        textView.setTypeface(null, Typeface.NORMAL);

        Button button = (index == 0) ? firstRowButton : secondRowButton;
        button.setText("Record");

        updateValue(index, result);
        recordingIndex = -1;

        // enable buttons
        firstRowButton.setEnabled(true);
        secondRowButton.setEnabled(true);
    }

    private void startRecording(int index) {
        recordingIndex = index;

        TextView textView = (index == 0) ? firstRowText : secondRowText;
        textView.setText("recording");
        textView.setTypeface(null, Typeface.ITALIC);

        Button button = (index == 0) ? firstRowButton : secondRowButton;
        button.setText("Stop");

        // disable other button
        Button otherButton = (index == 0) ? secondRowButton : firstRowButton;
        otherButton.setEnabled(false);

        startListening();
    }

    private void updateValue(int index, String value) {
        if (index == 0) firstValue = value;
        else if (index == 1) secondValue = value;
        compareValues();
    }

    private void compareValues() {
        float result = comparator.compare(firstValue, secondValue);
        thirdRowText.setText(String.format("%s", result));
    }
}
