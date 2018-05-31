package com.rapsealk.digital_asset_liquidation.network;

import com.rapsealk.digital_asset_liquidation.GlobalVariable;
import com.rapsealk.digital_asset_liquidation.network.body.IdAndPasswordBody;
import com.rapsealk.digital_asset_liquidation.network.response.TokenResponse;
import com.rapsealk.digital_asset_liquidation.network.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by rapsealk on 2018. 5. 26..
 */
public interface RetrofitManager {

    public static final Retrofit instance = new Retrofit.Builder()
            .baseUrl(GlobalVariable.API_SERVER_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("user")
    Observable<UserResponse> getUser(
        @Header("Authorization") String authorization
    );

    @POST("auth/signin")
    Observable<TokenResponse> signIn(
        @Body IdAndPasswordBody body
    );
}
