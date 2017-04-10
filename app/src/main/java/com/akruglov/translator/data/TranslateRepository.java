package com.akruglov.translator.data;

import android.support.annotation.NonNull;

import com.akruglov.translator.data.local.TranslateLocalDataSource;
import com.akruglov.translator.data.preferences.SharedPreferencesManager;
import com.akruglov.translator.data.remote.TranslateRemoteDataSource;

/**
 * Created by akruglov on 09.04.17.
 */

public class TranslateRepository {

    private static TranslateRepository INSTANCE = null;

    private final TranslateRemoteDataSource translateRemoteDataSource;
    private final TranslateLocalDataSource translateLocalDataSource;
    private final SharedPreferencesManager sharedPreferencesManager;

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

//    public void getTranslateInfo(GetTranslateInfoCallback callback) {
//        if (sharedPreferencesManager.isTranslateInfoExists()) {
//            // Load translate info from preferences
//            callback.onLoadTranslateInfo(translateInfo);
//        } else
//    }
}
