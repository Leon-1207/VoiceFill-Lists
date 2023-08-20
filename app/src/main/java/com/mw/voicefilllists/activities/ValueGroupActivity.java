package com.mw.voicefilllists.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.model.PhonemeValue;

import java.util.ArrayList;
import java.util.List;

public abstract class ValueGroupActivity extends AppCompatActivity {

    protected ArrayList<PhonemeValue> valuesInGroup, possibleValues;
    private boolean loadingValuesInGroup = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_group);
        setupBottomButtonLine();
        setupToolbar();
        valuesInGroup = new ArrayList<>();
        possibleValues = new ArrayList<>();
        setupNameInput();
        loadValuesInGroup();
        loadPossibleValues();
    }

    protected void setupNameInput() {
        EditText nameTextEdit = findViewById(R.id.groupName);
        nameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSaveButtonState();
            }
        });
    }

    protected abstract void setupToolbar();

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
        return nameInputValue.length() > 0;
    }

    protected boolean isValuesInGroupValid() {
        return !valuesInGroup.isEmpty();
    }

    protected void updateSaveButtonState() {
        Button saveButton = findViewById(R.id.bottomButtonLine).findViewById(R.id.bottomSaveButton);
        boolean enable = (!loadingValuesInGroup) && isNameValid() && isValuesInGroupValid();
        saveButton.setEnabled(enable);
    }

    protected abstract void onSaveButtonClicked();

    protected abstract void loadValuesInGroup();

    protected void onLoadedValuesInGroup(List<PhonemeValue> entries) {
        while (!valuesInGroup.isEmpty()) valuesInGroup.remove(0);
        valuesInGroup.addAll(entries);
        fillContainerWithValueGroupEntries(getValuesInGroupContainer(), entries);
        onEndLoadingValuesInGroup();
        loadPossibleValues();
    }

    protected void onLoadedValuesOutOfGroup(List<PhonemeValue> entries) {
        while (!possibleValues.isEmpty()) possibleValues.remove(0);
        possibleValues.addAll(entries);
        fillContainerWithValueGroupEntries(getPossibleValuesContainer(), entries);
        onEndLoadingValuesOutOfGroup();
    }

    private void fillContainerWithValueGroupEntries(LinearLayout container, List<PhonemeValue> entries) {
        container.removeAllViews();
        for (PhonemeValue phonemeValue : entries) {
            ValueButton valueButton = new ValueButton(phonemeValue);
            View viewForValue = valueButton.inflate(this);
            container.addView(viewForValue);
        }
    }

    protected boolean isValueGroupEntrySelected(PhonemeValue phonemeValue) {
        for (PhonemeValue entryInList : valuesInGroup) {
            if (entryInList.getId() == phonemeValue.getId()) return true;
        }
        return false;
    }

    protected void onStartLoadingValuesInGroup() {
        onLoadingValuesInGroupChanged(true);
    }

    private void onEndLoadingValuesInGroup() {
        onLoadingValuesInGroupChanged(false);
    }

    protected void onStartLoadingValuesOutOfGroup() {
        onLoadingStateChanged(true, getPossibleValuesContainer(), findViewById(R.id.valuesOutOfGroupLoadingSpinner));
    }

    private void onEndLoadingValuesOutOfGroup() {
        onLoadingStateChanged(false, getPossibleValuesContainer(), findViewById(R.id.valuesOutOfGroupLoadingSpinner));
    }

    private void onLoadingValuesInGroupChanged(boolean newLoadingState) {
        loadingValuesInGroup = newLoadingState;
        onLoadingStateChanged(newLoadingState, getValuesInGroupContainer(), findViewById(R.id.valuesInGroupLoadingSpinner));
        updateSaveButtonState();
    }

    private void onLoadingStateChanged(boolean loading, View container, View loadingSpinner) {
        container.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        loadingSpinner.setVisibility(loading ? View.VISIBLE : View.GONE);
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
        updateSaveButtonState();
    }

    protected void deselectValue(ValueButton valueButton) {
        PhonemeValue phonemeValue = valueButton.getValueGroupEntry();
        int index = 0;
        for (PhonemeValue entryInList : valuesInGroup) {
            if (entryInList.getId() == phonemeValue.getId()) {
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
        updateSaveButtonState();
    }

    public ArrayList<PhonemeValue> getValuesInGroup() {
        return valuesInGroup;
    }

    private void loadPossibleValues() {
        onStartLoadingValuesOutOfGroup();
        ValueGroupActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(activity);
                List<PhonemeValue> phonemeValueList = database.loadAllValueGroupEntries(activity);
                ArrayList<PhonemeValue> result = new ArrayList<>();
                for (PhonemeValue entryInList : phonemeValueList) {
                    boolean isAlreadySelected = activity.isValueGroupEntrySelected(entryInList);
                    if (!isAlreadySelected) result.add(entryInList);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onLoadedValuesOutOfGroup(result);
                    }
                });
            }
        }).start();
    }

    protected class ValueButton {
        private final PhonemeValue phonemeValue;
        private View view;

        protected ValueButton(PhonemeValue phonemeValue) {
            this.phonemeValue = phonemeValue;
        }

        public View inflate(ValueGroupActivity activity) {
            Button result = new Button(activity);
            result.setText(phonemeValue.getLabel());
            result.setOnClickListener(v -> {
                if (activity.isValueGroupEntrySelected(this.phonemeValue)) {
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

        public PhonemeValue getValueGroupEntry() {
            return this.phonemeValue;
        }

        public View getView() {
            return view;
        }
    }
}
