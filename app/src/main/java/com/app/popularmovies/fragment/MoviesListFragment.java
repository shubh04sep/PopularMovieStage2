package com.app.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.popularmovies.ApplicationController;
import com.app.popularmovies.R;
import com.app.popularmovies.activity.MoviesDetailActivity;
import com.app.popularmovies.activity.MoviesListActivity;
import com.app.popularmovies.adapter.MoviesListAdapter;
import com.app.popularmovies.database.MoviesListingDao;
import com.app.popularmovies.model.events.MoviesListFilterEvent;
import com.app.popularmovies.model.movie_api.MoviesResponseBean;
import com.app.popularmovies.network.AppRetrofit;
import com.app.popularmovies.utility.AppConstants;
import com.app.popularmovies.utility.EndlessScrollListener;
import com.app.popularmovies.utility.Lg;
import com.app.popularmovies.utility.SnackBarBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 */
public class MoviesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private GridView mGridView;
    private ArrayList<MoviesResponseBean.MoviesResult> moviesResultsList = new ArrayList<>();
    private int mPagination = 1;
    EndlessScrollListener mEndlessScrollListener = new EndlessScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
                mPagination = mPagination + 1;
                getMoviesList(View.GONE);
            } else
                mSnackBar = SnackBarBuilder.make(mGridView, getString(R.string.no_internet_connction)).build();

        }
    };
    private MoviesListAdapter mAdapter;
    private String mSortByParam = AppConstants.POPULARITY_DESC;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isSortApplied;

    @Override
    public int getLayoutById() {
        return R.layout.fragment_movies_list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initUi() {
        EventBus.getDefault().register(this);
        mGridView = (GridView) findViewById(R.id.popular_movies_gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoviesListActivity moviesListActivity = (MoviesListActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.EXTRA_INTENT_PARCEL, moviesResultsList.get(position));

                if (moviesListActivity.mTwoPane) {
                    MoviesDetailFragment moviesDetailFragment = new MoviesDetailFragment();
                    moviesDetailFragment.setArguments(bundle);
                    moviesListActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, moviesDetailFragment).commit();

                } else {
                    Intent intent = new Intent(mContext, MoviesDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getMoviesList(View.VISIBLE);
    }

    private void getMoviesList(int progressBarVisibility) {
        if (mSortByParam.equals(AppConstants.MY_FAVORITES)) {
            MoviesListingDao moviesListingDao = new MoviesListingDao(mContext);
            mGridView.setOnScrollListener(null);
            moviesResultsList.addAll(moviesListingDao.getFavouriteMovieList());
            mAdapter = new MoviesListAdapter(mContext, moviesResultsList);
            mGridView.setAdapter(mAdapter);
            showProgressBar(false);
        } else if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            showProgressBar(true);

            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(AppConstants.PARAM_SORT_BY, mSortByParam);
            stringHashMap.put(AppConstants.PARAM_API_KEY, AppConstants.API_KEY);
            stringHashMap.put(AppConstants.PARAM_PAGE, mPagination + "");

            Call<MoviesResponseBean> beanCall = AppRetrofit.getInstance().getApiServices().apiMoviesList(stringHashMap);
            beanCall.enqueue(new Callback<MoviesResponseBean>() {
                @Override
                public void onResponse(Response<MoviesResponseBean> response, Retrofit retrofit) {
                    showProgressBar(false);
                    MoviesResponseBean responseBean = response.body();
                    if (responseBean != null) {
                        moviesResultsList.addAll(responseBean.getResults());

                        if (mPagination == 1) {
                            mGridView.setOnScrollListener(mEndlessScrollListener);
                        }

                        if (mAdapter == null) {
                            mAdapter = new MoviesListAdapter(mContext, moviesResultsList);
                            mGridView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        if (responseBean.getResults().size() == 0) {
                            mGridView.setOnScrollListener(null);
                        }

                        if (responseBean.getResults().isEmpty())
                            Lg.i("Retro", response.toString());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Lg.i("Retro", t.toString());
                }
            });
        } else {
            showProgressBar(false);
            mSnackBar = SnackBarBuilder.make(mGridView, getString(R.string.no_internet_connction))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesList(View.VISIBLE);
                        }
                    })
                    .build();
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            mPagination = 1;
            mGridView.setOnScrollListener(null);
            moviesResultsList.clear();
            try {
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            } catch (Exception ignored) {
            }

            if (isSortApplied) {
                isSortApplied = false;
            } else {
                mSortByParam = AppConstants.POPULARITY_DESC;
            }
            getMoviesList(View.VISIBLE);
        } else {
            showProgressBar(false);
            mSnackBar = SnackBarBuilder.make(mGridView, getString(R.string.no_internet_connction)).build();
        }
    }

    public void onEvent(MoviesListFilterEvent filterEvent) {
        isSortApplied = true;
        mSortByParam = filterEvent.getFilter();
        onRefresh();
    }
}
