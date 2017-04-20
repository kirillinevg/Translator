package com.akruglov.translator.ui.chooselanguage.presenter;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.models.Language;

import java.util.List;

/**
 * Created by akruglov on 12.04.17.
 */

public class ChooseLanguagePresenterCache {

    private List<Language> languages;
    private int currentLanguageId;

    public boolean isCacheExists() {
        return languages != null;
    }

    public void setLanguages(@NonNull List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public int getCurrentLanguageId() {
        return currentLanguageId;
    }

    public void setCurrentLanguageId(int currentLanguageId) {
        this.currentLanguageId = currentLanguageId;
    }

    public Language getCurrentLanguage() {
        for (Language language : languages) {
            if (language.getId() == currentLanguageId) {
                return language;
            }
        }
        return null;
    }
}
