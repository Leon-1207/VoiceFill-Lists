package com.mw.voicefilllists.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

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

    @Override
    protected void onSavedData(int templateId) {
        Log.i("CreateDataListTemplateActivity", "Tempalte saved (id=" + templateId + "). Starting result intent...");
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newDataListTemplateId", templateId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
