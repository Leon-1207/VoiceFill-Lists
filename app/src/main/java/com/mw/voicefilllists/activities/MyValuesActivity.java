package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;

public class MyValuesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_values);
        ActivityToolbarHelper.setupToolbar(this, R.string.my_values_page_title);
        setupCreateNewValueButton();
    }

    private void setupCreateNewValueButton() {
        Button button = findViewById(R.id.createValueGroupEntryButton);
        button.setOnClickListener(v -> {
            // Switch to new activity
            Intent intent = new Intent(MyValuesActivity.this.getApplicationContext(), CreateValueGroupEntryActivity.class);
            MyValuesActivity.this.startActivity(intent);
        });
    }
}
