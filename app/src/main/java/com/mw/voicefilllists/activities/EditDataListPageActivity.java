package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.DataListPageDatabaseEntry;

public class EditDataListPageActivity extends DataListPageActivity {
    int pageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // load page id
        Intent intent = getIntent();
        assert intent.hasExtra("pageId");
        this.pageId = intent.getIntExtra("pageId", -1);

        super.onCreate(savedInstanceState);
        findViewById(R.id.pageTemplate).setVisibility(View.GONE);
        displaySelectedTemplate();
    }

    private void displaySelectedTemplate() {
        findViewById(R.id.currentTemplate).setVisibility(View.INVISIBLE);
        EditDataListPageActivity activity = this;
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(activity);
            int templateId = db.dataListPageDAO().getById(pageId).templateId;
            String templateName = db.dataListTemplateDAO().getById(templateId).name;
            runOnUiThread(() -> {
                TextView textView = activity.findViewById(R.id.currentTemplate);
                textView.setText(templateName);
                textView.setVisibility(View.VISIBLE);
            });
        }).start();
    }

    @Override
    void saveOrUpdateToDatabaseInCurrentThread(DataListPageActivity activity) {
        DataListPageDatabaseEntry databaseEntry = AppDatabase.getInstance(activity).dataListPageDAO().getById(pageId);
        databaseEntry.pageName = activity.getNameInputValue();
        AppDatabase.getInstance(activity).dataListPageDAO().update(databaseEntry);
    }


    @Override
    protected void loadInitialNameInCurrentThread() {
        initialNameValue = AppDatabase.getInstance(this).dataListPageDAO().getById(pageId).pageName;
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.edit_list_page);
    }
}
