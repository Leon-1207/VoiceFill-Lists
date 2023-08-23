package com.mw.voicefilllists.localdb;

import com.mw.voicefilllists.localdb.entities.DataListColumnDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupColumnDatabaseEntry;
import com.mw.voicefilllists.model.DataListTemplate;
import com.mw.voicefilllists.model.PhonemeValue;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

public class DataConverter {
    public static PhonemeValue convert(PhonemeValueDatabaseEntry phonemeValueDatabaseEntry) {
        return new PhonemeValue(phonemeValueDatabaseEntry.valueId,
                phonemeValueDatabaseEntry.label,
                phonemeValueDatabaseEntry.phoneticTranscription.split(","));
    }

    public static ValueGroupColumnDatabaseEntry convert(ValueFromGroupColumn column, int templateId) {
        ValueGroupColumnDatabaseEntry result = new ValueGroupColumnDatabaseEntry();

        result.columnDatabaseEntry = new DataListColumnDatabaseEntry();
        result.columnDatabaseEntry.columnName = column.columnName;
        result.columnDatabaseEntry.templateId = templateId;

        result.groupId = column.valueGroupId;

        return result;
    }
}
