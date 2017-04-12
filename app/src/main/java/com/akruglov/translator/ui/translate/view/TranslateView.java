package com.akruglov.translator.ui.translate.view;

import com.akruglov.translator.data.models.Language;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by akruglov on 06.04.17.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface TranslateView extends MvpView {

    void showSourceLanguage(String language);
    void showDestinationLanguage(String language);
    void showSourceText(String text);
    void showTranslatedText(String text);
}
