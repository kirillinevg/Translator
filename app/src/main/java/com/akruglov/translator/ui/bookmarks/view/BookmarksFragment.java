package com.akruglov.translator.ui.bookmarks.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akruglov.translator.R;
import com.akruglov.translator.ui.adapters.TitledViewPagerAdapter;

/**
 * Created by akruglov on 12.04.17.
 */

public class BookmarksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.bookmarks_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        TitledViewPagerAdapter adapter = new TitledViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_tab_title));
        adapter.addFragment(new FavoritesFragment(), getString(R.string.favorites_tab_title));
        viewPager.setAdapter(adapter);
    }
}
