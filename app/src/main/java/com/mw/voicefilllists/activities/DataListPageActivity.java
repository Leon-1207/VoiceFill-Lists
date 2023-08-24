package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.LoadingScreen;
import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;

import java.util.List;

public abstract class DataListPageActivity extends AppCompatActivity {
    private LoadingScreen loadingScreen;
    private List<DataListTemplateDatabaseEntry> templates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadingScreen = new LoadingScreen(this);
        setContentView(R.layout.activity_data_list_page);
        setupToolbar();
        loadDataAndStartSetup();
        setupBottomButtonLine();
    }

    private void loadDataAndStartSetup() {

    }

    private void setupBottomButtonLine() {
        View buttonLine = findViewById(R.id.bottomButtonLine);
        assert buttonLine != null;

        // save button
        Button saveButton = buttonLine.findViewById(R.id.bottomSaveButton);
        saveButton.setOnClickListener(v -> onSaveButtonClicked());

        // cancel button
        Button cancelButton = buttonLine.findViewById(R.id.bottomCancelButton);
        cancelButton.setOnClickListener(v -> onBackPressed());

        updateSaveButtonState();
    }

    abstract void saveOrUpdateToDatabaseInCurrentThread();

    private void onSaveButtonClicked() {
        loadingScreen.show();
        DataListPageActivity activity = this;
        new Thread(() -> {
            saveOrUpdateToDatabaseInCurrentThread();
            runOnUiThread(() -> {
                activity.loadingScreen.dismiss();
                activity.finish();
            });
        }).start();
    }

    private void updateSaveButtonState() {
        // TODO
    }

    protected abstract void setupToolbar();
}
