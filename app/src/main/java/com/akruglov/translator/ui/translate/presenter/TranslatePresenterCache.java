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

    public void updateData(@NonNull Translation translation) {
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
}
