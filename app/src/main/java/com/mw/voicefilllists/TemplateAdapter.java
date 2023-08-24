package com.mw.voicefilllists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.DataListColumn;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private List<DataListColumn> items;
    protected List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries;

    public TemplateAdapter(List<DataListColumn> items, List<ValueGroupDatabaseEntry> valueGroupDatabaseEntries) {
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
        DataListColumn item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
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

            // Remove item button
            Button removeItemButton = itemView.findViewById(R.id.removeItemButton);
            removeItemButton.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    items.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

        private int getColumnDataTypeIndex(DataListColumn dataListColumn) {
            if (dataListColumn instanceof ValueFromGroupColumn) {
                for (int i = 0; i < valueGroupDatabaseEntries.size(); i++) {
                    if (valueGroupDatabaseEntries.get(i).groupId == ((ValueFromGroupColumn) dataListColumn).valueGroupId) {
                        return i;
                    }
                }
            } else {
                // TODO
            }
            return 0;
        }

        public void bind(DataListColumn item) {
            // name text input
            columnNameEditText.setText(item.columnName);

            // data type spinner
            int selectedIndex = getColumnDataTypeIndex(item);
            columnDataTypeSpinner.setSelection(selectedIndex, false);

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
