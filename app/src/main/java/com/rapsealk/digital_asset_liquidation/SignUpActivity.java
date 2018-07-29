package com.rapsealk.digital_asset_liquidation;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.struct.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private RetrofitManager retrofit;
    private SharedPreferenceManager sharedPreferenceManager;

    private TextView tvBirthdate;
    private String birthdate;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        retrofit = RetrofitManager.instance.create(RetrofitManager.class);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        EditText etName = (EditText) findViewById(R.id.et_name);
        EditText etEmail = (EditText) findViewById(R.id.et_email);
        EditText etPassword = (EditText) findViewById(R.id.et_password);
        tvBirthdate = (TextView) findViewById(R.id.tv_birthdate);

        CheckBox cbAdmin = (CheckBox) findViewById(R.id.cb_is_admin);

        AppCompatButton btnSignup = (AppCompatButton) findViewById(R.id.btn_signup);
        TextView tvLogin = (TextView) findViewById(R.id.tv_login);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        birthdate = dateFormat.format(System.currentTimeMillis());
        tvBirthdate.setText(birthdate);

        tvBirthdate.setOnClickListener(view -> buildDatePickerDialog());

        btnSignup.setOnClickListener(view -> {

            setProgressBarVisible(true);

            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setProgressBarVisible(false);

                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Unable to createUserWithEmailAndPassword.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setProgressBarVisible(true);

                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                    firebaseUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(updateTask -> {

                            setProgressBarVisible(false);

                            if (!updateTask.isSuccessful()) {
                                Toast.makeText(this, "Unable to updateProfile.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            setProgressBarVisible(true);

                            retrofit.createAccount()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(response -> {
                                        String address = response.getAddress();
                                        User user = new User(firebaseUser.getUid(), name, birthdate, address, cbAdmin.isChecked());
                                        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_USERS).child(user.getUid())
                                                .setValue(user)
                                                .addOnCompleteListener(dbTask -> {

                                                    setProgressBarVisible(false);

                                                    if (!dbTask.isSuccessful()) {
                                                        Toast.makeText(this, "Unable to setValue on Firebase Database.", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    sharedPreferenceManager.setUser(user);

                                                    setResult(RESULT_OK);
                                                    finish();
                                                });
                                        // }, Throwable::printStackTrace);
                                    }, throwable -> {
                                        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        throwable.printStackTrace();
                                    });
                        });
                });


            /*
            Maybe.create(emitter -> RxHandler.assignOnTask(emitter, mFirebaseAuth.createUserWithEmailAndPassword(email, password)));

            Maybe.create(new MaybeOnSubscribe<AuthResult>() {
                @Override
                public void subscribe(MaybeEmitter<AuthResult> emitter) throws Exception {

                }
            });
            */
        });

        tvLogin.setOnClickListener(view -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        birthdate = String.format(Locale.KOREA, "%d-%d-%d", year, month+1, dayOfMonth);
        tvBirthdate.setText(birthdate);
    }

    private void buildDatePickerDialog() {
        String[] date = birthdate.split("-");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, this,
                Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));

        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.show();
    }

    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            progressBar.setVisibility(ProgressBar.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
