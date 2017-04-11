package com.akruglov.translator;

import android.app.Application;

import com.squareup.leakcanary.*;
import com.squareup.leakcanary.BuildConfig;

import timber.log.Timber;

/**
 * Created by akruglov on 11.04.17.
 */

public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        if (timber.log.BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
