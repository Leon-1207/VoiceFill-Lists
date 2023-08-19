package com.mw.voicefilllists.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mw.voicefilllists.R;

public class CreateValueGroupEntryActivity extends ValueGroupEntryActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityToolbarHelper.setupToolbar(this, R.string.new_value_group_entry);
    }
}
