package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.akruglov.translator.ui.translate.models.Language;
import com.akruglov.translator.ui.translate.models.TranslateDataModel;
import com.akruglov.translator.ui.translate.view.ITranslateView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

/**
 * Created by akruglov on 06.04.17.
 */

@InjectViewState
public class TranslatePresenter extends MvpPresenter<ITranslateView> implements ITranslatePresenter {

    private TranslatePresenterCache translatePresenterCache;

    public TranslatePresenter(@NonNull TranslatePresenterCache translatePresenterCache) {
        this.translatePresenterCache = translatePresenterCache;
    }

    public void setTranslatePresenterCache(@NonNull TranslatePresenterCache translatePresenterCache) {
        this.translatePresenterCache = translatePresenterCache;
    }

    public void init() {
        if (translatePresenterCache.isCacheExists()) {
            setTranslateInfoToView(translatePresenterCache.getTranslateDataModel());
        } else {
            loadTranslateInfoFromData();
        };
    }

    private void loadTranslateInfoFromData() {
        // TODO: load from interactor
        TranslateDataModel data = new TranslateDataModel(new Language(0, "fr", "Французский"),
                new Language(0, "ru", "Русский"), null, null);
        translatePresenterCache.updateData(data);
        setTranslateInfoToView(data);
    }

    private void setTranslateInfoToView(@NonNull TranslateDataModel translateDataModel) {
        getViewState().showSourceLanguage(translateDataModel.getSourceLanguage().getDescription());
        getViewState().showDestinationLanguage(translateDataModel.getDestinationLanguage().getDescription());
        getViewState().showSourceText(translateDataModel.getSourceText());
        getViewState().showTranslatedText(translateDataModel.getTranslatedText());
    }

    @Override
    public void updateSourceText(String sourceText) {
        if (TextUtils.isEmpty(sourceText)) {
            clearTexts();
        } else {
            translatePresenterCache.updateSourceText(sourceText);
            translatePresenterCache.updateTranslatedText(sourceText);
            getViewState().showTranslatedText(sourceText);
        }
    }

    private void clearTexts() {
        translatePresenterCache.updateSourceText(null);
        translatePresenterCache.updateTranslatedText(null);
        getViewState().showTranslatedText(null);
    }
}
