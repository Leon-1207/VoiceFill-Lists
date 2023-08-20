package com.mw.voicefilllists.localdb;

import com.mw.voicefilllists.PhonemeValue;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;

public class DataConverter {
    public static PhonemeValue convert(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry) {
        return new PhonemeValue(phonemeValueDatabaseEntry.valueId,
                phonemeValueDatabaseEntry.label,
                phonemeValueDatabaseEntry.phoneticTranscription.split(","));
    }
}
