package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.ui.translate.models.Language;
import com.akruglov.translator.ui.translate.models.TranslateDataModel;
import com.akruglov.translator.ui.translate.view.ITranslateView;
import com.akruglov.translator.ui.translate.view.ITranslateView$$State;
import com.akruglov.translator.ui.translate.view.TranslateFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private TranslateDataModel translateDataModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new TranslatePresenter(translatePresenterCache);
        presenter.attachView(translateView);
        presenter.setViewState(translateViewState);
        //presenter.setTranslatePresenterCache(translatePresenterCache);

        translateDataModel = new TranslateDataModel(new Language(0, "ru", "Русский"),
                new Language(1, "it", "Итальянский"), "Ребенок", "Bambino");
    }

    @Test
    public void init_notNullCache() {
        when(translatePresenterCache.isCacheExists()).thenReturn(true);
        when(translatePresenterCache.getTranslateDataModel()).thenReturn(translateDataModel);

        presenter.init();

        verify(translateViewState).showSourceLanguage("Русский");
        verify(translateViewState).showDestinationLanguage("Итальянский");
        verify(translateViewState).showSourceText("Ребенок");
        verify(translateViewState).showTranslatedText("Bambino");
    }


}
