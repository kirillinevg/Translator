package com.akruglov.translator.ui.bookmarks.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.view.HistoryView;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenter;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by akruglov on 13.04.17.
 */

@InjectViewState
public class HistoryPresenter extends MvpPresenter<HistoryView> {

    //private List<Language> languages;
    private List<Translation> translations;

    private TranslateRepository translateRepository;

    public HistoryPresenter(@NonNull TranslateRepository translateRepository) {
        this.translateRepository = translateRepository;
        Timber.tag("HistoryPresenter");
    }

    public void init() {
        // After configuration changes view will be restored from ViewState,
        // so we must care only about first initialization
        if (/*languages == null && */translations == null) {
            loadHistory();

        }
    }

    private void loadHistory() {

        Log.d("HISPRES", "loadHistory");

//        languages = new ArrayList<>();
//        translations = new ArrayList<>();
//
//        languages.add(new Language(1, "en", "Английский"));
//        languages.add(new Language(2, "ru", "Русский"));
//        languages.add(new Language(3, "fr", "Французский"));
//        languages.add(new Language(4, "de", "Немецкий"));
//        languages.add(new Language(5, "it", "Итальянский"));
//        languages.add(new Language(6, "es", "Испанский"));
//
//        translations.add(new Translation(1, languages.get(0), languages.get(1), "father", "папа", false));
//        translations.add(new Translation(2, languages.get(0), languages.get(1), "mother", "мама", true));
//        translations.add(new Translation(3, languages.get(4), languages.get(1), "bambino", "ребенок", false));
//        translations.add(new Translation(4, languages.get(0), languages.get(1),
//                "first, last and second another time", "первый, последний и второй другой раз", false));

        translateRepository.loadHistory(new TranslateDataSource.ResultCallback<List<Translation>>() {

            @Override
            public void onLoaded(List<Translation> result) {
                translations = result;
                getViewState().showHistory(translations);
            }

            @Override
            public void onNotAvailable() {
                Timber.e("LoadHistory failed");
            }
        });

        //getViewState().showHistory(translations);
    }
}
