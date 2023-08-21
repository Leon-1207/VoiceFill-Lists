package com.mw.voicefilllists.model;

public class DataListPage {
    private final int id;
    private final String name;
    private final DataListTemplate template;

    public DataListPage(int id, String name, DataListTemplate template) {
        this.id = id;
        this.name = name;
        this.template = template;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DataListTemplate getTemplate() {
        return template;
    }
}
