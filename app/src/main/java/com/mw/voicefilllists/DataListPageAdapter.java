package com.mw.voicefilllists;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mw.voicefilllists.localdb.entities.DataListPageNameAndId;

import java.util.List;

public class DataListPageAdapter extends RecyclerView.Adapter<DataListPageAdapter.ViewHolder> {

    private final List<DataListPageNameAndId> dataList;
    private final Activity activity;

    public DataListPageAdapter(List<DataListPageNameAndId> dataList, Activity activity) {
        this.dataList = dataList;
        this.activity = activity;
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int pageId);
        void onOptionsButtonClick(int pageId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageButton optionsButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            optionsButton = itemView.findViewById(R.id.optionsButton);

            itemView.setOnClickListener(v -> {
                // Switch to main activity with page selected
            });

            optionsButton.setOnClickListener(v -> {
                // TODO open menu
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_data_list_pages_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataListPageNameAndId entry = dataList.get(position);
        Log.e("SS", "" + entry.pageName);
        holder.nameTextView.setText(entry.pageName);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
