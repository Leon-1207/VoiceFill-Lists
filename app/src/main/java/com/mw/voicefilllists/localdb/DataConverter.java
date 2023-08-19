package com.mw.voicefilllists.localdb;

import com.mw.voicefilllists.ValueGroupEntry;

public class DataConverter {
    public static ValueGroupEntry convert(ValueGroupEntryDatabaseEntry valueGroupEntryDatabaseEntry) {
        return new ValueGroupEntry(valueGroupEntryDatabaseEntry.uid,
                valueGroupEntryDatabaseEntry.label,
                valueGroupEntryDatabaseEntry.phoneticTranscription.split(","));
    }
}
