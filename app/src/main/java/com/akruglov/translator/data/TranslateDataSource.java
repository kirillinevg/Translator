package com.akruglov.translator.data;

import com.akruglov.translator.data.models.Language;

/**
 * Created by akruglov on 09.04.17.
 */

public interface TranslateDataSource {

    interface GetLanguageCallback {
        void onLoadLanguage(Language language);
    }
}
