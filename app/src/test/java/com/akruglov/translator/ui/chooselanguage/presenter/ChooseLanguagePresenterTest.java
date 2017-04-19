package com.akruglov.translator.ui.chooselanguage.presenter;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.ui.chooselanguage.view.ChooseLanguageView;
import com.akruglov.translator.ui.chooselanguage.view.ChooseLanguageView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by akruglov on 19.04.17.
 */

public class ChooseLanguagePresenterTest {

    @Mock
    ChooseLanguageView chooseLanguageView;

    @Mock
    ChooseLanguageView$$State chooseLanguageViewState;

    @Mock
    ChooseLanguagePresenterCache chooseLanguagePresenterCache;

    @Mock
    TranslateRepository translateRepository;

    @Captor
    private ArgumentCaptor<TranslateDataSource.ResultCallback<List<Language>>> languagesCallbackCaptor;

    private ChooseLanguagePresenter presenter;
    private List<Language> languages;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        presenter = new ChooseLanguagePresenter(chooseLanguagePresenterCache, translateRepository);
        presenter.attachView(chooseLanguageView);
        presenter.setViewState(chooseLanguageViewState);

        languages = new ArrayList<>();
        languages.add(new Language(0, "ru", "Русский"));
        languages.add(new Language(1, "en", "Английский"));
    }

    @Test
    public void init_notNullCache() {
        when(chooseLanguagePresenterCache.isCacheExists()).thenReturn(true);
        when(chooseLanguagePresenterCache.getLanguages()).thenReturn(languages);
        when(chooseLanguagePresenterCache.getCurrentLanguage()).thenReturn(languages.get(1));

        presenter.init(0);

        verify(chooseLanguageViewState, times(1)).showLanguages(languages);
        verify(chooseLanguageViewState, times(1)).setCurrentLanguage(languages.get(1));
    }

    @Test
    public void init_nullCache() {
        when(chooseLanguagePresenterCache.isCacheExists()).thenReturn(false);
        when(chooseLanguagePresenterCache.getCurrentLanguage()).thenReturn(languages.get(0));

        presenter.init(0);

        verify(translateRepository).getLanguages(languagesCallbackCaptor.capture());
        languagesCallbackCaptor.getValue().onLoaded(languages);

        verify(chooseLanguagePresenterCache, times(1)).setLanguages(languages);
        verify(chooseLanguagePresenterCache, times(1)).setCurrentLanguageId(0);

        verify(chooseLanguageViewState, times(1)).showLanguages(languages);
        verify(chooseLanguageViewState, times(1)).setCurrentLanguage(languages.get(0));
    }

    @Test
    public void selectNewLanguage() {
        when(chooseLanguagePresenterCache.getCurrentLanguageId()).thenReturn(0);

        presenter.selectNewLanguage(1);

        verify(chooseLanguageViewState, times(1)).selectNewLanguage(1);
    }
}
