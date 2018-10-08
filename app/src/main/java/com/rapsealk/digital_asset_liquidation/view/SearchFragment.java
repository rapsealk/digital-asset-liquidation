package com.rapsealk.digital_asset_liquidation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.AssetAdapter;
import com.rapsealk.digital_asset_liquidation.GlobalVariable;
import com.rapsealk.digital_asset_liquidation.MainActivity;
import com.rapsealk.digital_asset_liquidation.R;
import com.rapsealk.digital_asset_liquidation.struct.Asset;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    private FirebaseDatabase mFirebaseDatabase;

    private Spinner spinnerMajor;
    private Spinner spinnerMinor;
    private Button searchButton;
    private RecyclerView recyclerView;

    private AssetAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerMajor = (Spinner) view.findViewById(R.id.spn_major);
        ArrayAdapter majorAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.major_key, R.layout.support_simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);

        spinnerMinor = (Spinner) view.findViewById(R.id.spn_minor);
        final int[] minorItems = { R.array.minor_key_real_estate, R.array.minor_key_art, R.array.minor_key_antique };
        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter minorAdapter = ArrayAdapter.createFromResource(view.getContext(), minorItems[position], R.layout.support_simple_spinner_dropdown_item);
                spinnerMinor.setAdapter(minorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO("not implemented")
            }
        });

        searchButton = (Button) view.findViewById(R.id.btn_search);
        searchButton.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ArrayList<Asset> assets = new ArrayList<>();
        mAdapter = new AssetAdapter(view.getContext(), assets);
        recyclerView.setAdapter(mAdapter);
    }

    // ValueEventListener
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String majorCategory = (String) spinnerMajor.getSelectedItem();
        String minorCategory = (String) spinnerMinor.getSelectedItem();
        mAdapter.clearItems();
        for (DataSnapshot child: dataSnapshot.getChildren()) {
            Asset asset = child.getValue(Asset.class);
            if (!asset.category.major.equals(majorCategory) || !asset.category.minor.equals(minorCategory))
                continue;
            mAdapter.addItem(asset);
        }
        mAdapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).setProgressBarVisible(false);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        ((MainActivity) getActivity()).setProgressBarVisible(false);
    }

    // View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                updateAssetListView();
                break;
            default:
        }
    }

    private void updateAssetListView() {
        ((MainActivity) getActivity()).setProgressBarVisible(true);
        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET)
                // .equalTo("category/major", majorCategory)
                // .equalTo("category/minor", minorCategory)
                .orderByChild("orderKey")
                .addListenerForSingleValueEvent(this);
    }
}