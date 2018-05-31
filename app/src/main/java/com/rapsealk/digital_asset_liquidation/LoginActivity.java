package com.rapsealk.digital_asset_liquidation;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.network.body.IdAndPasswordBody;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends RealmAppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etId = (EditText) findViewById(R.id.et_id);
        EditText etPassword = (EditText) findViewById(R.id.et_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        TextView tvRegister = (TextView) findViewById(R.id.tv_register);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        RetrofitManager retrofit = RetrofitManager.instance.create(RetrofitManager.class);

        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        btnLogin.setOnClickListener(view -> {
            String id = etId.getText().toString();
            String password = etPassword.getText().toString();

            if (id.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Disposable disposable = retrofit.signIn(new IdAndPasswordBody(id, password))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(result -> {
                        if (result.isSucceed()) {
                            String token = result.getToken();
                            Log.d(TAG, "Token: " + token);
                            sharedPreferenceManager.setAuthToken(token);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, Throwable::printStackTrace);
        });

        tvRegister.setOnClickListener(view -> {
            Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
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

    /*
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
    */
}
