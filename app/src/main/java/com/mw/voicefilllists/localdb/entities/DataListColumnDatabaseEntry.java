package com.mw.voicefilllists.localdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataListColumnDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int columnId;

    @ColumnInfo(name = "template_id")
    public int templateId;
}
