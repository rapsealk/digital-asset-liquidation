package com.rapsealk.digital_asset_liquidation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.rapsealk.digital_asset_liquidation.view.AnimatedRecyclerView;

import java.util.ArrayList;

/**
 * Created by rapsealk on 2018. 5. 6..
 */
public class SearchActivity extends AppCompatActivity {

    private final String TAG = SearchActivity.class.getSimpleName();

    // TODO("singleton")
    private FirebaseDatabase mFirebaseDatabase;

    private Spinner majorSpinner;
    private Spinner minorSpinner;
    private Button searchButton;
    private AnimatedRecyclerView animatedRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        majorSpinner = (Spinner) findViewById(R.id.spn_major);
        minorSpinner = (Spinner) findViewById(R.id.spn_minor);
        searchButton = (Button) findViewById(R.id.btn_search);
        animatedRecyclerView = (AnimatedRecyclerView) findViewById(R.id.animated_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        animatedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Asset> items = new ArrayList<>();
        AssetAdapter adapter = new AssetAdapter(this, items);
        animatedRecyclerView.setAdapter(adapter);

        ArrayAdapter majorAdapter = ArrayAdapter.createFromResource(this, R.array.major_key, R.layout.support_simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);

        final int[] minorItems = { R.array.minor_key_real_estate, R.array.minor_key_art, R.array.minor_key_antique };

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter minorAdapter = ArrayAdapter.createFromResource(SearchActivity.this, minorItems[position], R.layout.support_simple_spinner_dropdown_item);
                minorSpinner.setAdapter(minorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO("not implemented")
            }
        });

        searchButton.setOnClickListener(view -> {
            setProgressBarVisibility(ProgressBar.VISIBLE);
            String majorCategory = (String) majorSpinner.getSelectedItem();
            String minorCategory = (String) minorSpinner.getSelectedItem();
            mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET)
                    // .equalTo("category/major", majorCategory)
                    // .equalTo("category/minor", minorCategory)
                    .orderByChild("orderKey")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            adapter.clearItems();
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                Asset asset = child.getValue(Asset.class);
                                if (!asset.category.major.equals(majorCategory) || !asset.category.minor.equals(minorCategory))
                                    continue;
                                Log.d(TAG, "asset: " + asset.imageUrl);
                                adapter.addItem(asset);
                            }
                            adapter.notifyDataSetChanged();
                            setProgressBarVisibility(ProgressBar.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            setProgressBarVisibility(ProgressBar.GONE);
                        }
                    });
        });
    }

    // TODO("customize progress bar")
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
        progressBar.setVisibility(visibility);
    }
}
