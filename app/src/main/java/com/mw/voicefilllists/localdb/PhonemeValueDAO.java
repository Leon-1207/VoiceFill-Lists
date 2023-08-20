package com.mw.voicefilllists.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhonemeValueDAO {
    @Query("SELECT * FROM PhonemeValueDatabaseEntry")
    List<PhonemeValueDatabaseEntry> getAll();

    @Query("SELECT * FROM PhonemeValueDatabaseEntry WHERE valueId IN (:valueIds)")
    List<PhonemeValueDatabaseEntry> loadAllByIds(int[] valueIds);

    @Insert
    void insert(PhonemeValueDatabaseEntry valueGroupEntry);

    @Delete
    int delete(PhonemeValueDatabaseEntry valueGroupEntry);

    @Update
    int update(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry);
}
