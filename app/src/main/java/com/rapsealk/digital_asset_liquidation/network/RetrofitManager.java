package com.rapsealk.digital_asset_liquidation.network;

import com.rapsealk.digital_asset_liquidation.GlobalVariable;
import com.rapsealk.digital_asset_liquidation.network.body.AddressBody;
import com.rapsealk.digital_asset_liquidation.network.response.AccountResponse;
import com.rapsealk.digital_asset_liquidation.network.response.BalanceResponse;
import com.rapsealk.digital_asset_liquidation.network.response.TokenResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rapsealk on 2018. 5. 26..
 */
public interface RetrofitManager {

    public static final Retrofit instance = new Retrofit.Builder()
            .baseUrl(GlobalVariable.API_SERVER_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /*
    @GET("user")
    Observable<UserResponse> getUser(
        @Header("Authorization") String authorization
    );

    @POST("auth/signin")
    Observable<TokenResponse> signIn(
        @Body IdAndPasswordBody body
    );
    */

    @POST("auth/signup")
    Observable<TokenResponse> signUp();

    @POST("accounts/create")
    Observable<AccountResponse> createAccount();

    @GET("accounts/balance")
    Observable<BalanceResponse> balanceOf(
        @Query("address") String address
    );

    @POST("accounts/airdrop")
    Observable<BalanceResponse> getAirdrop(
        @Body AddressBody body
    );
}
