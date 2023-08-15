package edu.cmu.pocketsphinx.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends PhoneRecognizerActivity {

    private TextView firstRowText, secondRowText, thirdRowText;
    private Button firstRowButton, secondRowButton, thirdRowButton;

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
        thirdRowButton = findViewById(R.id.thirdRowButton);

        // reset values
        firstValue = "";
        secondValue = "";
        recordingIndex = -1;

        firstRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the first row is clicked
                if (recordingIndex == 0) stopRecording(0);
                else {
                    if (recordingIndex == 1) stopRecording(1);
                    startRecording(0);
                }
            }
        });

        secondRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the second row is clicked
                if (recordingIndex == 1) stopRecording(1);
                else {
                    if (recordingIndex == 0) stopRecording(0);
                    startRecording(1);
                }
            }
        });

        thirdRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Compare button in the third row is clicked
                // TODO
            }
        });
    }

    private void stopRecording(int index) {
        String result = "TODO";

        TextView textView = (index == 0) ? firstRowText : secondRowText;
        textView.setText(result);
        textView.setTypeface(null, Typeface.NORMAL);

        Button button = (index == 0) ? firstRowButton : secondRowButton;
        button.setText("Record");

        recordingIndex = -1;
    }

    private void startRecording(int index) {
        recordingIndex = index;

        TextView textView = (index == 0) ? firstRowText : secondRowText;
        textView.setText("recording");
        textView.setTypeface(null, Typeface.ITALIC);

        Button button = (index == 0) ? firstRowButton : secondRowButton;
        button.setText("Stop");
    }
}
