package com.mw.voicefilllists.localdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PhonemeValueDatabaseEntry {
    @PrimaryKey(autoGenerate = true)
    public int valueId;

    @ColumnInfo(name = "label")
    public String label;

    @ColumnInfo(name = "phonetic_transcription")
    public String phoneticTranscription;
}
