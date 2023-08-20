package com.mw.voicefilllists.localdb;

import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

public interface ValueGroupDAO {
    @Transaction
    @Query("SELECT * FROM ValueGroupDatabaseEntry WHERE groupId in (:groupIds)")
    public List<ValueGroupWithValuesDatabaseEntry> loadAllByIds(int[] groupIds);
}
