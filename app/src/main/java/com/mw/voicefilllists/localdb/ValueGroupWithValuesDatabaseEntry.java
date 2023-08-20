package com.mw.voicefilllists.localdb;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;


public class ValueGroupWithValuesDatabaseEntry {
    @Embedded
    public ValueGroupDatabaseEntry valueGroupDatabaseEntry;
    @Relation(
            parentColumn = "groupId",
            entityColumn = "valueId",
            associateBy = @Junction(ValueGroupAndPhonemeValueDatabaseEntry.class)
    )
    public List<PhonemeValueDatabaseEntry> values;
}

