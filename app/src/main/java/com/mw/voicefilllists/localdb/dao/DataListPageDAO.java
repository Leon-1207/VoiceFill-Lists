package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mw.voicefilllists.localdb.entities.DataListPageDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.DataListPageNameAndId;

import java.util.List;

@Dao
public abstract class DataListPageDAO {
    @Query("SELECT * FROM data_list_page WHERE pageId = :pageId")
    abstract List<DataListPageDatabaseEntry> getById(int pageId);

    @Query("SELECT pageId, pageName FROM data_list_page")
    public abstract List<DataListPageNameAndId> getIdNamePairs();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(DataListPageDatabaseEntry databaseEntry);

    @Transaction
    @Update
    abstract void update(DataListPageDatabaseEntry databaseEntry);

    @Transaction
    @Delete
    abstract void delete(DataListPageDatabaseEntry databaseEntry);
}
