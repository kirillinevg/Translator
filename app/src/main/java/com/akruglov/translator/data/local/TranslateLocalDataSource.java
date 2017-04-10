package com.akruglov.translator.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by akruglov on 09.04.17.
 */

public class TranslateLocalDataSource {

    private static TranslateLocalDataSource INSTANCE;

    private DbOpenHelper dbHelper;

    private TranslateLocalDataSource(@NonNull Context context) {
        dbHelper = new DbOpenHelper(context);
    }

    public static TranslateLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TranslateLocalDataSource(context);
        }
        return INSTANCE;
    }


}
