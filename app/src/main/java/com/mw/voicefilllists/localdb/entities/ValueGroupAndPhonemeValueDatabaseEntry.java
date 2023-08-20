package com.mw.voicefilllists.localdb.entities;


import androidx.room.Entity;

@Entity(primaryKeys = {"groupId", "valueId"})
public class ValueGroupAndPhonemeValueDatabaseEntry {
    public int groupId;
    public int valueId;
}