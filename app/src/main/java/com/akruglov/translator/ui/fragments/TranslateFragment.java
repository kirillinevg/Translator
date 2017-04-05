package com.akruglov.translator.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private ImageButton clearSourceTextButton;
    private EditText sourceTextEditor;
    private TextView translatedTextView;

    public TranslateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        sourceLanguageTextView = (TextView) view.findViewById(R.id.source_language);

        destinationLanguageTextView = (TextView) view.findViewById(R.id.destination_language);

        swapLanguageButton = (ImageButton) view.findViewById(R.id.swap_languages);

        clearSourceTextButton = (ImageButton) view.findViewById(R.id.clear_source_text);
        clearSourceTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceTextEditor.setText(null);
            }
        });

        sourceTextEditor = (EditText) view.findViewById(R.id.source_text);
        translatedTextView = (TextView) view.findViewById(R.id.translated_text);

        return view;
    }
}
