package com.mw.voicefilllists.localdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mw.voicefilllists.localdb.DataConverter;
import com.mw.voicefilllists.localdb.entities.DataListColumnDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupColumnDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;
import com.mw.voicefilllists.model.DataListColumn;
import com.mw.voicefilllists.model.DataListTemplate;
import com.mw.voicefilllists.model.ValueFromGroupColumn;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class DataListTemplateDAO {
    @Transaction
    @Query("SELECT * FROM DataListTemplateDatabaseEntry WHERE templateId = (:templateId)")
    public abstract DataListTemplateDatabaseEntry getById(int templateId);

    @Query("SELECT * FROM DataListTemplateDatabaseEntry")
    public abstract List<DataListTemplateDatabaseEntry> getAll();

    @Transaction
    @Query("DELETE FROM ValueGroupColumnDatabaseEntry WHERE template_id = :templateId")
    public abstract void deleteValueGroupColumnDatabaseEntriesForTemplateId(int templateId);

    @Transaction
    @Query("DELETE FROM DataListTemplateDatabaseEntry WHERE templateId = :templateId")
    public abstract void deleteTemplateById(int templateId);

    @Transaction
    void deleteColumnsOfTemplate(int templateId) {
        // delete all kinds of columns

        // value group columns
        deleteValueGroupColumnDatabaseEntriesForTemplateId(templateId);

        // other types (numbers, data...)
        // TODO
    }

    @Transaction
    void deleteDataListAndAssociatedValues(int templateId) {
        // Perform multiple queries within a single transaction
        deleteColumnsOfTemplate(templateId);
        deleteTemplateById(templateId);
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(DataListTemplateDatabaseEntry dataListTemplateDatabaseEntry);

    @Transaction
    public long insertTemplateWithColumns(DataListTemplate dataListTemplate) throws Exception {
        DataListTemplateDatabaseEntry databaseEntry = new DataListTemplateDatabaseEntry();
        databaseEntry.name = dataListTemplate.name;
        long templateId = insert(databaseEntry);

        addColumnsForTemplate(dataListTemplate.columns, (int) templateId);

        return templateId;
    }

    @Transaction
    @Update
    public abstract int update(DataListTemplateDatabaseEntry dataListTemplateDatabaseEntry);

    @Transaction
    public long updateTemplateWithColumns(DataListTemplate dataListTemplate) throws Exception {
        int templateId = dataListTemplate.getTemplateId();

        DataListTemplateDatabaseEntry databaseEntry = getById(dataListTemplate.getTemplateId());
        databaseEntry.name = dataListTemplate.name;

        // delete current columns
        deleteColumnsOfTemplate(templateId);

        // add new columns
        addColumnsForTemplate(dataListTemplate.columns, templateId);

        update(databaseEntry);

        return templateId;
    }

    @Transaction
    public void addColumnsForTemplate(List<DataListColumn> columns, int templateId) throws Exception {
        for (DataListColumn column : columns) {
            if (column instanceof ValueFromGroupColumn) {
                insert(DataConverter.convert((ValueFromGroupColumn) column, templateId));
            } else {
                throw new Exception("DAO does not support this column class yet");
            }
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(ValueGroupColumnDatabaseEntry entry);

    @Transaction
    @Query("SELECT * FROM ValueGroupColumnDatabaseEntry WHERE template_id = :templateId")
    abstract List<ValueGroupColumnDatabaseEntry> getValueGroupColumnDatabaseEntries(int templateId);

    @Transaction
    public List<DataListColumn> loadColumnsForTemplateId(int templateId) {
        List<DataListColumn> columns = new ArrayList<>();

        // add columns of type ValueFromGroupColumn
        List<ValueGroupColumnDatabaseEntry> columnDatabaseEntries = getValueGroupColumnDatabaseEntries(templateId);
        for (ValueGroupColumnDatabaseEntry dbEntry : columnDatabaseEntries) {
            ValueFromGroupColumn item = new ValueFromGroupColumn();
            item.columnName = dbEntry.columnDatabaseEntry.columnName;
            item.valueGroupId = dbEntry.groupId;
            columns.add(item);
        }

        // add other column types
        // TODO

        return columns;
    }

    @Transaction
    public DataListTemplate loadDataListTemplate(int templateId) {
        DataListTemplate result = new DataListTemplate();
        DataListTemplateDatabaseEntry templateDbEntry = getById(templateId);

        // set name and id
        result.setTemplateId(templateId);
        result.name = templateDbEntry.name;

        // set columns
        result.columns = loadColumnsForTemplateId(templateId);

        return result;
    }
}
