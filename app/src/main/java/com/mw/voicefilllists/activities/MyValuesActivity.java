package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.ValueGroupEntry;
import com.mw.voicefilllists.localdb.AppDatabase;

import java.util.Arrays;
import java.util.List;

public class MyValuesActivity extends AbstractViewAndEditDataActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void loadValueList() {
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
                        LinearLayout container = getValueListView();
                        for (ValueGroupEntry valueGroupEntry : loadedValueGroupEntries) {
                            TextView newView = new TextView(activity);
                            newView.setText(String.format("%s %s", valueGroupEntry.getLabel(),
                                    Arrays.toString(valueGroupEntry.getPhoneticTranscription())));
                            container.addView(newView);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.my_values_page_title);
    }

    @Override
    protected void switchToCreateEntryActivity() {
        Intent intent = new Intent(MyValuesActivity.this.getApplicationContext(), CreateValueGroupEntryActivity.class);
        MyValuesActivity.this.startActivity(intent);
    }

    @Override
    protected int getNewEntryButtonStringResource() {
        return R.string.new_value_group_entry;
    }
}
