package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ValueGroupAndPhonemeValueDAO {
    @Query("SELECT valueId FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = (:groupId)")
    List<Integer> getAllPhonemeValuesIdsOfGroup(int groupId);

    @Query("DELETE FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = :groupId")
    void deleteByGroupId(int groupId);

    @Query("DELETE FROM ValueGroupAndPhonemeValueDatabaseEntry WHERE groupId = :groupId AND valueId = :valueId")
    void deleteByGroupIdAndValueId(int groupId, int valueId);
}
