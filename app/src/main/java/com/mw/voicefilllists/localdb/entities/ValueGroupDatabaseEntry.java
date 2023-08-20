package com.mw.voicefilllists.localdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ValueGroupDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int groupId;

    @ColumnInfo(name = "name")
    public String name;
}
