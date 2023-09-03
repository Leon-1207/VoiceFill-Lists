package com.mw.voicefilllists.activities;

import android.util.Log;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.DataListPageDatabaseEntry;

public class CreateDataListPageActivity extends DataListPageActivity {
    @Override
    void saveOrUpdateToDatabaseInCurrentThread(DataListPageActivity activity) {
        DataListPageDatabaseEntry databaseEntry = new DataListPageDatabaseEntry();
        databaseEntry.pageName = activity.getNameInputValue();
        assert (activity.getSelectedTemplateId() != null);
        databaseEntry.templateId = (int) activity.getSelectedTemplateId();
        AppDatabase.getInstance(this).dataListPageDAO().insert(databaseEntry);
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.new_list_page);
    }
}
