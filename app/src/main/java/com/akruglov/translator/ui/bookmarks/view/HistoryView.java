package com.akruglov.translator.ui.bookmarks.view;

import com.akruglov.translator.data.models.Translation;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

/**
 * Created by akruglov on 13.04.17.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface HistoryView extends MvpView {
    void showHistory(List<Translation> translations);

    @StateStrategyType(SkipStrategy.class)
    void insertTranslation(Translation translation);
}
