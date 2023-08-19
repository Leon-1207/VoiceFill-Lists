package com.mw.voicefilllists.localdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ValueGroupEntryDatabaseEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ValueGroupEntryDAO valueGroupEntryDao();
}
