package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.translate.view.TranslateView;
import com.akruglov.translator.ui.translate.view.TranslateView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by akruglov on 06.04.17.
 */

public final class TranslatePresenterTest {

    @Mock
    TranslateView translateView;

    @Mock
    TranslateView$$State translateViewState;

    @Mock
    TranslatePresenterCache translatePresenterCache;

    @Mock
    TranslateRepository translateRepository;

    @Captor
    private ArgumentCaptor<TranslateDataSource.ResultCallback<Translation>> translationCallbackCaptor;

    @Captor
    private ArgumentCaptor<Translation> translationCaptor;

    private TranslatePresenter presenter;
    private Translation translation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new TranslatePresenter(translatePresenterCache, translateRepository);
        presenter.attachView(translateView);
        presenter.setViewState(translateViewState);

        translation = new Translation(-1, new Language(0, "ru", "Русский"),
                new Language(1, "it", "Итальянский"), "Ребенок", "Bambino", false);
    }

    @Test
    public void init_notNullCache() {
        when(translatePresenterCache.isCacheExists()).thenReturn(true);
        when(translatePresenterCache.getTranslation()).thenReturn(translation);

        presenter.init();

        verify(translateViewState, times(1)).showSourceLanguage("Русский");
        verify(translateViewState, times(1)).showDestinationLanguage("Итальянский");
        verify(translateViewState, times(1)).showSourceText("Ребенок");
        verify(translateViewState, times(1)).showTranslatedText("Bambino");
    }

    @Test
    public void init_nullCache() {
        when(translatePresenterCache.isCacheExists()).thenReturn(false);

        presenter.init();

        verify(translateRepository).getLastTranslation(translationCallbackCaptor.capture());
        translationCallbackCaptor.getValue().onLoaded(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);

        verify(translateViewState, times(1)).showSourceLanguage(translation.getSourceLanguage().getDescription());
        verify(translateViewState, times(1)).showDestinationLanguage(translation.getDestinationLanguage().getDescription());
        verify(translateViewState, times(1)).showSourceText(translation.getSourceText());
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
    }

    @Test
    public void updateSourceText_notNull_notEquals_translateSuccess() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);
        when(translatePresenterCache.getSourceText()).thenReturn(translation.getSourceText());

        presenter.updateSourceText("папа");

        verify(translatePresenterCache, times(1)).updateSourceText("папа");
        verify(translateRepository).translate(translationCaptor.capture(), translationCallbackCaptor.capture());
        assertEquals(translationCaptor.getValue(), translation);
        translationCallbackCaptor.getValue().onLoaded(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
    }

    @Test
    public void updateSourceText_isNull() {
        presenter.updateSourceText(null);

        verify(translatePresenterCache, times(1)).updateSourceText(null);
        verify(translatePresenterCache, times(1)).updateTranslatedText(null);

        verify(translateViewState, times(1)).showTranslatedText(null);
        verify(translateViewState, times(1)).showSourceText(null);
    }

    @Test
    public void chooseSourceLanguage() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);

        presenter.chooseSourceLanguage();

        verify(translateViewState, times(1)).chooseSourceLanguage(translation.getSourceLanguage().getId());
    }

    @Test
    public void chooseDestinationLanguage() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);

        presenter.chooseDestinationLanguage();

        verify(translateViewState, times(1)).chooseDestinationLanguage(translation.getDestinationLanguage().getId());
    }

    @Test
    public void showTranslation() {
        presenter.showTranslation(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);
        verify(translateViewState, times(1)).showSourceLanguage(translation.getSourceLanguage().getDescription());
        verify(translateViewState, times(1)).showDestinationLanguage(translation.getDestinationLanguage().getDescription());
        verify(translateViewState, times(1)).showSourceText(translation.getSourceText());
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
    }

    @Test
    public void saveLastTranslation() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);

        presenter.saveLastTranslation();

        verify(translateRepository, times(1)).saveLastTranslation(translation);
    }

    @Test
    public void swapLanguages() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);
        when(translatePresenterCache.getSourceText()).thenReturn(translation.getSourceText());

        presenter.swapLanguages();

        verify(translatePresenterCache, times(1)).swapLanguages();

        verify(translateRepository).translate(translationCaptor.capture(), translationCallbackCaptor.capture());
        assertEquals(translationCaptor.getValue(), translation);
        translationCallbackCaptor.getValue().onLoaded(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
        verify(translateViewState, times(1)).showSourceLanguage(translatePresenterCache.getTranslation().getSourceLanguage().getDescription());
        verify(translateViewState, times(1)).showDestinationLanguage(translatePresenterCache.getTranslation().getDestinationLanguage().getDescription());
    }

    @Test
    public void selectSourceLanguage() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);
        when(translatePresenterCache.getSourceText()).thenReturn(translation.getSourceText());
        when(translateRepository.getLanguageById(anyInt())).thenReturn(translation.getSourceLanguage());

        presenter.selectSourceLanguage(translation.getSourceLanguage().getId());

        verify(translatePresenterCache, times(1)).setSourceLanguage(translation.getSourceLanguage());
        verify(translateViewState, times(1)).showSourceLanguage(translation.getSourceLanguage().getDescription());

        verify(translateRepository).translate(translationCaptor.capture(), translationCallbackCaptor.capture());
        assertEquals(translationCaptor.getValue(), translation);
        translationCallbackCaptor.getValue().onLoaded(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
    }

    @Test
    public void selectDestinationLanguage() {
        when(translatePresenterCache.getTranslation()).thenReturn(translation);
        when(translatePresenterCache.getSourceText()).thenReturn(translation.getSourceText());
        when(translateRepository.getLanguageById(anyInt())).thenReturn(translation.getDestinationLanguage());

        presenter.selectDestinatonLanguage(translation.getDestinationLanguage().getId());

        verify(translatePresenterCache, times(1)).setDestinationLanguage(translation.getDestinationLanguage());
        verify(translateViewState, times(1)).showDestinationLanguage(translation.getDestinationLanguage().getDescription());

        verify(translateRepository).translate(translationCaptor.capture(), translationCallbackCaptor.capture());
        assertEquals(translationCaptor.getValue(), translation);
        translationCallbackCaptor.getValue().onLoaded(translation);

        verify(translatePresenterCache, times(1)).setTranslation(translation);
        verify(translateViewState, times(1)).showTranslatedText(translation.getTranslatedText());
    }
}
