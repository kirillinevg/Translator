package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.ui.translate.models.Language;
import com.akruglov.translator.ui.translate.models.TranslateDataModel;

/**
 * Created by akruglov on 06.04.17.
 */

public class TranslatePresenterCache {

    private TranslateDataModel translateDataModel;

    public boolean isCacheExists() {
        return translateDataModel != null;
    }

    public void updateData(@NonNull TranslateDataModel translateDataModel) {
        this.translateDataModel = translateDataModel;
    }

    public TranslateDataModel getTranslateDataModel() {
        return translateDataModel;
    }

    public void updateSourceText(String text) {
        translateDataModel.setSourceText(text);
    }

    public void updateTranslatedText(String text) {
        translateDataModel.setTranslatedText(text);
    }
}
