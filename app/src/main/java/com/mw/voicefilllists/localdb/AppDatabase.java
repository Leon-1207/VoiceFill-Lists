package com.mw.voicefilllists.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mw.voicefilllists.localdb.dao.DataListTemplateDAO;
import com.mw.voicefilllists.localdb.entities.DataListColumnDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.DataListTemplateDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupColumnDatabaseEntry;
import com.mw.voicefilllists.model.DataListPage;
import com.mw.voicefilllists.model.DataListTemplate;
import com.mw.voicefilllists.model.PhonemeValue;
import com.mw.voicefilllists.localdb.dao.PhonemeValueDAO;
import com.mw.voicefilllists.localdb.dao.ValueGroupAndPhonemeValueDAO;
import com.mw.voicefilllists.localdb.dao.ValueGroupDAO;
import com.mw.voicefilllists.localdb.entities.PhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupAndPhonemeValueDatabaseEntry;
import com.mw.voicefilllists.localdb.entities.ValueGroupDatabaseEntry;

import java.util.ArrayList;
import java.util.List;

@Database(
        entities = {
                PhonemeValueDatabaseEntry.class,
                ValueGroupDatabaseEntry.class,
                ValueGroupAndPhonemeValueDatabaseEntry.class,
                DataListTemplateDatabaseEntry.class,
                ValueGroupColumnDatabaseEntry.class
        },
        version = 11)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    // Data Access Objects (DAO)

    public abstract PhonemeValueDAO phonemeValueDao();

    public abstract ValueGroupAndPhonemeValueDAO valueGroupAndPhonemeValueDAO();

    public abstract ValueGroupDAO valueGroupDAO();

    public abstract DataListTemplateDAO dataListTemplateDAO();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "voicefilllist-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }


    public List<PhonemeValue> loadAllValueGroupEntries(Context context) {
        List<PhonemeValue> result = new ArrayList<>();
        List<PhonemeValueDatabaseEntry> entries = getInstance(context)
                .phonemeValueDao()
                .getAll();
        for (PhonemeValueDatabaseEntry dbEntry : entries) {
            result.add(DataConverter.convert(dbEntry));
        }
        return result;
    }

    public void saveDataListTemplate(Context context, DataListTemplate dataListTemplate) throws Exception {
        DataListTemplateDAO dao = AppDatabase.getInstance(context).dataListTemplateDAO();
        System.out.println(dataListTemplate.hasTemplateId());
        if (dataListTemplate.hasTemplateId()) {
            // is already in database --> UPDATE
            dao.updateTemplateWithColumns(dataListTemplate);
        } else {
            // no ID --> is not in database --> INSERT
            dao.insertTemplateWithColumns(dataListTemplate);
        }
    }
}
