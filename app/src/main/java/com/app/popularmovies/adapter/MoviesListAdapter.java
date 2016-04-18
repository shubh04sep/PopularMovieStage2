package com.app.popularmovies.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.popularmovies.R;
import com.app.popularmovies.model.movie_api.MoviesResponseBean;
import com.app.popularmovies.utility.AppConstants;
import com.app.popularmovies.utility.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shubham on 2/14/2016.
 */
public class MoviesListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<MoviesResponseBean.MoviesResult> mArrayList;

    public MoviesListAdapter(Context mContext, ArrayList<MoviesResponseBean.MoviesResult> mArrayList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mArrayList = mArrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public MoviesResponseBean.MoviesResult getItem(int i) {
        return mArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_movies_list_row, null);
            holder.moviesThumbnail = (SquareImageView) convertView.findViewById(R.id.movies_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MoviesResponseBean.MoviesResult moviesResult = getItem(i);
        if (!TextUtils.isEmpty(moviesResult.getPosterPath()))
            Picasso.with(mContext)
                    .load(AppConstants.BASE_THUMB_IMAGE_URL + moviesResult.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.moviesThumbnail);
        else {
            holder.moviesThumbnail.setImageResource(R.drawable.placeholder);
        }
        return convertView;
    }

    private class ViewHolder {
        public SquareImageView moviesThumbnail;
    }
}
