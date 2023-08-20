package com.mw.voicefilllists.localdb;

import com.mw.voicefilllists.PhonemeValue;

public class DataConverter {
    public static PhonemeValue convert(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry) {
        return new PhonemeValue(phonemeValueDatabaseEntry.valueId,
                phonemeValueDatabaseEntry.label,
                phonemeValueDatabaseEntry.phoneticTranscription.split(","));
    }
}
