package com.app.popularmovies.activity;

import android.view.Menu;
import android.view.MenuItem;

import com.app.popularmovies.ApplicationController;
import com.app.popularmovies.R;
import com.app.popularmovies.database.MoviesHelper;
import com.app.popularmovies.model.events.MoviesListFilterEvent;
import com.app.popularmovies.utility.AppConstants;

import de.greenrobot.event.EventBus;

public class MoviesListActivity extends BaseActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public boolean mTwoPane;

    @Override
    public int getLayoutById() {
        return R.layout.activity_movies_list;
    }

    @Override
    public void initUi() {
        MoviesHelper.getDatabaseHelperInstance(ApplicationController.getApplicationInstance());
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                EventBus.getDefault().post(new MoviesListFilterEvent(AppConstants.POPULARITY_DESC));
                return true;
            case R.id.highest_rated:
                EventBus.getDefault().post(new MoviesListFilterEvent(AppConstants.HIGHEST_RATED));
                return true;
            case R.id.my_favorites:
                EventBus.getDefault().post(new MoviesListFilterEvent(AppConstants.MY_FAVORITES));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
