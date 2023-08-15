package edu.cmu.pocketsphinx.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends PhoneRecognizerActivity {

    private TextView firstRowText, secondRowText, thirdRowText;
    private Button firstRowButton, secondRowButton, thirdRowButton;

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

        firstRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the first row is clicked
                // Add your code here
            }
        });

        secondRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record button in the second row is clicked
                // Add your code here
            }
        });

        thirdRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Compare button in the third row is clicked
                // Add your code here
            }
        });
    }
}
