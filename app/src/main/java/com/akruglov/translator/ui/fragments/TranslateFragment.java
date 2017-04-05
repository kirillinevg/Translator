package com.akruglov.translator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akruglov.translator.R;

/**
 * Fragment for translation page of main view pager.
 */

public class TranslateFragment extends Fragment {

    private TextView sourceLanguageTextView;
    private TextView destinationLanguageTextView;
    private ImageButton swapLanguageButton;

    public TranslateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        sourceLanguageTextView = (TextView) view.findViewById(R.id.source_language);

        destinationLanguageTextView = (TextView) view.findViewById(R.id.destination_language);

        swapLanguageButton = (ImageButton) view.findViewById(R.id.swap_languages);

        return view;
    }
}
