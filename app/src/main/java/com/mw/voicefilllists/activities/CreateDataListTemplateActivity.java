package com.mw.voicefilllists.activities;

import com.mw.voicefilllists.R;

public class CreateDataListTemplateActivity extends DataListTemplateActivity {
    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.create_data_list_template);
    }
}
