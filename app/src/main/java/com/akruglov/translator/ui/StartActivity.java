package com.akruglov.translator.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.akruglov.translator.R;
import com.akruglov.translator.data.models.Translation;
import com.akruglov.translator.ui.adapters.ViewPagerAdapter;
import com.akruglov.translator.ui.bookmarks.view.BookmarksFragment;
import com.akruglov.translator.ui.translate.presenter.TranslatePresenter;
import com.akruglov.translator.ui.translate.view.TranslateFragment;
import com.arellomobile.mvp.MvpFacade;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.PresenterStore;
import com.arellomobile.mvp.presenter.PresenterType;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MenuItem prevMenuItem;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_translate:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_bookmark:
                    viewPager.setCurrentItem(1);
                    return true;
//                case R.id.navigation_settings:
//                    return true;
            }
            return false;
        }

    };

    private ViewPager.OnPageChangeListener onPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }

            bottomNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(onPageChangeListener);

        setupViewPager();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            navigateToTranslatePage();
        } else {
            super.onBackPressed();
        }
    }

    public void navigateToTranslatePage(Translation translation) {
        TranslateFragment fragment = (TranslateFragment) adapter.getItem(0);
        navigateToTranslatePage();
        PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();
        TranslatePresenter presenter =
                (TranslatePresenter) presenterStore.get(PresenterType.GLOBAL,
                        "TranslatePresenter", TranslatePresenter.class);
        presenter.showTranslation(translation);
    }

    private void navigateToTranslatePage() {
        onPageChangeListener.onPageSelected(0);
        viewPager.setCurrentItem(0);
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TranslateFragment());
        adapter.addFragment(new BookmarksFragment());
        viewPager.setAdapter(adapter);
    }

}
