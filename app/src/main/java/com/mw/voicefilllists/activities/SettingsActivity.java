package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivityToolbarHelper.setupToolbar(this, R.string.settings_page_title);
        createSettingButtons();
    }

    private void createSettingButtons() {
        addButton(R.string.settings_page_switch_to_my_lists, MyListsActivity.class);
        addButton(R.string.settings_page_switch_to_my_value_groups, MyValueGroupsActivity.class);
        addButton(R.string.settings_page_switch_to_my_values, MyValuesActivity.class);
    }

    private void addButton(int buttonTextStringId, final Class<?> destinationActivity) {
        String buttonText = getString(buttonTextStringId);
        addButton(buttonText, destinationActivity);
    }

    private void addButton(String buttonText, final Class<?> destinationActivity) {
        LinearLayout buttonContainer = findViewById(R.id.settings_page_button_container);


        Button button = new Button(this);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            // Switch to the specified activity
            Intent intent = new Intent(getApplicationContext(), destinationActivity);
            startActivity(intent);
        });

        // Add the button to the LinearLayout
        buttonContainer.addView(button);
    }
}






