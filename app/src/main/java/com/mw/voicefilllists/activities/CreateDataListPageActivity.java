package com.mw.voicefilllists.activities;

import com.mw.voicefilllists.R;

public class CreateDataListPageActivity extends DataListPageActivity {
    @Override
    void saveOrUpdateToDatabaseInCurrentThread() {
        // TODO
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.new_list_page);
    }
}
