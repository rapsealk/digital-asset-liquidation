package com.rapsealk.digital_asset_liquidation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.GlobalVariable;
import com.rapsealk.digital_asset_liquidation.R;
import com.rapsealk.digital_asset_liquidation.adapter.CardPagerAdapter;
import com.rapsealk.digital_asset_liquidation.struct.Asset;

import java.util.ArrayList;

public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private FirebaseDatabase mFirebaseDatabase;

    private ViewPager mPreviewPager;

    private int mPreviewPagerPosition = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewPager: Asset preview
        mPreviewPager = (ViewPager) view.findViewById(R.id.view_pager_preview);
        final CardPagerAdapter adapter = new CardPagerAdapter(getContext());
        mPreviewPager.setAdapter(adapter);
        mPreviewPager.setOffscreenPageLimit(3); // FIXME: MAX PREVIEW ITEMS SIZE
        mPreviewPager.addOnPageChangeListener(this);

        // new LoadPreviewTask(firebaseDatabase).execute(GlobalVariable.DATABASE_ASSET);
        loadAssetsPreview(mPreviewPager, adapter);
    }

    // ViewPager.OnPageChangeListener
    @Override
    public void onPageSelected(int position) {
        mPreviewPagerPosition = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPreviewPagerPosition == 1) mPreviewPager.setCurrentItem(4, false);
        else if (mPreviewPagerPosition == 5) mPreviewPager.setCurrentItem(2, false);
    }

    private void loadAssetsPreview(ViewPager viewPager, CardPagerAdapter adapter) {
        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET)
                .orderByChild("orderKey")
                .limitToFirst(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Asset> assets = new ArrayList<>();
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Asset asset = child.getValue(Asset.class);
                            if (asset != null) assets.add(asset);
                        }
                        // FIXME a better way
                        int idx = 1;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Asset asset = child.getValue(Asset.class);
                            if (idx == 1) assets.add(asset);
                            else if (idx == 2) {
                                assets.add(asset);
                                assets.add(0, asset);
                            }
                            else if (idx == 3) assets.add(1, asset);
                            idx += 1;
                        }
                        adapter.addItems(assets);
                        viewPager.getAdapter().notifyDataSetChanged();
                        // viewPager.setCurrentItem(1);
                        viewPager.setCurrentItem(2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });
    }

    /*
     * Inner Classes
     */
    /*
    private class LoadPreviewTask extends AsyncTask<String, Void, List<Asset>> implements ValueEventListener {

        private FirebaseDatabase firebaseDatabase;

        public LoadPreviewTask(FirebaseDatabase database) {
            super();
            this.firebaseDatabase = database;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Progress On

        }

        @Override
        protected List<Asset> doInBackground(String... pathlist) {
            String path = pathlist[0];
            firebaseDatabase.getReference(path)
                    .orderByChild("orderKey")
                    .limitToFirst(3)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Asset> assets) {
            super.onPostExecute(assets);
            // Progress Off
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(List<Asset> assets) {
            super.onCancelled(assets);
        }

        // ValueEventListener
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
    */
}