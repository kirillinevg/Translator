package com.akruglov.translator.ui.bookmarks.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.injection.Injection;
import com.akruglov.translator.ui.bookmarks.presenter.HistoryPresenter;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akruglov on 12.04.17.
 */

public class HistoryFragment extends MvpAppCompatFragment implements HistoryView {

    private static class TranslationHolder extends RecyclerView.ViewHolder {

        private TextView sourceTextView;
        private TextView translatedTextView;
        private TextView directionTextView;
        private ImageView favoriteImageView;

        final Drawable isFavoriteDrawable;
        final Drawable isNotFavoriteDrawable;

        private Translation translation;

        public TranslationHolder(View itemView) {
            super(itemView);

            Resources resources = itemView.getContext().getResources();
            isFavoriteDrawable = resources.getDrawable(R.drawable.ic_bookmark_green_24dp);
            isNotFavoriteDrawable = resources.getDrawable(R.drawable.ic_bookmark_grey_24dp);

            sourceTextView = (TextView) itemView.findViewById(R.id.source_text);
            translatedTextView = (TextView) itemView.findViewById(R.id.translated_text);
            directionTextView = (TextView) itemView.findViewById(R.id.direction_text);
            favoriteImageView = (ImageView) itemView.findViewById(R.id.favorite_image_view);
        }

        public void bind(Translation translation) {
            this.translation = translation;
            sourceTextView.setText(translation.getSourceText());
            translatedTextView.setText(translation.getTranslatedText());
            String from = translation.getSourceLanguage().getKey();
            String to = translation.getDestinationLanguage().getKey();
            directionTextView.setText(from + " - " + to);
            favoriteImageView.setImageDrawable(translation.isFavorite() ? isFavoriteDrawable : isNotFavoriteDrawable);
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
            holder.bind(translation);
        }

        @Override
        public int getItemCount() {
            return translations.size();
        }
    }

    private RecyclerView translationRecycleView;
    private TranslationAdapter translationAdapter;

    @InjectPresenter(type= PresenterType.GLOBAL, tag="HistoryPresenter")
    HistoryPresenter historyPresenter;

    @ProvidePresenter(type=PresenterType.GLOBAL, tag="HistoryPresenter")
    HistoryPresenter provideHistoryPresenter() {
        return new HistoryPresenter(
                Injection.provideTranslateRepositiory(getActivity().getApplicationContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        translationRecycleView = (RecyclerView) view.findViewById(R.id.translation_recycle_view);
        translationRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        translationRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        return view;
    }

    /** Method is overrided for detecting a moment when fragment
     * becomes visible by selecting its tab.
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && isResumed()) {
            onBecomeVisible();
        }
    }

    public void onBecomeVisible() {
        historyPresenter.init();
    }

    @Override
    public void showHistory(List<Translation> translations) {
        // Will be called only at first initialization or during recreating fragment
        // after configuration changes
        translationAdapter = new TranslationAdapter(translations);
        translationRecycleView.setAdapter(translationAdapter);
    }
}
