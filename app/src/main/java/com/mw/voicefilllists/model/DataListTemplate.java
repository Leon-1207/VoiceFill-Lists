package com.mw.voicefilllists.model;

import java.util.List;

public class DataListTemplate {
    private int templateId;
    public String name;
    public List<DataListColumn> columns;
    private boolean hasTemplateId;

    public DataListTemplate() {
        this.hasTemplateId = false;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.hasTemplateId = true;
        this.templateId = templateId;
    }

    public boolean hasTemplateId() {
        return hasTemplateId;
    }
}
