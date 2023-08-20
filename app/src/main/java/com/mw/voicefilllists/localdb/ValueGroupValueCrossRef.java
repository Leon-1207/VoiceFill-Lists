package com.mw.voicefilllists.localdb;


import androidx.room.Entity;

@Entity(primaryKeys = {"groupId", "valueId"})
public class ValueGroupValueCrossRef {
    public long groupId;
    public long valueId;
}