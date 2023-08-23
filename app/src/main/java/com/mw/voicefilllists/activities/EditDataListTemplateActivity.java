package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.model.DataListTemplate;

public class EditDataListTemplateActivity extends DataListTemplateActivity {
    private int templateId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // load template id
        Intent intent = getIntent();
        assert intent.hasExtra("templateId");
        this.templateId = intent.getIntExtra("templateId", -1);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected DataListTemplate loadInitialDataListTemplateInCurrentThread() {
        return AppDatabase
                .getInstance(this)
                .dataListTemplateDAO()
                .loadDataListTemplate(this.templateId);
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.edit_data_list_template);
    }
}
