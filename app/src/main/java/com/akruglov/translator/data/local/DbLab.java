package com.akruglov.translator.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

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

    public Translation findTranslation(Translation translation) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                HISTORY,
                new String[] { History.ID },
                History.SOURCE_LANG_ID + "= ? AND " +
                History.DEST_LANG_ID + " = ? AND " +
                History.SOURCE_TEXT + " = ?",
                new String[] {
                        Integer.toString(translation.getSourceLanguage().getId()),
                        Integer.toString(translation.getDestinationLanguage().getId()),
                        translation.getSourceText() },
                null,
                null,
                null
        );

        if (c != null && (c.isFirst() || c.moveToFirst())) {
            translation.setId(c.getInt(0));
            closeCursor(c);
            return translation;
        } else {
            closeCursor(c);
            return null;
        }
    }

    public Translation insertTranslation(Translation translation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.SOURCE_LANG_ID, translation.getSourceLanguage().getId());
        values.put(History.DEST_LANG_ID, translation.getDestinationLanguage().getId());
        values.put(History.SOURCE_TEXT, translation.getSourceText());
        values.put(History.TRANSLATED_TEXT, translation.getTranslatedText());
        translation.setId((int)db.insert(HISTORY, null, values));
        return translation;
    }
}
