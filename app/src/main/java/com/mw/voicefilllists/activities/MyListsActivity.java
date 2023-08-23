package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.widget.Button;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;

import java.util.List;

public class MyListsActivity extends AbstractViewAndEditDataActivity {
    @Override
    protected void loadValueList() {
        loadingScreen.show();
        MyListsActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // load data
                List<DataListTemplateDatabaseEntry> databaseEntryList = AppDatabase.getInstance(activity).dataListTemplateDAO().getAll();

                runOnUiThread(() -> {
                    fillTemplateList(databaseEntryList);
                    activity.loadingScreen.dismiss();  // stop loading screen
                });
            }
        }).start();
    }

    private void fillTemplateList(List<DataListTemplateDatabaseEntry> databaseEntryList) {
        getValueListView().removeAllViews();
        for (DataListTemplateDatabaseEntry dbEntry : databaseEntryList) {
            Button viewForEntry = new Button(this);
            viewForEntry.setText(dbEntry.name);
            System.out.println(dbEntry.name + " " + dbEntry.templateId);
            viewForEntry.setOnClickListener(v -> {
                // switch to edit template activity
                Intent intent = new Intent(MyListsActivity.this.getApplicationContext(), EditDataListTemplateActivity.class);
                intent.putExtra("templateId", dbEntry.templateId);
                MyListsActivity.this.startActivity(intent);
            });
            getValueListView().addView(viewForEntry);
        }
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.my_lists);
    }

    @Override
    protected void switchToCreateEntryActivity() {
        Intent intent = new Intent(MyListsActivity.this.getApplicationContext(), CreateDataListTemplateActivity.class);
        MyListsActivity.this.startActivity(intent);
    }

    @Override
    protected int getNewEntryButtonStringResource() {
        return R.string.new_list;
    }
}
