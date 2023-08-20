package com.mw.voicefilllists.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ValueGroupDAO {
    @Transaction
    @Query("SELECT * FROM ValueGroupDatabaseEntry WHERE groupId in (:groupIds)")
    List<ValueGroupWithValuesDatabaseEntry> loadAllByIds(int[] groupIds);
}
