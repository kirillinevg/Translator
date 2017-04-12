package com.akruglov.translator.ui.translate.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.injection.Injection;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenter;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenterCache;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

/**
 * Fragment for translation page of main view pager.
 */

public class TranslateFragment extends MvpAppCompatFragment implements ITranslateView {

    private TextView sourceLanguageTextView;
    private TextView destinationLanguageTextView;
    private ImageButton swapLanguageButton;
    private ImageButton clearSourceTextButton;
    private EditText sourceTextEditor;
    private TextView translatedTextView;

    @InjectPresenter
    TranslatePresenter translatePresenter;

    @ProvidePresenter
    TranslatePresenter provideTranslatePresenter() {
        return new TranslatePresenter(new TranslatePresenterCache(),
                Injection.provideTranslateRepositiory(getActivity().getApplicationContext()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        translatePresenter.init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        sourceLanguageTextView = (TextView) view.findViewById(R.id.source_language);

        destinationLanguageTextView = (TextView) view.findViewById(R.id.destination_language);

        swapLanguageButton = (ImageButton) view.findViewById(R.id.swap_languages);
        swapLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatePresenter.swapLanguages();
            }
        });

        clearSourceTextButton = (ImageButton) view.findViewById(R.id.clear_source_text);
        clearSourceTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatePresenter.updateSourceText(null);
            }
        });

        sourceTextEditor = (EditText) view.findViewById(R.id.source_text);

        sourceTextEditor.addTextChangedListener(new TextWatcher() {

            Handler uiHandler = new Handler();
            Runnable textChangedHandler;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String text = s.toString();
                uiHandler.removeCallbacks(textChangedHandler);
                textChangedHandler = new Runnable() {
                    @Override
                    public void run() {
                        updateSourceText(text);
                    }
                };
                uiHandler.postDelayed(textChangedHandler, 500);
            }

            private void updateSourceText(String text) {
                translatePresenter.updateSourceText(text);
            }
        });

        translatedTextView = (TextView) view.findViewById(R.id.translated_text);

        return view;
    }

    @Override
    public void showSourceLanguage(String language) {
        sourceLanguageTextView.setText(language);
    }

    @Override
    public void showDestinationLanguage(String language) {
        destinationLanguageTextView.setText(language);
    }

    @Override
    public void showSourceText(String text) {
        sourceTextEditor.setText(text);
    }

    @Override
    public void showTranslatedText(String text) {
        translatedTextView.setText(text);
    }
}
