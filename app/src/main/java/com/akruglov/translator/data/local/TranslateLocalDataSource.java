package com.akruglov.translator.data.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.TranslateNotificationManager;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by akruglov on 09.04.17.
 */

public class TranslateLocalDataSource implements TranslateDataSource {

    private static TranslateLocalDataSource INSTANCE;

    private DbLab dbLab;
    private TranslateNotificationManager notificationManager;
    private CustomExecutor executor;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private TranslateLocalDataSource(@NonNull Context context,
                                     @NonNull TranslateNotificationManager notificationManager) {
        dbLab = new DbLab(context);
        this.notificationManager = notificationManager;
        executor = new CustomExecutor();
    }

    public static TranslateLocalDataSource getInstance(@NonNull Context context,
                                                       @NonNull TranslateNotificationManager notificationManager) {
        if (INSTANCE == null) {
            INSTANCE = new TranslateLocalDataSource(context, notificationManager);
        }
        return INSTANCE;
    }

    public void getLanguages(final ResultCallback<List<Language>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Language> languages = dbLab.getLanguages();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (languages == null || languages.isEmpty()) {
                            callback.onNotAvailable();
                        } else {
                            callback.onLoaded(languages);
                        }
                    }
                });
            }
        });
    }

    public void setLanguages(final List<Language> languages, final ResultCallback<List<Language>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbLab.setLanguages(languages);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoaded(languages);
                    }
                });
            }
        });
    }

    public void findTranslation(final Translation translation, final ResultCallback<Translation> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Translation foundedTranslation = dbLab.findTranslation(translation);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (foundedTranslation != null) {
                            callback.onLoaded(foundedTranslation);
                        } else {
                            callback.onNotAvailable();
                        }
                    }
                });
            }
        });
    }

    public void insertTranslation(final Translation translation, final ResultCallback<Translation> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Translation updatedTranslation = dbLab.insertTranslation(translation);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoaded(updatedTranslation);
                    }
                });
                notificationManager.notifyTranslationInserted(new Translation(updatedTranslation));
            }
        });
    }

    public void loadHistory(final SparseArray<Language> languages, final ResultCallback<List<Translation>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Translation> translations = dbLab.getTranslations(languages);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (translations != null && !translations.isEmpty()) {
                            callback.onLoaded(translations);
                        } else {
                            callback.onNotAvailable();
                        }
                    }
                });
            }
        });
    }

    private static class CustomExecutor extends ThreadPoolExecutor {

        private static final int CORE_POOL_SIZE = 3;
        private static final int MAX_POOL_SIZE = 5;
        private static final int KEEP_ALIVE_TIME = 120;
        private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
        private static final BlockingDeque<Runnable> WORK_QUEUE = new LinkedBlockingDeque<>();

        CustomExecutor() {
            super(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, WORK_QUEUE);
        }
    }
}
