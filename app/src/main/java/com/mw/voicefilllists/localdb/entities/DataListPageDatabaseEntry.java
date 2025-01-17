package com.mw.voicefilllists.localdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_list_page")
public class DataListPageDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int pageId;

    public String pageName;
    
    public int templateId;

    public String data;
}
