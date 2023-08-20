package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.ValueGroupEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class ValueGroupActivity extends AppCompatActivity {
    protected ArrayList<ValueGroupEntry> valuesInGroup, possibleValues;
    private boolean loadingValuesInGroup = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_group);
        setupBottomButtonLine();
        valuesInGroup = new ArrayList<>();
        possibleValues = new ArrayList<>();
        loadValuesInGroup();
    }

    private void setupBottomButtonLine() {
        View buttonLine = findViewById(R.id.bottomButtonLine);
        assert buttonLine != null;

        // save button
        Button saveButton = buttonLine.findViewById(R.id.bottomSaveButton);
        saveButton.setOnClickListener(v -> onSaveButtonClicked());

        // cancel button
        Button cancelButton = buttonLine.findViewById(R.id.bottomCancelButton);
        cancelButton.setOnClickListener(v -> onBackPressed());

        updateSaveButtonState();
    }

    protected String getNameInputValue() {
        EditText nameEdit = findViewById(R.id.groupName);
        return nameEdit.getText().toString().trim();
    }

    protected boolean isNameValid() {
        String nameInputValue = getNameInputValue();
        return nameInputValue.length() > 2;
    }

    protected boolean isValuesInGroupValid() {
        return !valuesInGroup.isEmpty();
    }

    private void updateSaveButtonState() {
        Button saveButton = findViewById(R.id.bottomButtonLine).findViewById(R.id.bottomSaveButton);
        boolean enable = (!loadingValuesInGroup) && isNameValid() && isValuesInGroupValid();
        saveButton.setEnabled(enable);
    }

    protected abstract void onSaveButtonClicked();

    protected abstract void loadValuesInGroup();

    protected abstract void loadPossibleValues();

    protected void onLoadedValuesInGroup(List<ValueGroupEntry> entries) {
        while (!valuesInGroup.isEmpty()) valuesInGroup.remove(0);
        valuesInGroup.addAll(entries);
    }

    protected void onLoadedValuesOutOfGroup(List<ValueGroupEntry> entries) {
        while (!possibleValues.isEmpty()) possibleValues.remove(0);
        possibleValues.addAll(entries);
    }

    private void fillContainerWithValueGroupEntries(LinearLayout container, List<ValueGroupEntry> entries) {
        for (ValueGroupEntry valueGroupEntry : entries) {
            ValueButton valueButton = new ValueButton(valueGroupEntry);
            View viewForValue = valueButton.inflate(this);
            container.addView(viewForValue);
        }
    }

    protected boolean isValueGroupEntrySelected(ValueGroupEntry valueGroupEntry) {
        for (ValueGroupEntry entryInList : valuesInGroup) {
            if (entryInList.getId() == valueGroupEntry.getId()) return true;
        }
        return false;
    }

    protected void onStartLoadingValuesInGroup() {
        onLoadingValuesInGroupChanged(true);
    }

    protected void onEndLoadingValuesInGroup() {
        onLoadingValuesInGroupChanged(false);
    }

    private void onLoadingValuesInGroupChanged(boolean newLoadingState) {
        loadingValuesInGroup = newLoadingState;
        onLoadingStateChanged(newLoadingState, getValuesInGroupContainer(), findViewById(R.id.valuesInGroupLoadingSpinner));
        updateSaveButtonState();
    }

    private void onLoadingStateChanged(boolean loading, View container, View loadingSpinner) {
        container.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        loadingSpinner.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    protected LinearLayout getValuesInGroupContainer() {
        return findViewById(R.id.valuesInGroupContainer);
    }

    protected LinearLayout getPossibleValuesContainer() {
        return findViewById(R.id.valuesOutOfGroupContainer);
    }

    protected void selectValue(ValueButton valueButton) {
        valuesInGroup.add(valueButton.getValueGroupEntry());
        getPossibleValuesContainer().removeView(valueButton.getView());
        getValuesInGroupContainer().addView(valueButton.inflate(this));
        loadPossibleValues();
    }

    protected void deselectValue(ValueButton valueButton) {
        ValueGroupEntry valueGroupEntry = valueButton.getValueGroupEntry();
        int index = 0;
        for (ValueGroupEntry entryInList : valuesInGroup) {
            if (entryInList.getId() == valueGroupEntry.getId()) {
                // remove from list
                valuesInGroup.remove(index);

                // remove from upper container
                LinearLayout container = getValuesInGroupContainer();
                container.removeView(valueButton.getView());
                break;
            }
            index += 1;
        }
        loadPossibleValues();
    }

    protected class ValueButton {
        private final ValueGroupEntry valueGroupEntry;
        private View view;

        protected ValueButton(ValueGroupEntry valueGroupEntry) {
            this.valueGroupEntry = valueGroupEntry;
        }

        public View inflate(ValueGroupActivity activity) {
            Button result = new Button(activity);
            result.setText(valueGroupEntry.getLabel());
            result.setOnClickListener(v -> {
                if (activity.isValueGroupEntrySelected(this.valueGroupEntry)) {
                    // currently selected -> deselect
                    activity.deselectValue(this);
                } else {
                    // currently not selected -> select now
                    activity.selectValue(this);
                }
            });
            this.view = result;
            return result;
        }

        public ValueGroupEntry getValueGroupEntry() {
            return this.valueGroupEntry;
        }

        public View getView() {
            return view;
        }
    }
}
