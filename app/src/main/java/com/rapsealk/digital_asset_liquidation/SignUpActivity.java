package com.rapsealk.digital_asset_liquidation;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/DD", Locale.KOREA);
        String[] date = dateFormat.format(System.currentTimeMillis()).split("/");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, this,
                Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));

        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
