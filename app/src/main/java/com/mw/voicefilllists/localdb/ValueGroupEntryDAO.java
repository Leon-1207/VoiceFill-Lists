package com.mw.voicefilllists.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ValueGroupEntryDAO {
    @Query("SELECT * FROM ValueGroupEntryDatabaseEntry")
    List<ValueGroupEntryDatabaseEntry> getAll();

    @Query("SELECT * FROM ValueGroupEntryDatabaseEntry WHERE uid IN (:userIds)")
    List<ValueGroupEntryDatabaseEntry> loadAllByIds(int[] userIds);

    @Insert
    void insert(ValueGroupEntryDatabaseEntry valueGroupEntry);

    @Delete
    int delete(ValueGroupEntryDatabaseEntry valueGroupEntry);

    @Update
    int update(ValueGroupEntryDatabaseEntry valueGroupEntryDatabaseEntry);
}
