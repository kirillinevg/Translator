package com.akruglov.translator.ui.bookmarks.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.bookmarks.presenter.HistoryPresenter;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akruglov on 12.04.17.
 */

public class HistoryFragment extends MvpAppCompatFragment implements HistoryView {

    private static class TranslationHolder extends RecyclerView.ViewHolder {

        public TextView sourceTextView;
        public TextView translatedTextView;
        public TextView directionTextView;
        public ImageView favoriteImageView;

        public TranslationHolder(View itemView) {
            super(itemView);

            sourceTextView = (TextView) itemView.findViewById(R.id.source_text);
            translatedTextView = (TextView) itemView.findViewById(R.id.translated_text);
            directionTextView = (TextView) itemView.findViewById(R.id.direction_text);
            favoriteImageView = (ImageView) itemView.findViewById(R.id.favorite_image_view);
        }
    }

    private static class TranslationAdapter extends RecyclerView.Adapter<TranslationHolder> {

        private List<Translation> translations;

        public TranslationAdapter(List<Translation> translations) {
            this.translations = translations;
        }


        @Override
        public TranslationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_translation, parent, false);
            return new TranslationHolder(view);
        }

        @Override
        public void onBindViewHolder(TranslationHolder holder, int position) {
            Translation translation = translations.get(position);
            holder.sourceTextView.setText(translation.getSourceText());
            holder.translatedTextView.setText(translation.getTranslatedText());
            String from = translation.getSourceLanguage().getKey();
            String to = translation.getDestinationLanguage().getKey();
            holder.directionTextView.setText(from + " - " + to);
            Drawable favoriteDrawable;
            if (translation.isFavorite()) {
                favoriteDrawable = holder.favoriteImageView.getContext().getResources().getDrawable(R.drawable.ic_bookmark_green_24dp);
            } else {
                favoriteDrawable = holder.favoriteImageView.getContext().getResources().getDrawable(R.drawable.ic_bookmark_grey_24dp);
            }
            holder.favoriteImageView.setImageDrawable(favoriteDrawable);
        }

        @Override
        public int getItemCount() {
            return translations.size();
        }
    }

    private List<Language> languages = new ArrayList<>();
    private List<Translation> translations = new ArrayList<>();

    private RecyclerView translationRecycleView;
    private TranslationAdapter translationAdapter;

    @InjectPresenter
    HistoryPresenter historyPresenter;

    public HistoryFragment() {
        languages.add(new Language(1, "en", "Английский"));
        languages.add(new Language(2, "ru", "Русский"));
        languages.add(new Language(3, "fr", "Французский"));
        languages.add(new Language(4, "de", "Немецкий"));
        languages.add(new Language(5, "it", "Итальянский"));
        languages.add(new Language(6, "es", "Испанский"));

        translations.add(new Translation(1, languages.get(0), languages.get(1), "father", "папа", false));
        translations.add(new Translation(2, languages.get(0), languages.get(1), "mother", "мама", true));
        translations.add(new Translation(3, languages.get(4), languages.get(1), "bambino", "ребенок", false));
        translations.add(new Translation(4, languages.get(0), languages.get(1),
                "first, last and second another time", "первый, последний и второй другой раз", false));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //historyPresenter.init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        translationRecycleView = (RecyclerView) view.findViewById(R.id.translation_recycle_view);
        translationRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        translationRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        translationAdapter = new TranslationAdapter(translations);
        translationRecycleView.setAdapter(translationAdapter);

        historyPresenter.init();

        return view;
    }
}
