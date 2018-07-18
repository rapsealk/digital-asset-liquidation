package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.network.body.AddressBody;
import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.rapsealk.digital_asset_liquidation.schema.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseUser mFirebaseUser;
    private User mUser;

    RetrofitManager retrofit;
    SharedPreferenceManager sharedPreferenceManager;

    final private String[] mTabTitles = { "Tab#1", "Tab#2", "Tab#3" };
    // private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ProgressBar progressBar;
    private TextView tvEmail;
    private TextView tvAddress;
    private TextView tvBalance;
    private ConstraintLayout mBlockScreen;
    private ImageView ivAlert;
    private Button btnLogin;

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

        // mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mTabTitles));
        mDrawerList.setOnItemClickListener((parent, view, position, id) -> {

        });

        tvEmail = (TextView) findViewById(R.id.tv_user_email);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        ImageView ivToken = (ImageView) findViewById(R.id.iv_token);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        FloatingActionButton fabRegister = (FloatingActionButton) findViewById(R.id.fab_register);
        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        Button btnHistory = (Button) findViewById(R.id.btn_history);

        CarouselView cvMyAssets = (CarouselView) findViewById(R.id.carousel_my_assets);
        CarouselView cvNewAssets = (CarouselView) findViewById(R.id.carousel_new_assets);

        cvMyAssets.setPageCount(1);
        cvMyAssets.setImageListener(((position, imageView) -> {
            imageView.setColorFilter(getResources().getColor(R.color.cardview_dark_background));
        }));

        cvNewAssets.setPageCount(1);
        cvNewAssets.setImageListener(((position, imageView) -> {
            imageView.setColorFilter(getResources().getColor(R.color.cardview_dark_background));
        }));

        // ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_new_assets);

        mBlockScreen = (ConstraintLayout) findViewById(R.id.block_screen);
        ivAlert = (ImageView) findViewById(R.id.iv_alert);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, GlobalVariable.REQUEST_CODE_SIGN_IN);
        });

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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // TODO("Update Address");
                }
            }
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
                    initCarouselView(cvMyAssets, assets);
                    initCarouselView(cvNewAssets, assets);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });

        fabRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyAssetActivity.class);
            startActivity(intent);
        });

        fabSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            setProgressBarVisible(true);
            mUser = sharedPreferenceManager.getUser();
            Log.d(TAG, "user: " + mUser);
            mBlockScreen.setVisibility(ConstraintLayout.GONE);
            tvEmail.setText(mFirebaseUser.getEmail());
            tvAddress.setText(mUser.getAddress());

            retrofit.balanceOf(mUser.getAddress())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(balanceResponse -> {
                        tvBalance.setText(String.format(Locale.KOREA, "%d", balanceResponse.getBalance()));
                        setProgressBarVisible(false);
                    });
        } else {
            mBlockScreen.setVisibility(ConstraintLayout.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalVariable.REQUEST_CODE_SIGN_IN: {
                Log.d(TAG, "REQUEST_CODE_SIGN_IN: " + (resultCode == RESULT_OK));
                if (resultCode == RESULT_OK) {
                    ivAlert.setVisibility(ImageView.GONE);
                    btnLogin.setVisibility(Button.GONE);
                }
            }
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

    private void initCarouselView(CarouselView view, ArrayList<Asset> assets) {
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
}
