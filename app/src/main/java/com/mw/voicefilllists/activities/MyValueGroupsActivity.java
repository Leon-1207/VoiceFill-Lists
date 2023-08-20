package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

import java.util.List;

public class MyValueGroupsActivity extends AbstractViewAndEditDataActivity {
    @Override
    protected void loadValueList() {
        MyValueGroupsActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                List<ValueGroupDatabaseEntry> databaseEntries = database.valueGroupDAO().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.displayLoadedDatabaseEntries(databaseEntries);
                    }
                });
            }
        }).start();
    }

    private void displayLoadedDatabaseEntries(List<ValueGroupDatabaseEntry> databaseEntries) {
        LinearLayout container = findViewById(R.id.entryList);
        container.removeAllViews();
        for (ValueGroupDatabaseEntry dbEntry : databaseEntries) {
            View viewForGroup = getViewForDbEntry(dbEntry);
            container.addView(viewForGroup);
        }
    }

    private View getViewForDbEntry(ValueGroupDatabaseEntry databaseEntry) {
        Button result = new Button(this);
        result.setText(databaseEntry.name);
        result.setOnClickListener(v -> {
            Intent intent = new Intent(MyValueGroupsActivity.this, EditValueGroupActivity.class);
            intent.putExtra("groupId", databaseEntry.groupId);
            startActivity(intent);
        });
        return result;
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.my_value_groups_page_title);
    }

    @Override
    protected void switchToCreateEntryActivity() {
        Intent intent = new Intent(MyValueGroupsActivity.this.getApplicationContext(), CreateValueGroupActivity.class);
        MyValueGroupsActivity.this.startActivity(intent);
    }

    @Override
    protected int getNewEntryButtonStringResource() {
        return R.string.new_value_group;
    }
}
