package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.adapter.CardPagerAdapter;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.network.body.AddressBody;
import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.rapsealk.digital_asset_liquidation.struct.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseUser mFirebaseUser;
    private User mUser;

    RetrofitManager retrofit;
    SharedPreferenceManager sharedPreferenceManager;

    private ProgressBar progressBar;
    private TextView tvEmail;
    private TextView tvAddress;
    private TextView tvBalance;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private TextView mNavigationAddress;

    private int mViewPagerCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether permissions are granted
        // FIXME("Rx.all") ================================================================================
        int grantedPermissions = 0;
        for (String permission : GlobalVariable.PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
                grantedPermissions += 1;
        }
        if (grantedPermissions == 0) {
            Intent intent = new Intent(this, PermissionActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // ================================================================================================

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        if (mFirebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            setProgressBarVisible(true);
            mUser = sharedPreferenceManager.getUser();
            Log.d(TAG, "user: " + mUser);
            tvEmail.setText(mFirebaseUser.getEmail());
            tvAddress.setText(mUser.getAddress());

            mNavigationAddress.setText(mUser.getAddress());

            tvEmail.setOnClickListener(view -> {
                mFirebaseAuth.signOut();
            });

            retrofit.balanceOf(mUser.getAddress())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(balanceResponse -> {
                        tvBalance.setText(String.format(Locale.KOREA, "%d", balanceResponse.getBalance()));
                        setProgressBarVisible(false);
                    });
        } else {
            tvEmail.setOnClickListener(null);
        }
    }

    /*
    private void updateAccountAddress(Account account) {
        mTabTitles[0] = account.getAddress();
        synchronized (mDrawerList.getAdapter()) {
            mDrawerList.getAdapter().notify();
        }
    }
    */

    private void initLayout() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        // mNavigationView.setItemIconTintList(null);

        mNavigationAddress = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_nav_address);

        tvEmail = (TextView) findViewById(R.id.tv_user_email);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        ImageView ivToken = (ImageView) findViewById(R.id.iv_token);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // ViewPager with CardView
        // TODO("https://rubensousa.github.io/2016/08/viewpagercards")
        // ViewPager with Circular scroll
        // |B|C|<A|B|C>|A|B|
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_preview);
        CardPagerAdapter cardAdapter = new CardPagerAdapter(this);
        viewPager.setAdapter(cardAdapter);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO
            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // if (mViewPagerCurrentPosition == 0) viewPager.setCurrentItem(3, false);
                // else if (mViewPagerCurrentPosition == 4) viewPager.setCurrentItem(1, false);
                if (mViewPagerCurrentPosition == 1) viewPager.setCurrentItem(4, false);
                else if (mViewPagerCurrentPosition == 5) viewPager.setCurrentItem(2, false);
            }
        });

        // FloatingActionButton fabRegister = (FloatingActionButton) findViewById(R.id.fab_register);
        // FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        Button btnHistory = (Button) findViewById(R.id.btn_history);

        CarouselView cvNewAssets = (CarouselView) findViewById(R.id.carousel_new_assets);

        cvNewAssets.setPageCount(1);
        cvNewAssets.setImageListener(((position, imageView) -> {
            imageView.setColorFilter(getResources().getColor(R.color.cardview_dark_background));
        }));

        // utilities
        retrofit = RetrofitManager.instance.create(RetrofitManager.class);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        ivToken.setOnClickListener(view -> {
            if (mUser == null) return;
            setProgressBarVisible(true);
            retrofit.getAirdrop(new AddressBody(mUser.getAddress()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> {
                        tvBalance.setText(String.format(Locale.KOREA, "%d", response.getBalance()));
                        setProgressBarVisible(false);
                    }, Throwable::printStackTrace);
        });

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
                        cardAdapter.addItems(assets);
                        viewPager.getAdapter().notifyDataSetChanged();
                        // viewPager.setCurrentItem(1);
                        viewPager.setCurrentItem(2);
                        initCarouselView(cvNewAssets, assets.subList(2, 5));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });

        /*
        fabRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        */

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyAssetActivity.class);
            startActivity(intent);
        });

        /*
        fabSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        */
    }

    // TODO("customize progress bar")
    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private void initCarouselView(CarouselView view, List<Asset> assets) {
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Asset asset = assets.get(position);
                Picasso.get()
                        .load(asset.imageUrl)
                        .placeholder(R.color.cardview_dark_background)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }
        };
        ImageClickListener imageClickListener = new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Asset asset = assets.get(position);
                Toast.makeText(MainActivity.this, "Asset: " + asset.name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AssetActivity.class)
                        .putExtra("asset", asset);
                startActivity(intent);
            }
        };
        view.setImageListener(imageListener);
        view.setImageClickListener(imageClickListener);
        view.setPageCount(assets.size());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.nav_item_my_assets:
                intent.setClass(this, MyAssetActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item_register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item_search:
                intent.setClass(this, SearchActivity.class);
                startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
