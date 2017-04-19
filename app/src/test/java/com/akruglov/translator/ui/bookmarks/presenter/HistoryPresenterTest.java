package com.akruglov.translator.ui.bookmarks.presenter;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateNotificationManager;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.view.HistoryView;
import com.akruglov.translator.ui.bookmarks.view.HistoryView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by akruglov on 19.04.17.
 */

public class HistoryPresenterTest {

    @Mock
    HistoryView historyView;

    @Mock
    HistoryView$$State historyViewState;

    @Mock
    TranslateRepository translateRepository;

    @Mock
    TranslateNotificationManager translateNotificationManager;

    @Captor
    ArgumentCaptor<TranslateDataSource.ResultCallback<List<Translation>>> translationsCallbackCaptor;

    private HistoryPresenter presenter;
    private List<Translation> translations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new HistoryPresenter(translateRepository, translateNotificationManager);
        presenter.attachView(historyView);
        presenter.setViewState(historyViewState);

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
                false));
    }

    @Test
    public void init() {

        presenter.init();

        verify(translateRepository).loadHistory(translationsCallbackCaptor.capture());
        translationsCallbackCaptor.getValue().onLoaded(translations);

        verify(historyViewState, times(1)).showHistory(translations);
        verify(translateNotificationManager, times(1)).addListener(presenter);
    }

    @Test
    public void showTranslationDetails() {
        presenter.showTranslationDetails(translations.get(0));

        verify(historyViewState, times(1)).showTranslationDetails(any(Translation.class));
    }

    @Test
    public void setFavorite() {
        presenter.setFavorite(translations.get(0));

        verify(translateRepository, times(1)).setFavorite(translations.get(0));
    }

    @Test
    public void showClearHistoryNotification() {
        presenter.showClearHistoryNotification();

        verify(historyViewState, times(1)).showClearHistoryNotification();
    }

    @Test
    public void clearTranslations() {
        presenter.clearTranslations();

        verify(translateRepository, times(1)).clearTranslations();
    }

    @Test
    public void onDestroy() {
        presenter.onDestroy();

        verify(translateNotificationManager, times(1)).removeListener(presenter);
    }

    @Test
    public void onInsert() {
        presenter.onInsert(translations.get(0));

        verify(historyViewState, times(1)).insertTranslation(translations.get(0));
    }

    @Test
    public void onRemoveFromFavorites() {
        HashSet<Integer> fakeSet = new HashSet<>();

        presenter.onRemoveFromFavorites(fakeSet);

        verify(historyViewState, times(1)).removeFromFavorites(fakeSet);
    }
}
