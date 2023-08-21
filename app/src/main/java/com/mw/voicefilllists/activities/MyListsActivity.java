package com.mw.voicefilllists.activities;

import android.content.Intent;

import com.mw.voicefilllists.R;

public class MyListsActivity extends AbstractViewAndEditDataActivity {
    @Override
    protected void loadValueList() {
        // TODO
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
