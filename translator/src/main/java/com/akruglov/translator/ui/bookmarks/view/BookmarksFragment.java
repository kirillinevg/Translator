package com.akruglov.translator.ui.bookmarks.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.akruglov.translator.R;
import com.akruglov.translator.ui.adapters.TitledViewPagerAdapter;
import com.akruglov.translator.ui.bookmarks.presenter.FavoritesPresenter;
import com.akruglov.translator.ui.bookmarks.presenter.HistoryPresenter;
import com.arellomobile.mvp.MvpFacade;
import com.arellomobile.mvp.PresenterStore;
import com.arellomobile.mvp.presenter.PresenterType;

/**
 * Created by akruglov on 12.04.17.
 */

public class BookmarksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TitledViewPagerAdapter adapter;
    private ImageButton clearBookmarksButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.bookmarks_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        setupViewPager();

        clearBookmarksButton = (ImageButton) view.findViewById(R.id.clear_bookmarks_button);
        clearBookmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();
                    HistoryPresenter presenter =
                            (HistoryPresenter) presenterStore.get(PresenterType.GLOBAL,
                                    "HistoryPresenter", HistoryPresenter.class);
                    presenter.showClearHistoryNotification();
                } else if (viewPager.getCurrentItem() == 1) {
                    PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();
                    FavoritesPresenter presenter =
                            (FavoritesPresenter) presenterStore.get(PresenterType.GLOBAL,
                                    "FavoritesPresenter", FavoritesPresenter.class);
                    presenter.showClearFavoritesNotification();
                }
            }
        });

        return view;
    }

    private void setupViewPager() {
        adapter = new TitledViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_tab_title));
        adapter.addFragment(new FavoritesFragment(), getString(R.string.favorites_tab_title));
        viewPager.setAdapter(adapter);
    }

    /**
     * Method is overrided for detecting a moment when fragment
     * becomes visible by selecting its tab in BottomNavigationBar.
     * @param menuVisible
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && isResumed()) {
            if (viewPager.getCurrentItem() == 0) {
                Log.i("BookmarkFragment", "onMenuVisibility");
                HistoryFragment historyFragment = (HistoryFragment)adapter.getItem(0);
                if (historyFragment.isAdded()) {
                    // After configuration changes the fragment will be not added yet,
                    // so we don't need to initialize its presenter
                    // (presenter already initialized)
                    historyFragment.onBecomeVisible();
                }
            }
        }
    }
}
