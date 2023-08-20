package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;

import java.util.List;

@Dao
public interface PhonemeValueDAO {
    @Query("SELECT * FROM PhonemeValueDatabaseEntry")
    List<PhonemeValueDatabaseEntry> getAll();

    @Query("SELECT * FROM PhonemeValueDatabaseEntry WHERE valueId IN (:valueIds)")
    List<PhonemeValueDatabaseEntry> loadAllByIds(List<Integer> valueIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PhonemeValueDatabaseEntry valueGroupEntry);

    @Delete
    int delete(PhonemeValueDatabaseEntry valueGroupEntry);

    @Update
    int update(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry);
}
