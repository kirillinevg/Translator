package com.akruglov.translator.data;

import android.os.Handler;
import android.os.Looper;

import com.akruglov.translator.data.models.Translation;

import java.util.HashSet;

/**
 * Created by akruglov on 17.04.17.
 */

public class TranslateNotificationManager {

    private static TranslateNotificationManager INSTANCE = null;

    public static TranslateNotificationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranslateNotificationManager();
        }
        return INSTANCE;
    }

    private HashSet<TranslationListener> translationListeners = new HashSet<>();
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public interface TranslationListener {
        void onInsert(Translation translation);
    }

    public void addListener(TranslationListener listener) {
        translationListeners.add(listener);
    }

    public void removeListener(TranslationListener listener) {
        translationListeners.remove(listener);
    }

    public void notifyTranslationInserted(final Translation translation) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyTranslationInsertedOnUiThread(translation);
            }
        });
    }

    private void notifyTranslationInsertedOnUiThread(Translation translation) {
        for (TranslationListener l : translationListeners) {
            l.onInsert(translation);
        }
    }
}
