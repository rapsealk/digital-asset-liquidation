package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rapsealk.digital_asset_liquidation.adapter.ViewPagerAdapter;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.network.body.AddressBody;
import com.rapsealk.digital_asset_liquidation.struct.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;
import com.rapsealk.digital_asset_liquidation.view.SwipableViewPager;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser mFirebaseUser;
    private User mUser;

    RetrofitManager retrofit;
    SharedPreferenceManager sharedPreferenceManager;

    private SwipableViewPager mViewPager;

    private ProgressBar mProgressBar;
    private TextView tvAddress;
    private TextView tvBalance;

    private DrawerLayout mDrawerLayout;

    private TextView mNavigationAddress;

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
            // setProgressBarVisible(true);
            mUser = sharedPreferenceManager.getUser();
            tvAddress.setText(mUser.getAddress());

            mNavigationAddress.setText(mUser.getAddress());

            Disposable api = retrofit.balanceOf(mUser.getAddress())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(balanceResponse -> {
                        tvBalance.setText(String.format(Locale.KOREA, "%d", balanceResponse.getBalance()));
                        updateAppIconBadge(balanceResponse.getBalance());
                        // setProgressBarVisible(false);
                    });
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        // mNavigationView.setItemIconTintList(null);

        // TabLayout (https://coding-factory.tistory.com/206)
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final String[] tabItems = { "Main", "Search", "Settings" };
        for (String item: tabItems) {
            tabLayout.addTab(tabLayout.newTab().setText(item));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ViewPager
        mViewPager = (SwipableViewPager) findViewById(R.id.view_pager);
        mViewPager.setPagingEnabled(false);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 3);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);

        /* BottomNavigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        */

        mNavigationAddress = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_nav_address);

        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        ImageView ivToken = (ImageView) findViewById(R.id.iv_token);

        // FloatingActionButton fabRegister = (FloatingActionButton) findViewById(R.id.fab_register);
        // FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);

        // utilities
        retrofit = RetrofitManager.instance.create(RetrofitManager.class);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        ivToken.setOnClickListener(view -> {
            if (mUser == null) return;
            setProgressBarVisible(true);
            Disposable api = retrofit.getAirdrop(new AddressBody(mUser.getAddress()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> {
                        tvBalance.setText(String.format(Locale.KOREA, "%d", response.getBalance()));
                        updateAppIconBadge(response.getBalance());
                        setProgressBarVisible(false);
                    }, Throwable::printStackTrace);
        });

        /*
        fabRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        */

        /*
        fabSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        */
    }

    public void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    // NavigationView.OnNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            // NavigationView
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
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // TabLayout.OnTabSelectedListener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    // https://medium.com/marojuns-android/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%95%84%EC%9D%B4%EC%BD%98-%EB%B1%83%EC%A7%80-%EC%99%84%EC%A0%84%EB%B6%84%ED%95%B4-c27028014e4d
    private void updateAppIconBadge(int count) {
        /*
        Intent intent = new Intent("android.intent.action.BADGE_COUND_UPDATE");
        intent.putExtra("badge_count_package_name", getComponentName().getPackageName());
        intent.putExtra("badge_count_class_name", getComponentName().getClassName());
        intent.putExtra("badge_count", count);
        sendBroadcast(intent);
        */
    }
}
