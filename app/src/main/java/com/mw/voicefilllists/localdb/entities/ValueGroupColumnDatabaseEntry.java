package com.mw.voicefilllists.localdb.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ValueGroupColumnDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int groupId;

    @Embedded
    public DataListColumnDatabaseEntry columnDatabaseEntry;
}
