package com.akruglov.translator.ui.bookmarks.presenter;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.view.FavoritesView;
import com.akruglov.translator.ui.bookmarks.view.FavoritesView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by akruglov on 19.04.17.
 */

public class FavoritesPresenterTest {

    @Mock
    FavoritesView favoritesView;

    @Mock
    FavoritesView$$State favoritesViewState;

    @Mock
    TranslateRepository translateRepository;

    @Captor
    ArgumentCaptor<TranslateDataSource.ResultCallback<List<Translation>>> translationsCallbackCaptor;

    @Captor
    ArgumentCaptor<List<Translation>> translationsCaptor;

    private FavoritesPresenter presenter;
    private List<Translation> translations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new FavoritesPresenter(translateRepository);
        presenter.attachView(favoritesView);
        presenter.setViewState(favoritesViewState);

        translations = new ArrayList<>();
        translations.add(new Translation(0,
                new Language(0, "ru", "Русский"),
                new Language(1, "en", "Английский"),
                "папа",
                "father",
                true));
        translations.add(new Translation(1,
                new Language(0, "ru", "Русский"),
                new Language(1, "en", "Английский"),
                "цветок",
                "flour",
                true));
    }

    @Test
    public void init_favoritesLoaded() {
        presenter.init();

        verify(translateRepository).loadFavorites(translationsCallbackCaptor.capture());
        translationsCallbackCaptor.getValue().onLoaded(translations);

        verify(favoritesViewState, times(1)).showFavorites(translations);
    }

    @Test
    public void init_favoritesNotAvailable() {
        presenter.init();

        verify(translateRepository).loadFavorites(translationsCallbackCaptor.capture());
        translationsCallbackCaptor.getValue().onNotAvailable();

        verify(favoritesViewState, times(1)).showFavorites(translationsCaptor.capture());
        assertEquals(translationsCaptor.getValue().size(), 0);
    }

    @Test
    public void showTranslationDetails() {
        presenter.showTranslationDetails(translations.get(0));

        verify(favoritesViewState, times(1)).showTranslationDetails(any(Translation.class));
    }

    @Test
    public void setFavorite() {
        presenter.setFavorite(translations.get(0));

        verify(translateRepository, times(1)).setFavorite(translations.get(0));
    }

    @Test
    public void showClearFavoritesNotification() {
        presenter.showClearFavoritesNotification();

        verify(favoritesViewState, times(1)).showClearFavoritesNotification();
    }

    @Test
    public void clearFavorites() {

        presenter.setFavorites(translations);

        presenter.clearFavorites();

        verify(translateRepository, times(1)).removeFromFavorites(translations);
    }
}
