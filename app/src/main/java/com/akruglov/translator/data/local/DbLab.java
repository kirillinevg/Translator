package com.akruglov.translator.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akruglov.translator.data.models.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akruglov on 11.04.17.
 */

public class DbLab implements DbContract {

    private final DbOpenHelper dbHelper;

    DbLab(Context context) {
        dbHelper = new DbOpenHelper(context);
    }

    DbLab(DbOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    List<Language> getLanguages() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String tables = LANGUAGES;
        String orderBy = Languages.DESCRIPTION + " ASC";

        Cursor c = db.query(LANGUAGES,
                null,
                null,
                null,
                null,
                null,
                orderBy,
                null);

        if (c != null && (c.isFirst() || c.moveToFirst())) {
            List<Language> languages = new ArrayList<>();
            do {
                Language language = new Language(
                        c.getInt(c.getColumnIndex(Languages.ID)),
                        c.getString(c.getColumnIndex(Languages.KEY)),
                        c.getString(c.getColumnIndex(Languages.DESCRIPTION))
                );
                languages.add(language);
            } while (c.moveToNext());
            closeCursor(c);
            return languages;
        }
        closeCursor(c);
        return null;
    }

    void setLanguages(List<Language> languages) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Language language : languages) {
            ContentValues values = new ContentValues();
            values.put(Languages.KEY, language.getKey());
            values.put(Languages.DESCRIPTION, language.getDescription());
            language.setId((int)db.insert(LANGUAGES, null, values));
        }
    }

    private static void closeCursor(Cursor c) {
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }
}
