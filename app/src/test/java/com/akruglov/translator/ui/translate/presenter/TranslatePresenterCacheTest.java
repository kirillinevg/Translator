package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Created by akruglov on 08.04.17.
 */

public class TranslatePresenterCacheTest {

    private Translation translation;
    private TranslatePresenterCache translatePresenterCache;

    @Before
    public void setUp() {
        translatePresenterCache = new TranslatePresenterCache();
        translation = new Translation(
                -1,
                new Language(0, "ru", "Русский"),
                new Language(1, "en", "Англйский"),
                "папа",
                "father",
                false);
    }

    @Test
    public void common_test() {

        // assert isCacheExist
        assertFalse(translatePresenterCache.isCacheExists());

        // test no null exception
        translatePresenterCache.updateSourceText(null);
        translatePresenterCache.updateTranslatedText(null);

        // update cache
        translatePresenterCache.setTranslation(translation);

        // update texts
        translatePresenterCache.updateSourceText("мама");
        translatePresenterCache.updateTranslatedText("mother");

        // assert new texts
        assertEquals(translatePresenterCache.getTranslation().getSourceText(), "мама");
        assertEquals(translatePresenterCache.getTranslation().getTranslatedText(), "mother");



        assertEquals(translatePresenterCache.getSourceText(), translation.getSourceText());
    }

    @Test
    public void swapLanguages_test() {
        translatePresenterCache.setTranslation(translation);

        Translation copy = new Translation(translation);

        translatePresenterCache.swapLanguages();

        assertEquals(translatePresenterCache.getTranslation().getSourceLanguage(), copy.getDestinationLanguage());
        assertEquals(translatePresenterCache.getTranslation().getDestinationLanguage(), copy.getSourceLanguage());
    }

    @Test
    public void setLanguages_test() {
        translatePresenterCache.setTranslation(translation);
        Language newLanguage = new Language(8, "fr", "Французский");
        translatePresenterCache.setSourceLanguage(newLanguage);
        translatePresenterCache.setDestinationLanguage(newLanguage);

        assertEquals(translatePresenterCache.getTranslation().getSourceLanguage(), newLanguage);
        assertEquals(translatePresenterCache.getTranslation().getDestinationLanguage(), newLanguage);
    }
}
