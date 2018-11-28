package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rapsealk.digital_asset_liquidation.adapter.EthAssetAdapter;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.struct.EthAsset;
import com.rapsealk.digital_asset_liquidation.struct.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rapsealk on 2018. 5. 5..
 */
public class MyAssetActivity extends AppCompatActivity {

    private final String TAG = MyAssetActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    // private FirebaseDatabase mFirebaseDatabase;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_asset);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<EthAsset> items = new ArrayList<>();
        EthAssetAdapter adapter = new EthAssetAdapter(this, items);
        recyclerView.setAdapter(adapter);
        /*
        AnimatedRecyclerView animatedRecyclerView = (AnimatedRecyclerView) findViewById(R.id.animated_recycler_view);
        animatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Asset> items = new ArrayList<>();
        AssetAdapter adapter = new AssetAdapter(this, items);
        animatedRecyclerView.setAdapter(adapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        setProgressBarVisibility(ProgressBar.VISIBLE);
        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET)
                .orderByChild("orderKey")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clearItems();
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Asset asset = child.getValue(Asset.class);
                            try {
                                if (!asset.owner.equals(mCurrentUser.getUid())) continue;
                                Log.d(TAG, "asset: " + asset.imageUrl);
                                adapter.addItem(asset);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        setProgressBarVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.toString());
                        setProgressBarVisibility(ProgressBar.GONE);
                    }
                });
        */

        RetrofitManager retrofit = RetrofitManager.instance.create(RetrofitManager.class);
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        User user = sharedPreferenceManager.getUser();

        setProgressBarVisibility(ProgressBar.VISIBLE);
        Disposable d = retrofit.getAssetsOf(user.getAddress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(assetsResponse -> {
                    for (EthAsset asset: assetsResponse.getAssets()) {
                        Log.d(TAG, "==========================================");
                        Log.d(TAG, "ID: " + asset.getId());
                        Log.d(TAG, "Owner: " + asset.getOwner());
                        Log.d(TAG, "Price: " + asset.getPrice());
                        Log.d(TAG, "Total: " + asset.getTotalShare());
                        Log.d(TAG, "Owning: " + asset.getOwningShare());
                        Log.d(TAG, "Buyable: " + asset.getBuyableShare());
                        Log.d(TAG, "==========================================");
                    }
                    adapter.addAll(assetsResponse.getAssets());
                    adapter.notifyDataSetChanged();
                    setProgressBarVisibility(ProgressBar.GONE);
                });
    }

    private void setProgressBarVisibility(int visibility) {
        switch (visibility) {
            case ProgressBar.VISIBLE: {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                break;
            }
            case ProgressBar.GONE: {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        mProgressBar.setVisibility(visibility);
    }
}
