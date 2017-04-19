package com.akruglov.translator.ui.bookmarks.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.view.FavoritesView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by akruglov on 18.04.17.
 */

@InjectViewState
public class FavoritesPresenter extends MvpPresenter<FavoritesView> {

    private List<Translation> favorites;

    private TranslateRepository translateRepository;

    public FavoritesPresenter(@NonNull TranslateRepository translateRepository) {
        this.translateRepository = translateRepository;
        Timber.tag("FavoritesPresenter");
    }

    public void init() {
        loadFavorites();
    }

    private void loadFavorites() {

        translateRepository.loadFavorites(new TranslateDataSource.ResultCallback<List<Translation>>() {

            @Override
            public void onLoaded(List<Translation> result) {
                favorites = result;
                getViewState().showFavorites(favorites);
            }

            @Override
            public void onNotAvailable() {
                Timber.e("LoadFavorites failed");
                getViewState().showFavorites(new ArrayList<Translation>());
            }
        });
    }

    public void showTranslationDetails(Translation translation) {
        Translation copy = new Translation(translation);
        getViewState().showTranslationDetails(copy);
    }

    public void setFavorite(Translation translation) {
        translateRepository.setFavorite(translation);
    }

    public void showClearFavoritesNotification() {
        getViewState().showClearFavoritesNotification();
    }

    public void clearFavorites() {
        getViewState().clearFavorites();
        translateRepository.removeFromFavorites(favorites);
    }
}
