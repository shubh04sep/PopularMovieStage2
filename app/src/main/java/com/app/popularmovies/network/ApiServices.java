package com.app.popularmovies.network;


import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by rahil on 9/9/15.
 */
public interface ApiServices {

    @GET("discover/movie")
    Call<com.app.popularmovies.model.movie_api.MoviesResponseBean> apiMoviesList(@QueryMap Map<String, String> stringMap);


    @GET("movie/{movie_id}?")
    Call<com.app.popularmovies.model.movie_api.MoviesResponseBean.MoviesResult> apiMoviesDetail(@Path("movie_id") long taskId, @QueryMap Map<String, String> stringMap);

    @GET("movie/{movie_id}/videos?")
    Call<com.app.popularmovies.model.trailers_api.TrailersResponseBean> apiMovieTrailers(@Path("movie_id") long movieId, @QueryMap Map<String, String> stringMap);

    @GET("movie/{movie_id}/reviews?")
    Call<com.app.popularmovies.model.reviews_api.ReviewsListingResponse> apiMovieReviews(@Path("movie_id") long movieId, @QueryMap Map<String, String> stringMap);
}
