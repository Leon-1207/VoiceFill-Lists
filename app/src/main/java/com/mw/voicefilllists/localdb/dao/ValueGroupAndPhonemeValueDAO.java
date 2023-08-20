package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mw.voicefilllists.localdb.entities.ValueGroupAndPhonemeValueDatabaseEntry;

import java.util.List;

@Dao
public interface ValueGroupAndPhonemeValueDAO {
    @Query("SELECT valueId FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = (:groupId)")
    List<Integer> getAllPhonemeValuesIdsOfGroup(int groupId);

    @Query("DELETE FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = :groupId")
    void deleteByGroupId(int groupId);

    @Query("DELETE FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = :groupId AND valueId = :valueId")
    void deleteByGroupIdAndValueId(int groupId, int valueId);

    @Query("DELETE FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = :groupId AND valueId not in (:valueIds)")
    void deleteIfValueIdIsNotInList(int groupId, int[] valueIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ValueGroupAndPhonemeValueDatabaseEntry valueGroupAndPhonemeValueDatabaseEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ValueGroupAndPhonemeValueDatabaseEntry> entries);
}
