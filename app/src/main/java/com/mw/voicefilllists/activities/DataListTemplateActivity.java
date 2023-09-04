package com.mw.voicefilllists.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mw.voicefilllists.LoadingScreen;
import com.mw.voicefilllists.R;
import com.mw.voicefilllists.TemplateAdapter;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.DataListColumn;
import com.mw.voicefilllists.model.DataListTemplate;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

import java.util.ArrayList;
import java.util.List;

public abstract class DataListTemplateActivity extends AppCompatActivity {
    private TemplateAdapter templateAdapter;
    private List<DataListColumn> items;
    private List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries;
    private LoadingScreen loadingScreen;
    private DataListTemplate initialDataListTemplate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadingScreen = new LoadingScreen(this);
        setContentView(R.layout.activity_data_list_template);
        setupToolbar();
        this.valueGroupDatabaseEntries = null;
        loadDataAndStartSetup();
        setupBottomButtonLine();
    }

    protected abstract DataListTemplate loadInitialDataListTemplateInCurrentThread();

    private void loadDataAndStartSetup() {
        DataListTemplateActivity activity = this;
        loadingScreen.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                activity.valueGroupDatabaseEntries = database.valueGroupDAO().getAll();

                // load initial DataListTemplate
                activity.initialDataListTemplate = activity.loadInitialDataListTemplateInCurrentThread();
                System.out.println("ID: " +
                        (activity.initialDataListTemplate.hasTemplateId() ? activity.initialDataListTemplate.getTemplateId() : "NONE")
                        + (" Columns: " + (activity.initialDataListTemplate.columns == null ? "NONE" : activity.initialDataListTemplate.columns.size()))
                        + " Name: " + activity.initialDataListTemplate.name);

                runOnUiThread(() -> {
                    activity.setupTextView();
                    activity.setupRecyclerView();
                });
                activity.loadingScreen.dismiss();
            }
        }).start();
    }

    private void setupTextView() {
        EditText editText = findViewById(R.id.templateName);
        editText.setText(initialDataListTemplate.name);
    }

    protected abstract void setupToolbar();

    private void setupRecyclerView() {
        items = new ArrayList<>();
        if (this.initialDataListTemplate.columns != null) {
            items.addAll(this.initialDataListTemplate.columns);
        }

        templateAdapter = new TemplateAdapter(items, valueGroupDatabaseEntries);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(templateAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(templateAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        findViewById(R.id.addItemButton).setOnClickListener(v -> addItem());
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

    private void onSaveButtonClicked() {
        DataListTemplateActivity activity = this;
        loadingScreen.show();
        DataListTemplate dataListTemplate = this.createDataListTemplateFromInput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // save to database
                    int id = AppDatabase.getInstance(activity).saveDataListTemplate(activity, dataListTemplate);

                    // finish activity
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.loadingScreen.dismiss();
                            activity.onSavedData(id);
                        }
                    });
                } catch (Exception exception) {
                    // finish activity
                    runOnUiThread(() -> {
                        activity.loadingScreen.dismiss();
                        Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }

    protected void onSavedData(int templateId) {
        this.finish();
    }

    private void updateSaveButtonState() {
        // TODO
    }

    private void addItem() {
        items.add(new DataListColumn());
        templateAdapter.notifyItemInserted(items.size() - 1);
    }

    public DataListTemplate createDataListTemplateFromInput() {
        DataListTemplate result = new DataListTemplate();

        // set id
        if (this.initialDataListTemplate.hasTemplateId()) {
            result.setTemplateId(this.initialDataListTemplate.getTemplateId());
        }

        // set name
        result.name = getNameInputValue();

        // set columns
        result.columns = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        int columnCount = this.templateAdapter.getItemCount();
        for (int i = 0; i < columnCount; i++) {
            TemplateAdapter.ViewHolder columnViewHolder = (TemplateAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (columnViewHolder != null) {
                result.columns.add(columnViewHolder.getDataListColumn());
            }
        }

        return result;
    }

    protected String getNameInputValue() {
        EditText input = findViewById(R.id.templateName);
        return input.getText().toString().trim();
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        private TemplateAdapter adapter;

        public ItemTouchHelperCallback(TemplateAdapter adapter) {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAbsoluteAdapterPosition();
            int toPosition = target.getAbsoluteAdapterPosition();
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Not used in this example
        }
    }
}
