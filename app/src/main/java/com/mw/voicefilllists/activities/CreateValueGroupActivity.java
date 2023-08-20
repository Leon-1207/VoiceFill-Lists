package com.mw.voicefilllists.activities;

import android.app.Activity;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.PhonemeValue;
import com.mw.voicefilllists.localdb.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateValueGroupActivity extends ValueGroupActivity {
    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.new_value_group);
    }

    @Override
    protected void onSaveButtonClicked() {
        Activity activity = this;

        // save to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                // TODO
                activity.finish();
            }
        }).start();
    }

    @Override
    protected void loadValuesInGroup() {
        onStartLoadingValuesInGroup();
        onLoadedValuesInGroup(new ArrayList<>());
    }

    @Override
    protected void loadPossibleValues() {
        onStartLoadingValuesOutOfGroup();
        CreateValueGroupActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                List<PhonemeValue> phonemeValueList = database.loadAllValueGroupEntries(activity);
                ArrayList<PhonemeValue> result = new ArrayList<>();
                for (PhonemeValue entryInList : phonemeValueList) {
                    boolean isAlreadySelected = activity.isValueGroupEntrySelected(entryInList);
                    if (!isAlreadySelected) result.add(entryInList);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onLoadedValuesOutOfGroup(result);
                    }
                });
            }
        }).start();
    }
}
