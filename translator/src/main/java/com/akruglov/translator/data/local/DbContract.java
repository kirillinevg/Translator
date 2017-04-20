package com.akruglov.translator.data.local;

/**
 * Created by akruglov on 09.04.17.
 */

public interface DbContract {
    String DB_NAME = "translate.sqlite";

    String LANGUAGES = "languages";
    interface Languages {
        String ID = "language_id";
        String KEY = "language_key";
        String DESCRIPTION = "language_description";
    }

    String HISTORY = "history";
    interface History {
        String ID = "row_id";
        String SOURCE_LANG_ID = "history_source_lang_id";
        String DEST_LANG_ID = "histrory_dest_lang_id";
        String SOURCE_TEXT = "history_source_text";
        String TRANSLATED_TEXT = "history_translated_text";
        String IS_FAVORITE = "history_is_favorite";
    }
}
