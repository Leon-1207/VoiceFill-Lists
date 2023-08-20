package com.mw.voicefilllists.activities;

import android.app.Activity;

public class CreateValueGroupActivity extends ValueGroupActivity {
    @Override
    protected void onSaveButtonClicked() {
        Activity activity = this;

        // save to database
        // TODO

        // close activity
        activity.finish();  // TODO move inside runnable
    }

    @Override
    protected void loadValuesInGroup() {
        onStartLoadingValuesInGroup();

    }

    @Override
    protected void loadPossibleValues() {
        // TODO
    }
}
