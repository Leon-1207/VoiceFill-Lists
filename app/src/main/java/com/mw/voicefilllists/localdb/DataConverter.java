package com.mw.voicefilllists.localdb;

import com.mw.voicefilllists.model.PhonemeValue;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

public class DataConverter {
    public static PhonemeValue convert(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry) {
        return new PhonemeValue(phonemeValueDatabaseEntry.valueId,
                phonemeValueDatabaseEntry.label,
                phonemeValueDatabaseEntry.phoneticTranscription.split(","));
    }
}
