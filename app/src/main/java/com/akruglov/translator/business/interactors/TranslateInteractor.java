package com.akruglov.translator.business.interactors;

import com.akruglov.translator.ui.translate.models.Language;

/**
 * Created by akruglov on 06.04.17.
 */

public class TranslateInteractor {

    interface Callback {
        void onTranslated(String translatedText);
    }

    public TranslateInteractor(Callback callback) {

    }

    void translate(String sourceText, Language sourceLanguage, Language destinationLanguage) {

    }
}
