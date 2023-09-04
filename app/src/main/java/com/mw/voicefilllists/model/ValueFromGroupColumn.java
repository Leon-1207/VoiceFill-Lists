package com.mw.voicefilllists.model;

import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

import java.util.List;

public class ValueFromGroupColumn extends DataListColumn {
    public int valueGroupId;

    public List<PhonemeValue> phonemeValues;
}
