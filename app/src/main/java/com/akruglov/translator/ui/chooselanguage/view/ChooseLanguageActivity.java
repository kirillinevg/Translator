package com.akruglov.translator.ui.chooselanguage.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.injection.Injection;
import com.akruglov.translator.ui.chooselanguage.presenter.ChooseLanguagePresenter;
import com.akruglov.translator.ui.chooselanguage.presenter.ChooseLanguagePresenterCache;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

/**
 * Created by akruglov on 12.04.17.
 */

public class ChooseLanguageActivity extends MvpAppCompatActivity implements ChooseLanguageView {

    private static final String TITLE_EXTRA =
            "com.akruglov.translator.ui.chooselanguage.view.ChooseLanguageActivity.Title";

    private static final String LANGUAGE_ID_EXTRA =
            "com.akruglov.translator.ui.chooselanguage.view.ChooseLanguageActivity.LanguageId";

    public static Intent createIntent(Context context, String title, int languageId) {
        Intent intent = new Intent(context, ChooseLanguageActivity.class);
        intent.putExtra(TITLE_EXTRA, title);
        intent.putExtra(LANGUAGE_ID_EXTRA, languageId);
        return intent;
    }

    public static int getLanguageId(Intent result) {
        return result.getIntExtra(LANGUAGE_ID_EXTRA, 1);
    }

    @InjectPresenter
    ChooseLanguagePresenter chooseLanguagePresenter;

    ListView languageListView;
    ArrayAdapter<Language> adapter;

    @ProvidePresenter
    ChooseLanguagePresenter providePresenter() {
        return new ChooseLanguagePresenter(new ChooseLanguagePresenterCache(),
                Injection.provideTranslateRepositiory(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        TextView title = (TextView) findViewById(R.id.choose_lang_title);
        title.setText(getIntent().getStringExtra(TITLE_EXTRA));

        ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseLanguageActivity.super.onBackPressed();
            }
        });

        adapter = new ArrayAdapter<Language>(
                this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1);

        languageListView = (ListView) findViewById(R.id.lang_list);
        languageListView.setAdapter(adapter);
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int newLanguageId = adapter.getItem(position).getId();
                chooseLanguagePresenter.selectNewLanguage(newLanguageId);
            }
        });

        chooseLanguagePresenter.init(getIntent().getIntExtra(LANGUAGE_ID_EXTRA, 1));
    }

    @Override
    public void setCurrentLanguage(Language currentLanguage) {
        languageListView.setItemChecked(adapter.getPosition(currentLanguage), true);
    }

    @Override
    public void showLanguages(List<Language> languages) {
        adapter.addAll(languages);
    }

    @Override
    public void selectNewLanguage(int languageId) {
        Intent data = new Intent();
        data.putExtra(LANGUAGE_ID_EXTRA, languageId);
        setResult(RESULT_OK, data);
        finish();
    }
}
