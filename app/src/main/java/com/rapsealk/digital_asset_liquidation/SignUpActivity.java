package com.rapsealk.digital_asset_liquidation;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.rapsealk.digital_asset_liquidation.schema.User;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private TextView tvBirthdate;
    private String birthdate;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        EditText etName = (EditText) findViewById(R.id.et_name);
        EditText etEmail = (EditText) findViewById(R.id.et_email);
        EditText etPassword = (EditText) findViewById(R.id.et_password);
        tvBirthdate = (TextView) findViewById(R.id.tv_birthdate);

        AppCompatButton btnSignup = (AppCompatButton) findViewById(R.id.btn_signup);
        TextView tvLogin = (TextView) findViewById(R.id.tv_login);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD", Locale.KOREA);
        birthdate = dateFormat.format(System.currentTimeMillis());

        tvBirthdate.setOnClickListener(view -> buildDatePickerDialog());

        btnSignup.setOnClickListener(view -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            /*
            Maybe.create(emitter -> RxHandler.assignOnTask(emitter, mFirebaseAuth.createUserWithEmailAndPassword(email, password)));

            Maybe.create(new MaybeOnSubscribe<AuthResult>() {
                @Override
                public void subscribe(MaybeEmitter<AuthResult> emitter) throws Exception {

                }
            });
            */

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        progressBar.setVisibility(ProgressBar.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (task.isSuccessful()) {

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(ProgressBar.VISIBLE);

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {

                                        progressBar.setVisibility(ProgressBar.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        if (updateTask.isSuccessful()) {

                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBar.setVisibility(ProgressBar.VISIBLE);

                                            mFirebaseDatabase.getReference(GlobalVariable.DATABASE_USERS).child(user.getUid())
                                                    .setValue(new User(user.getUid(), name, birthdate))
                                                    .addOnCompleteListener(dbTask -> {

                                                        progressBar.setVisibility(ProgressBar.GONE);
                                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                        if (dbTask.isSuccessful()) {
                                                            finish();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
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
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/DD", Locale.KOREA);
        // String[] date = dateFormat.format(System.currentTimeMillis()).split("/");
        String[] date = birthdate.split("-");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, this,
                Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));

        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.show();
    }
}
