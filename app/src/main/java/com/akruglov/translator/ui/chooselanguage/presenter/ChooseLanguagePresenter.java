package com.akruglov.translator.ui.chooselanguage.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.ui.chooselanguage.view.ChooseLanguageView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import timber.log.Timber;

/**
 * Created by akruglov on 12.04.17.
 */

@InjectViewState
public class ChooseLanguagePresenter extends MvpPresenter<ChooseLanguageView> {

    private ChooseLanguagePresenterCache chooseLanguagePresenterCache;
    private TranslateRepository translateRepository;

    public ChooseLanguagePresenter(@NonNull ChooseLanguagePresenterCache chooseLanguagePresenterCache,
                                   @NonNull TranslateRepository translateRepository) {
        this.chooseLanguagePresenterCache = chooseLanguagePresenterCache;
        this.translateRepository = translateRepository;
        Timber.tag("ChooseLanguagePresenter");
    }

    public void init(int currentLanguageId) {
        if (chooseLanguagePresenterCache.isCacheExists()) {
            showLanguages(chooseLanguagePresenterCache.getLanguages());
        } else {
            loadLanguages(currentLanguageId);
        }
    }

    private void loadLanguages(final int currentLanguageId) {
        translateRepository.getLanguages(new TranslateDataSource.ResultCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                chooseLanguagePresenterCache.setLanguages(result);
                chooseLanguagePresenterCache.setCurrentLanguageId(currentLanguageId);
                showLanguages(result);
            }

            @Override
            public void onNotAvailable() {
                Timber.e("GetLanguages failed");
            }
        });
    }

    private void showLanguages(List<Language> languages) {
        Language currentLanguage = chooseLanguagePresenterCache.getCurrentLanguage();
        getViewState().showLanguages(languages);
        if (currentLanguage != null) {
            getViewState().setCurrentLanguage(currentLanguage);
        }
    }

    public void selectNewLanguage(int newLanguageId) {
        if (newLanguageId != chooseLanguagePresenterCache.getCurrentLanguageId()) {
            getViewState().selectNewLanguage(newLanguageId);
        }
    }
}
