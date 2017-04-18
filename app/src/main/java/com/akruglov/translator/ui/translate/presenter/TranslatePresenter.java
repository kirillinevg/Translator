package com.akruglov.translator.ui.translate.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.translate.view.TranslateView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import timber.log.Timber;

/**
 * Created by akruglov on 06.04.17.
 */

@InjectViewState
public class TranslatePresenter extends MvpPresenter<TranslateView> {

    private TranslatePresenterCache translatePresenterCache;
    private TranslateRepository translateRepository;

    public TranslatePresenter(@NonNull TranslatePresenterCache translatePresenterCache,
                              @NonNull TranslateRepository translateRepository) {
        this.translatePresenterCache = translatePresenterCache;
        this.translateRepository = translateRepository;
        Timber.tag("TranslatePresenter");
    }

    public void init() {
        if (!translatePresenterCache.isCacheExists()) {
            loadLastTranslation();
        } else {
            setTranslateInfoToView(translatePresenterCache.getTranslation());
        }
    }

    private void loadLastTranslation() {
        translateRepository.getLastTranslation(new TranslateDataSource.ResultCallback<Translation>() {

            @Override
            public void onLoaded(Translation result) {
                translatePresenterCache.setTranslation(result);
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

    public void updateSourceText(String sourceText) {
        if (sourceText == null || sourceText.isEmpty()) {
            clearTexts();
        } else {
            translatePresenterCache.updateSourceText(sourceText);
            translate();
        }
    }

    private void translate() {
        if (TextUtils.isEmpty(translatePresenterCache.getSourceText())) {
            return;
        }
        final Translation translation = translatePresenterCache.getTranslation();
        translateRepository.translate(translation, new TranslateDataSource.ResultCallback<Translation>() {
            @Override
            public void onLoaded(Translation result) {
                translatePresenterCache.setTranslation(translation);
                getViewState().showTranslatedText(translation.getTranslatedText());
            }

            @Override
            public void onNotAvailable() {
                Timber.e("Translate failed");
            }
        });
    }

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
        getViewState().showSourceText(null);
    }


    public void chooseSourceLanguage() {
        getViewState().chooseSourceLanguage(translatePresenterCache.getTranslation().getSourceLanguage().getId());
    }

    public void chooseDestinationLanguage() {
        getViewState().chooseDestinationLanguage(translatePresenterCache.getTranslation().getDestinationLanguage().getId());
    }

    public void selectSourceLanguage(int newSourceLanguageId) {
        Language newSourceLanguage = translateRepository.getLanguageById(newSourceLanguageId);
        if (newSourceLanguage != null) {
            translatePresenterCache.setSourceLanguage(newSourceLanguage);
            getViewState().showSourceLanguage(newSourceLanguage.getDescription());
            translate();
        }
    }

    public void selectDestinatonLanguage(int newDestinationLanguageId) {
        Language newDestinationLanguage = translateRepository.getLanguageById(newDestinationLanguageId);
        if (newDestinationLanguage != null) {
            translatePresenterCache.setDestinationLanguage(newDestinationLanguage);
            getViewState().showDestinationLanguage(newDestinationLanguage.getDescription());
            translate();
        }
    }

    /**
     * Show translation from history or favorites
     * @param translation translation to show
     */
    public void showTranslation(Translation translation) {
        translatePresenterCache.setTranslation(translation);
        setTranslateInfoToView(translation);
    }

    public void saveLastTranslation() {
        translateRepository.saveLastTranslation(translatePresenterCache.getTranslation());
    }


}
