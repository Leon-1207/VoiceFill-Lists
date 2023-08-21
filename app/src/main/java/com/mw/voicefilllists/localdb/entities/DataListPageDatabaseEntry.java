package com.mw.voicefilllists.localdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataListPageDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int pageId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "template_id")
    public int templateId;
}
