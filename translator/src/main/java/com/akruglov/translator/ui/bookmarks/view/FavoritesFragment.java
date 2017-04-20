package com.akruglov.translator.ui.bookmarks.view;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.injection.Injection;
import com.akruglov.translator.ui.StartActivity;
import com.akruglov.translator.ui.bookmarks.presenter.FavoritesPresenter;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by akruglov on 12.04.17.
 */

public class FavoritesFragment extends MvpAppCompatFragment implements FavoritesView {

    private static class TranslationHolder extends RecyclerView.ViewHolder {

        private TextView sourceTextView;
        private TextView translatedTextView;
        private TextView directionTextView;
        private ImageView favoriteImageView;

        final Drawable isFavoriteDrawable;
        final Drawable isNotFavoriteDrawable;

        private Translation translation;
        private FavoritesPresenter presenter;

        public TranslationHolder(View itemView, final FavoritesPresenter presenter) {
            super(itemView);

            this.presenter = presenter;

            Resources resources = itemView.getContext().getResources();
            isFavoriteDrawable = resources.getDrawable(R.drawable.ic_bookmark_green_24dp);
            isNotFavoriteDrawable = resources.getDrawable(R.drawable.ic_bookmark_grey_24dp);

            sourceTextView = (TextView) itemView.findViewById(R.id.source_text);
            translatedTextView = (TextView) itemView.findViewById(R.id.translated_text);
            directionTextView = (TextView) itemView.findViewById(R.id.direction_text);
            favoriteImageView = (ImageView) itemView.findViewById(R.id.favorite_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showTranslationDetails(translation);
                }
            });

            favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.i("switch history-favorites");
                    translation.setFavorite(!translation.isFavorite());
                    favoriteImageView.setImageDrawable(translation.isFavorite() ? isFavoriteDrawable : isNotFavoriteDrawable);
                    presenter.setFavorite(translation);
                }
            });
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

    private static class TranslationAdapter extends RecyclerView.Adapter<FavoritesFragment.TranslationHolder> {

        private List<Translation> favorites;
        private FavoritesPresenter presenter;

        public TranslationAdapter(List<Translation> favorites, FavoritesPresenter presenter) {
            this.favorites = favorites;
            this.presenter = presenter;
        }

        @Override
        public FavoritesFragment.TranslationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_translation, parent, false);
            return new FavoritesFragment.TranslationHolder(view, presenter);
        }

        @Override
        public void onBindViewHolder(FavoritesFragment.TranslationHolder holder, int position) {
            Translation translation = favorites.get(position);
            holder.bind(translation);
        }

        public void setFavorites(List<Translation> favorites) {
            this.favorites = favorites;
        }

        @Override
        public int getItemCount() {
            return favorites.size();
        }

        public void clear() {
            favorites = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    private RecyclerView translationRecycleView;
    private FavoritesFragment.TranslationAdapter translationAdapter;

    @InjectPresenter(type= PresenterType.GLOBAL, tag="FavoritesPresenter")
    FavoritesPresenter favoritesPresenter;

    @ProvidePresenter(type=PresenterType.GLOBAL, tag="FavoritesPresenter")
    FavoritesPresenter provideFavoritesPresenter() {
        return new FavoritesPresenter(Injection.provideTranslateRepositiory(getActivity().getApplicationContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

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
        favoritesPresenter.init();
    }

    @Override
    public void showFavorites(List<Translation> favorites) {
        if (translationAdapter == null) {
            translationAdapter = new TranslationAdapter(favorites, favoritesPresenter);
            translationRecycleView.setAdapter(translationAdapter);
        } else {
            translationAdapter.setFavorites(favorites);
            translationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showTranslationDetails(Translation translation) {
        StartActivity activity = (StartActivity) getActivity();
        activity.navigateToTranslatePage(translation);
    }

    public void showClearFavoritesNotification() {

        if (translationAdapter.getItemCount() == 0) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.favorites_title)
               .setMessage(R.string.favorites_clear_question)
               .setNegativeButton(R.string.negative_button_text, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               })
               .setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       favoritesPresenter.clearFavorites();
                       translationAdapter.clear();

                   }
               });

        builder.create().show();
    }

    @Override
    public void clearFavorites() {

    }
}
