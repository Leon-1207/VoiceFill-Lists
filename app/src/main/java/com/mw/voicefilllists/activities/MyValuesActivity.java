package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.ValueGroupEntry;
import com.mw.voicefilllists.localdb.AppDatabase;

import java.util.Arrays;
import java.util.List;

public class MyValuesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_values);
        ActivityToolbarHelper.setupToolbar(this, R.string.my_values_page_title);
        setupCreateNewValueButton();
        loadValueList();
    }

    private void loadValueList() {
        MyValuesActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ValueGroupEntry> loadedValueGroupEntries = AppDatabase
                        .getInstance(activity)
                        .loadAllValueGroupEntries(activity);

                // add data to UI
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        LinearLayout container = findViewById(R.id.valueGroupEntries);
                        for (ValueGroupEntry valueGroupEntry : loadedValueGroupEntries) {
                            TextView newView = new TextView(activity);
                            newView.setText(String.format("%s %s", valueGroupEntry.getLabel(), Arrays.toString(valueGroupEntry.getPhoneticTranscription())));
                            container.addView(newView);
                        }
                    }
                });
            }
        }).start();
    }

    private void setupCreateNewValueButton() {
        Button button = findViewById(R.id.createValueGroupEntryButton);
        button.setOnClickListener(v -> {
            // Switch to new activity
            Intent intent = new Intent(MyValuesActivity.this.getApplicationContext(), CreateValueGroupEntryActivity.class);
            MyValuesActivity.this.startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValueList();
    }
}
