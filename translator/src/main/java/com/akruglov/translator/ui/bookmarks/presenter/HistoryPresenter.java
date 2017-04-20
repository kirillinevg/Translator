package com.akruglov.translator.ui.bookmarks.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateNotificationManager;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.view.HistoryView;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenter;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import timber.log.Timber;

/**
 * Created by akruglov on 13.04.17.
 */

@InjectViewState
public class HistoryPresenter extends MvpPresenter<HistoryView> implements TranslateNotificationManager.TranslationListener {

    private List<Translation> translations;

    private TranslateRepository translateRepository;
    private TranslateNotificationManager notificationManager;

    public HistoryPresenter(@NonNull TranslateRepository translateRepository,
                            @NonNull TranslateNotificationManager notificationManager) {
        this.translateRepository = translateRepository;
        this.notificationManager = notificationManager;
        Timber.tag("HistoryPresenter");
    }

    public void init() {
        // After configuration changes view will be restored from ViewState,
        // so we must care only about first initialization
        if (/*languages == null && */translations == null) {
            loadHistory();
            notificationManager.addListener(this);
        }
    }

    private void loadHistory() {

        translateRepository.loadHistory(new TranslateDataSource.ResultCallback<List<Translation>>() {

            @Override
            public void onLoaded(List<Translation> result) {
                translations = result;
                getViewState().showHistory(translations);
            }

            @Override
            public void onNotAvailable() {
                Timber.e("LoadHistory failed");
            }
        });
    }

    @Override
    public void onInsert(Translation translation) {
        getViewState().insertTranslation(translation);
    }

    @Override
    public void onRemoveFromFavorites(HashSet<Integer> removedFromFavorites) {
        getViewState().removeFromFavorites(removedFromFavorites);
    }

    @Override
    public void onDestroy() {
        notificationManager.removeListener(this);
        super.onDestroy();
    }

    public void showTranslationDetails(Translation translation) {
        Translation copy = new Translation(translation);
        getViewState().showTranslationDetails(copy);
    }

    public void setFavorite(Translation translation) {
        translateRepository.setFavorite(translation);
    }

    public void showClearHistoryNotification() {
        getViewState().showClearHistoryNotification();
    }

    public void clearTranslations() {
        translateRepository.clearTranslations();
    }
}
