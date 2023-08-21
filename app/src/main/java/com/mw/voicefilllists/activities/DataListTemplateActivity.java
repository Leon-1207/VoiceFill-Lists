package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;

public abstract class DataListTemplateActivity extends AppCompatActivity {
    private TemplateAdapter templateAdapter;
    private List<String> items;
    private List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries;
    private LoadingScreen loadingScreen;

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

    private void loadDataAndStartSetup() {
        DataListTemplateActivity activity = this;
        loadingScreen.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                activity.valueGroupDatabaseEntries = database.valueGroupDAO().getAll();
                runOnUiThread(activity::setupRecyclerView);
                activity.loadingScreen.dismiss();
            }
        }).start();
    }

    protected abstract void setupToolbar();

    private void setupRecyclerView() {
        items = new ArrayList<>();
        items.add("First Name");
        items.add("Last Name");
        items.add("Email");

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

    protected abstract void onSaveButtonClicked();

    private void updateSaveButtonState() {
        // TODO
    }

    private void addItem() {
        items.add("New Item");
        templateAdapter.notifyItemInserted(items.size() - 1);
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
