package com.mw.voicefilllists.localdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataListTemplateDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int templateId;

    @ColumnInfo(name = "name")
    public String name;
}
