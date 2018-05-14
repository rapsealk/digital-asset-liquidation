package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rapsealk.digital_asset_liquidation.schema.User;

import io.realm.Realm;

public class LoginActivity extends RealmAppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(view -> {
            setProgressBarVisibility(ProgressBar.VISIBLE);
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(intent, GlobalVariable.REQUEST_CODE_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalVariable.REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                setProgressBarVisibility(ProgressBar.GONE);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    setProgressBarVisibility(ProgressBar.GONE);
                    if (task.isSuccessful()) {
                        String uid = mFirebaseAuth.getCurrentUser().getUid();
                        updateLocalDatabase(uid);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
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

    private void updateLocalDatabase(String uid) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        User user = realm.where(User.class)
                .equalTo("uid", uid)
                .findFirst();
        if (user == null) {
            user = realm.createObject(User.class)
                    .setUid(uid);
        }
        realm.commitTransaction();
    }
}
