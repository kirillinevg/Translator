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

    private List<Language> languageCache;
    private SparseArray<Language> languageCacheMap = new SparseArray<>();

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

    public void translate(final Translation translation, final ResultCallback<Translation> callback) {
        translateLocalDataSource.findTranslation(translation, new ResultCallback<Translation>() {

            @Override
            public void onLoaded(Translation result) {
                callback.onLoaded(result);
            }

            @Override
            public void onNotAvailable() {
                getTranslationFromNetwork(translation, callback);
            }
        });
    }

    private void getTranslationFromNetwork(final Translation translation, final ResultCallback<Translation> callback) {
        translateRemoteDataSource.getTranslation(translation, new ResultCallback<Translation>() {

            @Override
            public void onLoaded(Translation result) {
                translateLocalDataSource.insertTranslation(translation, callback);
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    public Language getLanguageById(int languageId) {
        // Languages are already in cache
        return getLanguageFromCacheById(languageId);
    }

    public void getLanguages(final ResultCallback<List<Language>> callback) {
        if (languageCache != null) {
            callback.onLoaded(languageCache);
        } else {
            getLanguagesFromLocal(callback);
        }
    }

    public void loadHistory(final ResultCallback<List<Translation>> callback) {
        translateLocalDataSource.loadHistory(languageCacheMap, new ResultCallback<List<Translation>>() {

            @Override
            public void onLoaded(List<Translation> result) {
                callback.onLoaded(result);
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    private void getLanguagesFromLocal(final ResultCallback<List<Language>> callback) {
        translateLocalDataSource.getLanguages(new ResultCallback<List<Language>>() {

            @Override
            public void onLoaded(List<Language> result) {
                saveLanguagesToCache(result);
                if (callback != null) callback.onLoaded(result);
            }

            @Override
            public void onNotAvailable() {
                if (callback != null) callback.onNotAvailable();
            }
        });
    }

    public void getLastTranslation(ResultCallback<Translation> callback) {
        if (sharedPreferencesManager.isTranslateInfoExists()) {
            Translation translation = sharedPreferencesManager.getTranslation();
            getLanguagesFromLocal(null);
            callback.onLoaded(translation);
        } else {
            makeDefaultTranslationFromLocal(callback);
        }    
    }

    public void saveLastTranslation(Translation translation) {
        sharedPreferencesManager.saveTranslation(translation);
    }

    private void makeDefaultTranslationFromLocal(final ResultCallback<Translation> callback) {
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

    private void getLanguagesFromNetwork(final ResultCallback<Translation> callback) {
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

    private void saveLanguagesToLocal(List<Language> languages, final ResultCallback<Translation> callback) {
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

    private String getSystemlanguage() {
        return Locale.getDefault().getLanguage();
    }

    private Language getLanguageFromCacheByKey(String key) {
        for (Language language : languageCache) {
            if (language.getKey().equals(key)) {
                return language;
            }
        }
        return null;
    }

    private Language getLanguageFromCacheById(int id) {
        for (Language language : languageCache) {
            if (language.getId() == id) {
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
        languageCache = languages;
        for (Language language : languages) {
            languageCacheMap.append(language.getId(), language);
        }
    }

    private Translation getDefaultTransaltion() {
        return new Translation(
                -1, // id
                getDefaultSourceLanguage(),
                getDefaultDestinationLanguage(),
                null, // source text
                null,  // translated text,
                false // is not favorite
        );
    }
}
