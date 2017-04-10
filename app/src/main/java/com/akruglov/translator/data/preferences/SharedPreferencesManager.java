package com.akruglov.translator.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by akruglov on 09.04.17.
 */

public final class SharedPreferencesManager {

    private  static final String MY_APP_PREFERENCES = "dbe87957-42e9-4bb3-b3ca-8622c1957940";

    private static final String PREF_SOURCE_TEXT = "source.text";
    private static final String PREF_TRANSLATED_TEXT = "translated.text";

    private static final String PREF_SOURCE_LANG_ID = "source.lang.id";
    private static final String PREF_SOURCE_LANG_KEY = "source.lang.key";
    private static final String PREF_SOURCE_LANG_DESCRIPTION = "source.lang.description";

    private static final String PREF_DEST_LANG_ID = "dest.lang.id";
    private static final String PREF_DEST_LANG_KEY = "dest.lang.key";
    private static final String PREF_DEST_LANG_DESCRIPTION = "dest.lang.description";


    private SharedPreferences sharedPreferences;

    private static SharedPreferencesManager INSTANCE;

    private SharedPreferencesManager(Context context){
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                MY_APP_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context){
        if(INSTANCE == null)
            INSTANCE = new SharedPreferencesManager(context);

        return INSTANCE;
    }

    public boolean isTranslateInfoExists() {
        return sharedPreferences.contains(PREF_SOURCE_LANG_ID);
    }

    private int getSourceLanguageId(){
        return sharedPreferences.getInt(PREF_SOURCE_LANG_ID, 0);
    }

    private void setSourceLanguageId(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SOURCE_LANG_ID, value);
        editor.apply();
    }
}
