package com.rapsealk.digital_asset_liquidation.network;

import com.rapsealk.digital_asset_liquidation.network.response.DefaultResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by rapsealk on 2018. 5. 26..
 */
public interface RetrofitManager {

    public static final Retrofit instance = new Retrofit.Builder()
            .baseUrl("HTTP_BASE_URL")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("user")
    Observable<DefaultResponse> getUser(
        @Header("Authorization") String authorization
    );

    /*
    @GET("user")
    Call<DefaultResponse> getUser(
        @Header("Authorization") String authorization
    );
    */
}
