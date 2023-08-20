package com.mw.voicefilllists.activities;

import android.content.Intent;

import com.mw.voicefilllists.R;

public class MyValueGroupsActivity extends AbstractViewAndEditDataActivity {
    @Override
    protected void loadValueList() {
        // TODO
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.my_value_groups_page_title);
    }

    @Override
    protected void switchToCreateEntryActivity() {
        Intent intent = new Intent(MyValueGroupsActivity.this.getApplicationContext(), CreateValueGroupEntryActivity.class);
        MyValueGroupsActivity.this.startActivity(intent);
    }

    @Override
    protected int getNewEntryButtonStringResource() {
        return R.string.new_value_group;
    }
}
