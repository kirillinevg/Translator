package com.akruglov.translator.ui.translate.presenter;

import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by akruglov on 08.04.17.
 */

public class TranslatePresenterCacheTest {

    @Test
    public void common_test() {

        // create cache
        TranslatePresenterCache translatePresenterCache = new TranslatePresenterCache();

        // assert isCacheExist
        assertThat(translatePresenterCache.isCacheExists()).isFalse();

        // test no null exception
        translatePresenterCache.updateSourceText(null);
        translatePresenterCache.updateTranslatedText(null);

        // update cache
        translatePresenterCache.updateData(getData());

        // update texts
        translatePresenterCache.updateSourceText("мама");
        translatePresenterCache.updateTranslatedText("mother");

        // assert new texts
        assertThat(translatePresenterCache.getTranslation().getSourceText()).isEqualTo("мама");
        assertThat(translatePresenterCache.getTranslation().getTranslatedText()).isEqualTo("mother");
    }

    private Translation getData() {
        return new Translation(
                new Language(0, "ru", "Русский"),
                new Language(1, "en", "Англйский"),
                "папа",
                "father");
    }
}
