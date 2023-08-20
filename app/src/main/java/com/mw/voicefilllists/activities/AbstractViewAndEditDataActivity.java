package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;

public abstract class AbstractViewAndEditDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstract_activity_view_and_edit_entries);
        setupToolbar();
        setupCreateNewValueButton();
        loadValueList();
    }

    /**
     * Loads current entries from local database and adds them to view
     */
    protected abstract void loadValueList();

    protected abstract void setupToolbar();

    protected abstract void switchToCreateEntryActivity();

    protected abstract int getNewEntryButtonStringResource();

    private void setupCreateNewValueButton() {
        Button button = findViewById(R.id.createNewEntryButton);
        button.setText(getNewEntryButtonStringResource());
        button.setOnClickListener(v -> switchToCreateEntryActivity());
    }

    protected LinearLayout getValueListView() {
        return findViewById(R.id.entryList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValueList();
    }
}
