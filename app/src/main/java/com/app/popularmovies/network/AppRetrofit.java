package com.app.popularmovies.network;

import com.app.popularmovies.utility.AppConstants;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.lang.reflect.Modifier;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * Network Api class to handle service calls
 * Created by shubham on 19/1/16.
 * Example :-
 * <pre>
 * {@code
 * Call<MoviesResponseBean> call = mApiServices.categories(new GeneralReqBean(mUserData.getUser_id()));
 * call.enqueue(new Callback<MoviesResponseBean>() {
 *
 * @Override
 * public void onResponse(Response<MoviesResponseBean> response, Retrofit retrofit) {
 *      // Do something with response
 * }
 *
 * @Override
 * public void onFailure(Throwable t) {
 *      // Handle error case
 * }
 * });
 * </pre>
 */
public class AppRetrofit {

    private static AppRetrofit mInstance;
    com.app.popularmovies.network.ApiServices apiServices;

    private AppRetrofit() {

        //for logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //setting up client
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);

        //rest adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        /*.serializeNulls()*/
                        .create()))
                .build();

        apiServices = retrofit.create(com.app.popularmovies.network.ApiServices.class);
    }

    public static synchronized AppRetrofit getInstance() {
        if (mInstance == null)
            mInstance = new AppRetrofit();
        return mInstance;
    }

    public com.app.popularmovies.network.ApiServices getApiServices() {
        return apiServices;
    }
}
