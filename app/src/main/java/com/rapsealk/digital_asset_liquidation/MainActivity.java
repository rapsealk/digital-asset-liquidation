package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.rapsealk.digital_asset_liquidation.schema.User;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MainActivity extends RealmAppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mFirebaseDatabase;

    private Realm realm;

    private ProgressBar progressBar;
    // private Button mBtnKeyGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Log.d(TAG, "uid: " + mCurrentUser.getUid());

        /*
        RetrofitManager retrofit = RetrofitManager.instance.create(RetrofitManager.class);

        mCurrentUser.getIdToken(true)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        Disposable disposable = retrofit.getUser(token)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(result -> {
                                    String message = result.getMessage();
                                    Log.d(TAG, "message: " + message);
                                }, error -> {
                                    error.printStackTrace();
                                });
                    }
                });
        */

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        realm = Realm.getDefaultInstance();

        // Check whether permissions are granted
        // FIXME("Rx.all") ================================================================================
        int grantedPermissions = 0;
        for (String permission : GlobalVariable.PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
                grantedPermissions += 1;
        }
        if (grantedPermissions == 0) {
            Intent intent = new Intent(MainActivity.this, PermissionActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // ================================================================================================
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        FloatingActionButton fabRegister = (FloatingActionButton) findViewById(R.id.fab_register);
        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        // mBtnKeyGen = (Button) findViewById(R.id.btn_keygen);
        // Button btnRegister = (Button) findViewById(R.id.btn_register);
        Button btnHistory = (Button) findViewById(R.id.btn_history);
        // Button btnSearch = (Button) findViewById(R.id.btn_search);

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

        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET)
                .orderByChild("orderKey")
                .limitToFirst(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Asset> assets = new ArrayList<>();
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Asset asset = child.getValue(Asset.class);
                            if (asset == null) continue;
                            assets.add(asset);
                        }
                        initCarouselView(cvMyAssets, assets);
                        initCarouselView(cvNewAssets, assets);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });

        User user = realm.where(User.class)
                .equalTo("uid", mCurrentUser.getUid())
                .findFirst();
        /*
        if (user != null) {
            mBtnKeyGen.setText(user.getPublicKey());
        } else {
            mFirebaseDatabase.getReference(GlobalVariable.DATABASE_USERS).child(mCurrentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            realm.beginTransaction();
                            User _user = realm.createObject(User.class);
                            _user.copy(user);
                            realm.commitTransaction();
                            // mBtnKeyGen.setText(user.getPublicKey());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // TODO("not implemented")
                        }
                    });
        }
        */

        /* TODO("too much work on main thread")
        mBtnKeyGen.setOnClickListener(view -> {
            new KeyGenTask().execute();
        });
        */

        fabRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        /*
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            // TODO("update recent assets")
            startActivity(intent);
        });
        */

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyAssetActivity.class);
            startActivity(intent);
        });

        fabSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        /*
        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
        */
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

    /* TODO("memory leak")
    private class KeyGenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... args) {
            runOnUiThread(() -> setProgressBarVisibility(ProgressBar.VISIBLE));
            String hashMessage = "HASH MESSAGE";
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
                ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp521r1");
                keyPairGenerator.initialize(ecGenParameterSpec);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                PublicKey publicKey = keyPair.getPublic();
                Log.d(TAG, "Public Key: " + publicKey.toString() + ", length: " + publicKey.toString().length());
                PrivateKey privateKey = keyPair.getPrivate();
                Log.d(TAG, "Private Key: " + privateKey.toString() + ", length: " + privateKey.toString().length());
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // run 1 sec process
                String input = publicKey.toString();
                for (int i = 0; i < 10000; i++) {
                    messageDigest.reset();
                    messageDigest.update(input.getBytes());
                    byte[] digested = messageDigest.digest();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte message: digested) { stringBuilder.append(Integer.toString((message & 0xFF) + 0x100, 16).substring(1)); }
                    hashMessage = stringBuilder.toString();
                }
            } catch (Exception e) { // NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException
                e.printStackTrace();
            } finally {
                runOnUiThread(() -> setProgressBarVisibility(ProgressBar.GONE));
            }
            return hashMessage;
        }

        @Override
        protected void onPostExecute(String hashMessage) {
            Log.d(TAG, "SHA-256 Hash: " + hashMessage);
            updatePublicKey(hashMessage.substring(hashMessage.length()-20));
        }
    }
    */

    /*
    private void updatePublicKey(String publicKey) {
        runOnUiThread(() -> setProgressBarVisibility(ProgressBar.VISIBLE));
        HashMap<String, Object> update = new HashMap<>();
        update.put("public_key", publicKey);
        mFirebaseDatabase.getReference("users").child(mCurrentUser.getUid())
                .updateChildren(update)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Realm
                        realm.beginTransaction();
                        String uid = mCurrentUser.getUid();
                        User user = realm.where(User.class)
                                .equalTo("uid", uid)
                                .findFirst();
                        if (user == null) {
                            user = realm.createObject(User.class)
                                    .setUid(uid);
                        }
                        user.setPublicKey(publicKey);
                        realm.commitTransaction();
                        mBtnKeyGen.setText(publicKey);
                    }
                    runOnUiThread(() -> setProgressBarVisibility(ProgressBar.GONE));
                })
                .addOnFailureListener(this, e -> {
                    e.printStackTrace();
                    runOnUiThread(() -> setProgressBarVisibility(ProgressBar.GONE));
                });
    }
    */

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
