package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Created by akruglov on 08.04.17.
 */

public class TranslatePresenterCacheTest {

    @Test
    public void common_test() {

        // create cache
        TranslatePresenterCache translatePresenterCache = new TranslatePresenterCache();

        // assert isCacheExist
        assertFalse(translatePresenterCache.isCacheExists());

        // test no null exception
        translatePresenterCache.updateSourceText(null);
        translatePresenterCache.updateTranslatedText(null);

        // update cache
        translatePresenterCache.setTranslation(getData());

        // update texts
        translatePresenterCache.updateSourceText("мама");
        translatePresenterCache.updateTranslatedText("mother");

        // assert new texts
        assertEquals(translatePresenterCache.getTranslation().getSourceText(), "мама");
        assertEquals(translatePresenterCache.getTranslation().getTranslatedText(), "mother");
    }

    private Translation getData() {
        return new Translation(
                -1,
                new Language(0, "ru", "Русский"),
                new Language(1, "en", "Англйский"),
                "папа",
                "father",
                false);
    }
}
