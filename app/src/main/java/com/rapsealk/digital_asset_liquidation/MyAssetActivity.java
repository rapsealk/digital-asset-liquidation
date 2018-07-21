package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.rapsealk.digital_asset_liquidation.view.AnimatedRecyclerView;

import java.util.ArrayList;

/**
 * Created by rapsealk on 2018. 5. 5..
 */
public class MyAssetActivity extends AppCompatActivity {

    private final String TAG = MyAssetActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mFirebaseDatabase;

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
