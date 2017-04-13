package com.akruglov.translator.ui.bookmarks.presenter;

import android.util.Log;

import com.akruglov.translator.ui.bookmarks.view.HistoryView;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenter;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import timber.log.Timber;

/**
 * Created by akruglov on 13.04.17.
 */

@InjectViewState
public class HistoryPresenter extends MvpPresenter<HistoryView> {

    public HistoryPresenter() {
        Timber.tag("HistoryPresenter");
        Log.d("HISPRES", "Ctor");
    }

    public void init() {
        Timber.i("Init called");
        Log.d("HISPRES", "Init called");
    }
}
