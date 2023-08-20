package com.mw.voicefilllists.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mw.voicefilllists.PhonemeValue;

import java.util.ArrayList;
import java.util.List;

@Database(
        entities = {
                PhonemeValueDatabaseEntry.class,
                ValueGroupDatabaseEntry.class,
                ValueGroupAndPhonemeValueDatabaseEntry.class
        },
        version = 8)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract PhonemeValueDAO valueGroupEntryDao();

    public abstract ValueGroupDAO valueGroupDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "voicefilllist-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }

    public List<PhonemeValue> loadAllValueGroupEntries(Context context) {
        List<PhonemeValue> result = new ArrayList<>();
        List<PhonemeValueDatabaseEntry> entries = getInstance(context)
                .valueGroupEntryDao()
                .getAll();
        for (PhonemeValueDatabaseEntry dbEntry : entries) {
            result.add(DataConverter.convert(dbEntry));
        }
        return result;
    }
}
