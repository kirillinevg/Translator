package com.akruglov.translator.ui.chooselanguage.view;

import com.akruglov.translator.data.models.Language;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

/**
 * Created by akruglov on 12.04.17.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ChooseLanguageView extends MvpView {

    void setCurrentLanguage(Language currentLanguage);
    void showLanguages(List<Language> languages);
    void selectNewLanguage(int languageId);
}
