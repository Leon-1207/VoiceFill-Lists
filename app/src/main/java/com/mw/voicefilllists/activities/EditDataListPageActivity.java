package com.mw.voicefilllists.activities;

import com.mw.voicefilllists.R;

public class EditDataListPageActivity extends DataListPageActivity {
    @Override
    void saveOrUpdateToDatabaseInCurrentThread(DataListPageActivity activity) {
        // TODO
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.edit_list_page);
    }
}
