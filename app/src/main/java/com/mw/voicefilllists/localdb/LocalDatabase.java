package com.mw.voicefilllists.localdb;

import android.content.Context;

import androidx.room.Room;

import com.mw.voicefilllists.ValueGroupEntry;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabase {

    private static final String DATABASE_NAME = "voicefilllists_database";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public List<ValueGroupEntry> loadAllValueGroupEntries() {
        List<ValueGroupEntry> result = new ArrayList<>();
        List<ValueGroupEntryDatabaseEntry> entries = instance.valueGroupEntryDao().getAll();
        for (ValueGroupEntryDatabaseEntry dbEntry : entries) {
            result.add(DataConverter.convert(dbEntry));
        }
        return result;
    }
}
