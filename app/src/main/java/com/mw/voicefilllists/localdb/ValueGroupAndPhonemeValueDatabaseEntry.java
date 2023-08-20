package com.mw.voicefilllists.localdb;


import androidx.room.Entity;

@Entity(primaryKeys = {"groupId", "valueId"})
public class ValueGroupAndPhonemeValueDatabaseEntry {
    public long groupId;
    public long valueId;
}