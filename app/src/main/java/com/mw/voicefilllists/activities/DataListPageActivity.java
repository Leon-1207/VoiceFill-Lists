package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.DecoratedSpinnerAdapterWithAddOption;
import com.mw.voicefilllists.LoadingScreen;
import com.mw.voicefilllists.NothingSelectedSpinnerAdapter;
import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;

import java.util.List;

public abstract class DataListPageActivity extends AppCompatActivity {
    private LoadingScreen loadingScreen;
    private List<DataListTemplateDatabaseEntry> templates;
    private Spinner templateSpinner;
    private EditText nameTextInput;
    protected String initialNameValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadingScreen = new LoadingScreen(this);

        setContentView(R.layout.activity_data_list_page);
        this.templateSpinner = findViewById(R.id.pageTemplate);
        this.nameTextInput = findViewById(R.id.pageName);

        setupToolbar();
        setupBottomButtonLine();
        loadDataAndStartSetup();
    }

    private void loadDataAndStartSetup() {
        loadingScreen.show();
        new Thread(() -> {
            loadTemplatesFromDatabaseInCurrentThread();
            loadInitialNameInCurrentThread();
            runOnUiThread(() -> {
                setupNameInput();
                setupTemplateInput();
                loadingScreen.dismiss();
            });
        }).start();
    }

    private void loadTemplatesFromDatabaseInCurrentThread() {
        this.templates = AppDatabase.getInstance(this).dataListTemplateDAO().getAll();
    }

    protected void loadInitialNameInCurrentThread() {
        this.initialNameValue = "";
    }

    protected void setupNameInput() {
        nameTextInput.setText(initialNameValue);
    }

    protected void setupTemplateInput() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (DataListTemplateDatabaseEntry databaseEntry : templates) {
            adapter.add(databaseEntry.name);
        }
        SpinnerAdapter adapter1 = new NothingSelectedSpinnerAdapter(adapter, R.layout.please_select_an_option_item, this);
        SpinnerAdapter adapter2 = new DecoratedSpinnerAdapterWithAddOption(this, adapter1);
        templateSpinner.setAdapter(adapter2);
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

    abstract void saveOrUpdateToDatabaseInCurrentThread(DataListPageActivity activity);

    private void onSaveButtonClicked() {
        loadingScreen.show();
        DataListPageActivity activity = this;
        new Thread(() -> {
            saveOrUpdateToDatabaseInCurrentThread(activity);
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

    protected String getNameInputValue() {
        return nameTextInput.getText().toString().trim();
    }

    protected Integer getSelectedTemplateId() {
        for (DataListTemplateDatabaseEntry template : templates) {
            if (template.name.equals(templateSpinner.getSelectedItem())) return template.templateId;
        }
        return null;
    }

    public Spinner getTemplateSpinner() {
        return templateSpinner;
    }

    public EditText getNameTextInput() {
        return nameTextInput;
    }
}
