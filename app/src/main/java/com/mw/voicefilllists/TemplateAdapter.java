package com.mw.voicefilllists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private List<String> items;
    protected List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries;

    public TemplateAdapter(List<String> items, List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries) {
        this.items = items;
        this.valueGroupDatabaseEntries = valueGroupDatabaseEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EditText columnNameEditText;
        private final Spinner columnDataTypeSpinner;

        public ViewHolder(View itemView) {
            super(itemView);

            // name text input
            columnNameEditText = itemView.findViewById(R.id.columnNameEditText);

            // data type spinner
            List<String> dataTypeNames = new ArrayList<>();
            for (ValueGroupDatabaseEntry dbEntry : valueGroupDatabaseEntries) {
                dataTypeNames.add(dbEntry.name);
            }
            columnDataTypeSpinner = itemView.findViewById(R.id.dataTypeSpinner);
            ArrayAdapter<String> dataTypeAdapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, dataTypeNames);
            dataTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            columnDataTypeSpinner.setAdapter(dataTypeAdapter);
        }

        public void bind(String item) {
            // name text input
            columnNameEditText.setText(item);

            // data type spinner
            // TODO

            // Implement other UI components and interactions here
            // TODO
        }

        public ValueFromGroupColumn getDataListColumn() {
            ValueFromGroupColumn result = new ValueFromGroupColumn();

            // set data id
            int selectedIndex = columnDataTypeSpinner.getSelectedItemPosition();
            ValueGroupDatabaseEntry selectedEntry = valueGroupDatabaseEntries.get(selectedIndex);
            result.valueGroupId = selectedEntry.groupId;

            // set name
            result.columnName = columnNameEditText.getText().toString().trim();

            return result;
        }
    }
}
