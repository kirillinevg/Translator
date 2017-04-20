package com.akruglov.translator.ui.chooselanguage.presenter;

import com.akruglov.translator.data.models.Language;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by akruglov on 19.04.17.
 */

public class ChooseLanguagePresenterCacheTest {

    private ChooseLanguagePresenterCache chooseLanguagePresenterCache;
    private List<Language> languages;

    @Before
    public void setUp() {
        chooseLanguagePresenterCache = new ChooseLanguagePresenterCache();
        languages = new ArrayList<>();
        languages.add(new Language(0, "ru", "Русский"));
        languages.add(new Language(1, "en", "Английский"));
    }

    @Test
    public void setLanguages_test() {

        assertFalse(chooseLanguagePresenterCache.isCacheExists());

        chooseLanguagePresenterCache.setLanguages(languages);

        assertEquals(chooseLanguagePresenterCache.getLanguages(), languages);

        chooseLanguagePresenterCache.setCurrentLanguageId(1);

        assertEquals(chooseLanguagePresenterCache.getCurrentLanguageId(), 1);
        assertEquals(chooseLanguagePresenterCache.getCurrentLanguage(), languages.get(1));

        chooseLanguagePresenterCache.setCurrentLanguageId(5);

        assertEquals(chooseLanguagePresenterCache.getCurrentLanguage(), null);
    }

    @Test
    public void setCurrentLanguage_success() {
        chooseLanguagePresenterCache.setLanguages(languages);

        chooseLanguagePresenterCache.setCurrentLanguageId(1);

        assertEquals(chooseLanguagePresenterCache.getCurrentLanguageId(), 1);
        assertEquals(chooseLanguagePresenterCache.getCurrentLanguage(), languages.get(1));
    }

    @Test
    public void setCurrentLanguage_failed() {
        chooseLanguagePresenterCache.setLanguages(languages);

        chooseLanguagePresenterCache.setCurrentLanguageId(5);

        assertEquals(chooseLanguagePresenterCache.getCurrentLanguageId(), 5);
        assertEquals(chooseLanguagePresenterCache.getCurrentLanguage(), null);
    }
}
