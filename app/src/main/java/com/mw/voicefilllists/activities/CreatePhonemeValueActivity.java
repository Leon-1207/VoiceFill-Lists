package com.mw.voicefilllists.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;

public class CreatePhonemeValueActivity extends PhonemeValueActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityToolbarHelper.setupToolbar(this, R.string.new_value_group_entry);
    }

    @Override
    protected void onSaveButtonClicked() {
        // save new entry to database
        Activity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(activity);
                PhonemeValueDatabaseEntry dbEntry = new PhonemeValueDatabaseEntry();

                dbEntry.label = getEntryNameInputValue();
                dbEntry.phoneticTranscription = getAllPhoneticTranscriptionsAsCommaSpreadString();
                db.phonemeValueDao().insert(dbEntry);
                // done

                // close activity
                activity.finish();
            }
        }).start();

    }
}
