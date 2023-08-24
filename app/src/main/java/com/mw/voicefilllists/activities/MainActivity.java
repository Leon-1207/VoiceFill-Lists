package com.mw.voicefilllists.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;

public class MainActivity extends AppCompatActivity {
    private ClickDurationData menuClickDurationData;
    private ClickDurationData settingsClickDurationData;

    private abstract static class ClickDurationData {
        public final Handler handler;
        public boolean isLongClick;

        public ClickDurationData() {
            this.handler = new Handler();
            this.isLongClick = false;
        }

        public final Runnable longClickRunnable = () -> {
            isLongClick = true;
            onLongClick();
        };

        public abstract void onShortClick();

        public abstract void onLongClick();
    }

    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // settings button
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        this.settingsClickDurationData = new ClickDurationData() {
            @Override
            public void onShortClick() {
                onSettingsButtonClicked();
            }

            @Override
            public void onLongClick() {
                onSettingsButtonLongClicked();
            }
        };
        settingsButton.setOnTouchListener((v, event) -> handleSecurityButtonClick(event, settingsClickDurationData));


        // list page menu button
        this.menuClickDurationData = new ClickDurationData() {
            @Override
            public void onShortClick() {
                onPageMenuButtonClicked();
            }

            @Override
            public void onLongClick() {
                onPageMenuButtonLongClicked();
            }
        };
        findViewById(R.id.templateSheetMenuButton).setOnTouchListener((v, event) -> handleSecurityButtonClick(event, menuClickDurationData));
    }

    private boolean handleSecurityButtonClick(MotionEvent event, ClickDurationData clickDurationData) {
        // Button has to be held instead of clicked
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickDurationData.handler.postDelayed(clickDurationData.longClickRunnable, 1000); // 1000 milliseconds = 1 seconds
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clickDurationData.handler.removeCallbacks(clickDurationData.longClickRunnable);
                if (!clickDurationData.isLongClick) {
                    // Short click action
                    clickDurationData.onShortClick();
                }
                clickDurationData.isLongClick = false;
                break;
        }
        return true;
    }

    private void onSettingsButtonLongClicked() {
        // Handle long click action
        // Switch to the settings activity
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void onSettingsButtonClicked() {
        // Handle short click action
        Toast.makeText(this, R.string.settings_button_short_click_toast, Toast.LENGTH_SHORT).show();
    }

    private void onPageMenuButtonLongClicked() {
        // switch activity
        Intent intent = new Intent(getApplicationContext(), MyListPagesActivity.class);
        startActivity(intent);
    }

    private void onPageMenuButtonClicked() {
        // Handle short click action
        Toast.makeText(this, R.string.page_menu_button_short_click_toast, Toast.LENGTH_SHORT).show();
    }
}

