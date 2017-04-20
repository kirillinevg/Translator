package com.akruglov.translator.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.akruglov.translator.data.TranslateNotificationManager;
import com.akruglov.translator.data.TranslateRepository;
import com.akruglov.translator.data.local.TranslateLocalDataSource;
import com.akruglov.translator.data.preferences.SharedPreferencesManager;
import com.akruglov.translator.data.remote.TranslateRemoteDataSource;

/**
 * Created by akruglov on 11.04.17.
 */

public class Injection {

    public static TranslateRepository provideTranslateRepositiory(@NonNull Context context) {
        return TranslateRepository.getInstance(
                TranslateRemoteDataSource.getInstance(),
                TranslateLocalDataSource.getInstance(context, TranslateNotificationManager.getInstance()),
                SharedPreferencesManager.getInstance(context));
    }

    public static TranslateNotificationManager provideTranslationNotificationManager() {
        return TranslateNotificationManager.getInstance();
    }
}
