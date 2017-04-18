package com.akruglov.translator.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

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
                new String[] { History.ID, History.TRANSLATED_TEXT, History.IS_FAVORITE },
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
            translation.setTranslatedText(c.getString(1));
            translation.setFavorite(c.getInt(2) == 1);
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
        values.put(History.IS_FAVORITE, 0);
        translation.setId((int)db.insert(HISTORY, null, values));
        translation.setFavorite(false);
        return translation;
    }

    public List<Translation> getTranslations(SparseArray<Language> languages) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String orderBy = History.ID + " DESC";

        Cursor c = db.query(HISTORY,
                null,
                null,
                null,
                null,
                null,
                orderBy,
                null);

        if (c != null && (c.isFirst() || c.moveToFirst())) {
            List<Translation> translations = new ArrayList<>();
            do {
                Translation translation = new Translation(
                        c.getInt(c.getColumnIndex(History.ID)),
                        languages.get(c.getInt(c.getColumnIndex(History.SOURCE_LANG_ID))),
                        languages.get(c.getInt(c.getColumnIndex(History.DEST_LANG_ID))),
                        c.getString(c.getColumnIndex(History.SOURCE_TEXT)),
                        c.getString(c.getColumnIndex(History.TRANSLATED_TEXT)),
                        c.getInt(c.getColumnIndex(History.IS_FAVORITE)) == 1
                );
                translations.add(translation);
            } while (c.moveToNext());
            closeCursor(c);
            return translations;
        }
        closeCursor(c);
        return null;
    }

    public void setFavorite(Translation translation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(History.IS_FAVORITE, translation.isFavorite());
        db.update(HISTORY,
                  values,
                  History.ID + "=?",
                  new String[] { String.valueOf(translation.getId()) });
    }
}
