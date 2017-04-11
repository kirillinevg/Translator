package com.akruglov.translator.data;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.akruglov.translator.data.local.TranslateLocalDataSource;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.data.preferences.SharedPreferencesManager;
import com.akruglov.translator.data.remote.TranslateRemoteDataSource;

import java.util.List;
import java.util.Locale;

/**
 * Created by akruglov on 09.04.17.
 */

public class TranslateRepository implements TranslateDataSource {

    private static TranslateRepository INSTANCE = null;

    private final TranslateRemoteDataSource translateRemoteDataSource;
    private final TranslateLocalDataSource translateLocalDataSource;
    private final SharedPreferencesManager sharedPreferencesManager;

    SparseArray<Language> cache = new SparseArray<>();

    private TranslateRepository(@NonNull TranslateRemoteDataSource translateRemoteDataSource,
                                @NonNull TranslateLocalDataSource translateLocalDataSource,
                                @NonNull SharedPreferencesManager sharedPreferencesManager) {
        this.translateRemoteDataSource = translateRemoteDataSource;
        this.translateLocalDataSource = translateLocalDataSource;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public static TranslateRepository getInstance(TranslateRemoteDataSource translateRemoteDataSource,
                                                  TranslateLocalDataSource translateLocalDataSource,
                                                  SharedPreferencesManager sharedPreferencesManager) {
        if (INSTANCE == null) {
            INSTANCE = new TranslateRepository(
                    translateRemoteDataSource,
                    translateLocalDataSource,
                    sharedPreferencesManager);
        }
        return INSTANCE;
    }

    private void makeDefaultTranslationFromLocal(ResultCallback<Translation> callback) {
        translateLocalDataSource.getLanguages(new ResultCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                saveLanguagesToCache(result);
                Translation translation = getDefaultTransaltion();
                callback.onLoaded(translation);
            }

            @Override
            public void onNotAvailable() {
                getLanguagesFromNetwork(callback);
            }
        });
    }

    private void getLanguagesFromNetwork(ResultCallback<Translation> callback) {
        translateRemoteDataSource.getLanguages(new ResultCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                saveLanguagesToLocal(result, callback);
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    private void saveLanguagesToLocal(List<Language> languages, ResultCallback<Translation> callback) {
        translateLocalDataSource.setLanguages(languages, new ResultCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                saveLanguagesToCache(result);
                Translation translation = getDefaultTransaltion();
                callback.onLoaded(translation);
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    public void getLastTranslation(ResultCallback<Translation> callback) {
        if (sharedPreferencesManager.isTranslateInfoExists()) {
            Translation translation = sharedPreferencesManager.getTranslation();
            callback.onLoaded(translation);
        } else {
            makeDefaultTranslationFromLocal(callback);
        }    
    }

    private String getSystemlanguage() {
        return Locale.getDefault().getLanguage();
    }

    private Language getLanguageFromCacheByKey(String key) {
        for (int i = 0; i < cache.size(); ++i) {
            Language language = cache.get(i);
            if (language.getKey().equals(key)) {
                return language;
            }
        }
        return null;
    }

    private Language getDefaultSourceLanguage() {
        String key = getSystemlanguage().equals("en") ? "fr" : "en";
        return getLanguageFromCacheByKey(key);
    }

    private Language getDefaultDestinationLanguage() {
        String key = getSystemlanguage();
        return getLanguageFromCacheByKey(key);
    }

    private void saveLanguagesToCache(List<Language> languages) {
        for (Language language : languages) {
            cache.append(language.getId(), language);
        }
    }

    private Translation getDefaultTransaltion() {
        return new Translation(
                -1, // id
                getDefaultSourceLanguage(),
                getDefaultDestinationLanguage(),
                null, // source text
                null  // translated text
        );
    }
}
