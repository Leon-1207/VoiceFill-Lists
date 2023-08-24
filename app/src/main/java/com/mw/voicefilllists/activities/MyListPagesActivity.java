package com.mw.voicefilllists.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mw.voicefilllists.DataListPageAdapter;
import com.mw.voicefilllists.LoadingScreen;
import com.mw.voicefilllists.R;
import com.mw.voicefilllists.localdb.AppDatabase;
import com.mw.voicefilllists.localdb.entities.DataListPageNameAndId;

import java.util.ArrayList;
import java.util.List;

public class MyListPagesActivity extends AbstractViewAndEditDataActivity {
    private RecyclerView recyclerView;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loadingScreen = new LoadingScreen(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadValueList() {
        loadingScreen.show();
        getValueListView().removeAllViews();
        if (recyclerView == null) recyclerView = new RecyclerView(getValueListView().getContext());
        MyListPagesActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // load data
                // TODO
                // AppDatabase.getInstance(activity).dataListPageDAO()
                List<DataListPageNameAndId> dataList = new ArrayList<>();

                // insert data into UI
                runOnUiThread(() -> {
                    DataListPageAdapter adapter = new DataListPageAdapter(dataList, activity);
                    recyclerView.setAdapter(adapter);
                    loadingScreen.dismiss();
                });
            }
        }).start();
    }

    @Override
    protected void setupToolbar() {
        ActivityToolbarHelper.setupToolbar(this, R.string.my_list_pages);
    }

    @Override
    protected void switchToCreateEntryActivity() {
        Intent intent = new Intent(MyListPagesActivity.this.getApplicationContext(), CreateDataListPageActivity.class);
        MyListPagesActivity.this.startActivity(intent);
    }

    @Override
    protected int getNewEntryButtonStringResource() {
        return R.string.new_list_page;
    }
}
