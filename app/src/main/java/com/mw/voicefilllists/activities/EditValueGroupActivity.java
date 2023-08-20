package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.DataConverter;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.PhonemeValue;

import java.util.ArrayList;
import java.util.List;

public class EditValueGroupActivity extends ValueGroupActivity {
    public int activityGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // get groupId from intent
        Intent intent = getIntent();
        assert intent != null;
        assert intent.hasExtra("groupId");
        this.activityGroupId = intent.getIntExtra("groupId", -1);

        super.onCreate(savedInstanceState);

        loadNameTextInputValue();
    }

    private void loadNameTextInputValue() {
        // disable name text input until values is loaded
        enableOrDisableNameTextInput(false);

        EditValueGroupActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                ValueGroupDatabaseEntry databaseEntry = database.valueGroupDAO().getById(activity.activityGroupId);
                assert activity.activityGroupId == databaseEntry.groupId;
                runOnUiThread(() -> {
                    EditText editText = activity.findViewById(R.id.groupName);
                    editText.setText(databaseEntry.name);
                    activity.enableOrDisableNameTextInput(true);
                    activity.updateSaveButtonState();
                });
            }
        }).start();
    }

    private void enableOrDisableNameTextInput(boolean enable) {
        findViewById(R.id.groupName).setEnabled(enable);
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.edit_value_group);
    }

    @Override
    protected void onSaveButtonClicked() {
        EditValueGroupActivity activity = this;

        // save to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);

                // update name
                // TODO

                // update connections to PhonemeValues in DB
                // TODO

                activity.finish();
            }
        }).start();
    }

    @Override
    protected void loadValuesInGroup() {
        onStartLoadingValuesInGroup();
        EditValueGroupActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                List<Integer> phonemeValueIdsInGroup = database
                        .valueGroupAndPhonemeValueDAO()
                        .getAllPhonemeValuesIdsOfGroup(activity.activityGroupId);
                List<PhonemeValueDatabaseEntry> phonemeValueDatabaseEntries = database
                        .phonemeValueDao()
                        .loadAllByIds(phonemeValueIdsInGroup);
                List<PhonemeValue> phonemeValues = new ArrayList<>();
                for (PhonemeValueDatabaseEntry dbValue : phonemeValueDatabaseEntries) {
                    phonemeValues.add(DataConverter.convert(dbValue));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onLoadedValuesInGroup(phonemeValues);
                    }
                });
            }
        }).start();
    }
}
