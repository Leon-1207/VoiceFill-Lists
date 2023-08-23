package com.mw.voicefilllists.activities;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.model.DataListTemplate;

public class CreateDataListTemplateActivity extends DataListTemplateActivity {
    @Override
    protected DataListTemplate loadInitialDataListTemplateInCurrentThread() {
        return new DataListTemplate();
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.create_data_list_template);
    }
}
