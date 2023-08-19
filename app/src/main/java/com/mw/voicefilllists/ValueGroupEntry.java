package com.mw.voicefilllists;

public class ValueGroupEntry {
    private final int uid;
    private String label, phoneticTranscription;

    public ValueGroupEntry(int uid, String label, String phoneticTranscription) {
        this.uid = uid;
        this.label = label;
        this.phoneticTranscription = phoneticTranscription;
    }

    public String getPhoneticTranscription() {
        return phoneticTranscription;
    }

    public void setPhoneticTranscription(String phoneticTranscription) {
        this.phoneticTranscription = phoneticTranscription;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getUid() {
        return uid;
    }
}
