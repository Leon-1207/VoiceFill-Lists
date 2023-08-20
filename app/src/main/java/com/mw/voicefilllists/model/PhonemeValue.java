package com.mw.voicefilllists.model;

public class PhonemeValue {
    private final int id;
    private String label;
    private String[] phoneticTranscription;

    public PhonemeValue(int id, String label, String[] phoneticTranscription) {
        this.id = id;
        this.label = label;
        this.phoneticTranscription = phoneticTranscription;
    }

    public String[] getPhoneticTranscription() {
        return phoneticTranscription;
    }

    public void setPhoneticTranscription(String[] phoneticTranscription) {
        this.phoneticTranscription = phoneticTranscription;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }
}
