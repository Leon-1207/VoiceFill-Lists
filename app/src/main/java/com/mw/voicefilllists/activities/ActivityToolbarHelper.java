package com.mw.voicefilllists.activities;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.mw.voicefilllists.R;

public class ActivityToolbarHelper {
    public static void setupToolbar(Activity activity, int pageTitle) {
        View toolbar = activity.findViewById(R.id.toolbar);

        View backButton = toolbar.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> activity.onBackPressed());

        TextView titleView = toolbar.findViewById(R.id.pageTitle);
        titleView.setText(pageTitle);
    }
}
