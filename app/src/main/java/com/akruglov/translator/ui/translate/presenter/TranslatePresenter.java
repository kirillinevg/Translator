package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.translate.view.ITranslateView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import timber.log.Timber;

/**
 * Created by akruglov on 06.04.17.
 */

@InjectViewState
public class TranslatePresenter extends MvpPresenter<ITranslateView> implements ITranslatePresenter {

    private TranslatePresenterCache translatePresenterCache;
    private TranslateRepository translateRepository;

    public TranslatePresenter(@NonNull TranslatePresenterCache translatePresenterCache,
                              @NonNull TranslateRepository translateRepository) {
        this.translatePresenterCache = translatePresenterCache;
        this.translateRepository = translateRepository;
        Timber.tag("TranslatePresenter");
    }

    public void init() {
        if (translatePresenterCache.isCacheExists()) {
            setTranslateInfoToView(translatePresenterCache.getTranslation());
        } else {
            loadLastTranslation();
        };
    }

    private void loadLastTranslation() {
        translateRepository.getLastTranslation(new TranslateDataSource.ResultCallback<Translation>() {

            @Override
            public void onLoaded(Translation result) {
                translatePresenterCache.updateData(result);
                setTranslateInfoToView(result);
            }

            @Override
            public void onNotAvailable() {
                Timber.e("LoadLastTranslation failed");
            }

        });
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
            translate();
        }
    }

    private void translate() {
        final Translation translation = translatePresenterCache.getTranslation();
        translateRepository.translate(translation, new TranslateDataSource.ResultCallback<Translation>() {
            @Override
            public void onLoaded(Translation result) {
                translatePresenterCache.updateData(translation);
                getViewState().showTranslatedText(translation.getTranslatedText());
            }

            @Override
            public void onNotAvailable() {
                Timber.e("Translate failed");
            }
        });
    }

    @Override
    public void swapLanguages() {
        translatePresenterCache.swapLanguages();
        translate();
        String sourceLanguage = translatePresenterCache.getTranslation().getSourceLanguage().getDescription();
        String destinationLanguage = translatePresenterCache.getTranslation().getDestinationLanguage().getDescription();
        getViewState().showSourceLanguage(sourceLanguage);
        getViewState().showDestinationLanguage(destinationLanguage);
    }

    private void clearTexts() {
        translatePresenterCache.updateSourceText(null);
        translatePresenterCache.updateTranslatedText(null);
        getViewState().showTranslatedText(null);
    }


}
