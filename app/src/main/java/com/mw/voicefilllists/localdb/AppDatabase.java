package com.mw.voicefilllists.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mw.voicefilllists.ValueGroupEntry;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {ValueGroupEntryDatabaseEntry.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ValueGroupEntryDAO valueGroupEntryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "voicefilllist-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }

    public List<ValueGroupEntry> loadAllValueGroupEntries(Context context) {
        List<ValueGroupEntry> result = new ArrayList<>();
        List<ValueGroupEntryDatabaseEntry> entries = getInstance(context)
                .valueGroupEntryDao()
                .getAll();
        for (ValueGroupEntryDatabaseEntry dbEntry : entries) {
            result.add(DataConverter.convert(dbEntry));
        }
        return result;
    }
}
