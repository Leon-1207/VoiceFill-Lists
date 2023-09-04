package com.mw.voicefilllists;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class DecoratedSpinnerAdapterWithAddOption implements SpinnerAdapter {
    private final SpinnerAdapter baseAdapter;
    private final Context context;

    public DecoratedSpinnerAdapterWithAddOption(Context context, SpinnerAdapter baseAdapter) {
        this.context = context;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            // This is the "Add new entry" option
            // Create a special view for it
            // You can customize the view for this option as needed
            // Here, we'll use a simple text view
            // Replace R.layout.add_new_entry_layout with your custom layout if needed
            View addNewEntryView = View.inflate(context, R.layout.please_select_an_option_item, null);

            // Handle the click event for the "Add new entry" option
            addNewEntryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start an intent when the option is clicked
                    
                }
            });

            return addNewEntryView;
        } else {
            // For other items in the spinner, delegate to the base adapter
            return baseAdapter.getDropDownView(position, convertView, parent);
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        baseAdapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        baseAdapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        // Return the count of items in the base adapter plus one for the "Add new entry" option
        return baseAdapter.getCount() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == getCount() - 1) {
            return null; // "Add new entry" option is not selectable
        } else {
            return baseAdapter.getItem(position);
        }
    }

    @Override
    public long getItemId(int i) {
        return baseAdapter.getItemId(i);
    }

    @Override
    public boolean hasStableIds() {
        return baseAdapter.hasStableIds();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return baseAdapter.getView(i, view, viewGroup);
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return baseAdapter.isEmpty();
    }
}