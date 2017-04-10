package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.translate.view.ITranslateView;
import com.akruglov.translator.ui.translate.view.ITranslateView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by akruglov on 06.04.17.
 */

public final class TranslatePresenterTest {

    @Mock
    ITranslateView translateView;

    @Mock
    ITranslateView$$State translateViewState;

    @Mock
    TranslatePresenterCache translatePresenterCache;

    private TranslatePresenter presenter;
    private Translation translation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new TranslatePresenter(translatePresenterCache);
        presenter.attachView(translateView);
        presenter.setViewState(translateViewState);

        translation = new Translation(new Language(0, "ru", "Русский"),
                new Language(1, "it", "Итальянский"), "Ребенок", "Bambino");
    }

    @Test
    public void init_notNullCache() {
        when(translatePresenterCache.isCacheExists()).thenReturn(true);
        when(translatePresenterCache.getTranslation()).thenReturn(translation);

        presenter.init();

        verify(translateViewState).showSourceLanguage("Русский");
        verify(translateViewState).showDestinationLanguage("Итальянский");
        verify(translateViewState).showSourceText("Ребенок");
        verify(translateViewState).showTranslatedText("Bambino");
    }

    @Test
    public void init_nullCache() {
        when(translatePresenterCache.isCacheExists()).thenReturn(false);

        presenter.init();

        verify(translatePresenterCache, times(1)).updateData(any(Translation.class));

        verify(translateViewState).showSourceLanguage(anyString());
        verify(translateViewState).showDestinationLanguage(anyString());
        verify(translateViewState).showSourceText(any());
        verify(translateViewState).showTranslatedText(any());
    }

    @Test
    public void updateSourceText_notNull() {
        presenter.updateSourceText("test");

        verify(translatePresenterCache, times(1)).updateSourceText("test");
        verify(translatePresenterCache, times(1)).updateTranslatedText("test");

        verify(translateViewState).showTranslatedText("test");
    }

    @Test
    public void updateSourceText_isNull() {
        presenter.updateSourceText(null);

        verify(translatePresenterCache, times(1)).updateSourceText(null);
        verify(translatePresenterCache, times(1)).updateTranslatedText(null);

        verify(translateViewState).showTranslatedText(null);
    }
}
