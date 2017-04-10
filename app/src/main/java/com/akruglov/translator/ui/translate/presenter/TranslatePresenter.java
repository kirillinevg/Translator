package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
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

    public void init() {
        if (translatePresenterCache.isCacheExists()) {
            setTranslateInfoToView(translatePresenterCache.getTranslation());
        } else {
            loadTranslateInfoFromData();
        };
    }

    private void loadTranslateInfoFromData() {
        // TODO: load from interactor
        Translation data = new Translation(new Language(0, "fr", "Французский"),
                new Language(0, "ru", "Русский"), null, null);
        translatePresenterCache.updateData(data);
        setTranslateInfoToView(data);
    }

    private void setTranslateInfoToView(@NonNull Translation translation) {
        getViewState().showSourceLanguage(translation.getSourceLanguage().getDescription());
        getViewState().showDestinationLanguage(translation.getDestinationLanguage().getDescription());
        getViewState().showSourceText(translation.getSourceText());
        getViewState().showTranslatedText(translation.getTranslatedText());
    }

    @Override
    public void updateSourceText(String sourceText) {
        if (sourceText == null || sourceText.isEmpty()) {
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
