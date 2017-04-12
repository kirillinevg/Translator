package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

/**
 * Created by akruglov on 06.04.17.
 */

public class TranslatePresenterCache {

    private Translation translation;

    public boolean isCacheExists() {
        return translation != null;
    }

    public void setTranslation(@NonNull Translation translation) {
        this.translation = translation;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void updateSourceText(String text) {
        if (translation == null) {
            return;
        }
        translation.setSourceText(text);
    }

    public void updateTranslatedText(String text) {
        if (translation == null) {
            return;
        }
        translation.setTranslatedText(text);
    }

    public void swapLanguages() {
        Language sourceLanguage = translation.getSourceLanguage();
        Language destinationLanguage = translation.getDestinationLanguage();
        translation.setSourceLanguage(destinationLanguage);
        translation.setDestinationLanguage(sourceLanguage);
    }

    public void setSourceLanguage(Language language) {
        translation.setSourceLanguage(language);
    }

    public void setDestinationLanguage(Language language) {
        translation.setDestinationLanguage(language);
    }
}
