package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private Button mBtnKeyGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Log.d(TAG, "uid: " + user.getUid());

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

        mBtnKeyGen = (Button) findViewById(R.id.btn_keygen);
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        Button btnHistory = (Button) findViewById(R.id.btn_history);
        Button btnSearch = (Button) findViewById(R.id.btn_search);

        // TODO("too much work on main thread")
        mBtnKeyGen.setOnClickListener(view -> {
            new KeyGenTask().execute();
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyAssetActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
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

    // TODO("memory leak")
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
            mBtnKeyGen.setText(hashMessage.substring(hashMessage.length()-20));
        }
    }
}
