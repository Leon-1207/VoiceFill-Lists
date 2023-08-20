package com.mw.voicefilllists.activities;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.ValueGroupAndPhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.PhonemeValue;

import java.util.ArrayList;
import java.util.List;

public class CreateValueGroupActivity extends ValueGroupActivity {
    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.new_value_group);
    }

    @Override
    protected void onSaveButtonClicked() {
        CreateValueGroupActivity activity = this;

        // save to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);

                // insert database entry for value group
                ValueGroupDatabaseEntry databaseEntry = new ValueGroupDatabaseEntry();
                databaseEntry.name = activity.getNameInputValue();
                int groupId = (int) database.valueGroupDAO().insert(databaseEntry);

                // insert database entry for each PhonemeValue assigned to the new ValueGroup
                List<PhonemeValue> selectedPhonemeValues = activity.getValuesInGroup();
                ArrayList<ValueGroupAndPhonemeValueDatabaseEntry> databaseEntriesToAdd = new ArrayList<>();
                for (PhonemeValue phonemeValue : selectedPhonemeValues) {
                    ValueGroupAndPhonemeValueDatabaseEntry dbEntry = new ValueGroupAndPhonemeValueDatabaseEntry();
                    dbEntry.groupId = groupId;
                    dbEntry.valueId = phonemeValue.getId();
                    databaseEntriesToAdd.add(dbEntry);
                }
                database.valueGroupAndPhonemeValueDAO().insertAll(databaseEntriesToAdd);

                activity.finish();
            }
        }).start();
    }

    @Override
    protected void loadValuesInGroup() {
        onStartLoadingValuesInGroup();
        onLoadedValuesInGroup(new ArrayList<>());
    }
}
