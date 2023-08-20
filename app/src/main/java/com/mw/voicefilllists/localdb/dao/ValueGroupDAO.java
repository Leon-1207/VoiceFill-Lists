package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

import java.util.List;

@Dao
public interface ValueGroupDAO {
    @Query("SELECT * FROM ValueGroupDatabaseEntry")
    List<ValueGroupDatabaseEntry> getAll();

    @Query("SELECT * FROM ValueGroupDatabaseEntry WHERE groupId in (:groupIds)")
    List<ValueGroupDatabaseEntry> getAllByIds(int[] groupIds);

    @Query("SELECT * FROM ValueGroupDatabaseEntry WHERE groupId = (:groupId)")
    ValueGroupDatabaseEntry getById(int groupId);

    @Delete
    int delete(ValueGroupDatabaseEntry valueGroupDatabaseEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ValueGroupDatabaseEntry valueGroupDatabaseEntry);
}
