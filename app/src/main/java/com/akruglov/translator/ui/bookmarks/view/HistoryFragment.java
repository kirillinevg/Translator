package com.akruglov.translator.ui.bookmarks.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akruglov.translator.R;
import com.arellomobile.mvp.MvpAppCompatFragment;

/**
 * Created by akruglov on 12.04.17.
 */

public class HistoryFragment extends MvpAppCompatFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}
