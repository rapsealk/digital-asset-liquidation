package com.rapsealk.digital_asset_liquidation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = RegisterActivity.class.getSimpleName();

    private Spinner majorSpinner;
    private Spinner minorSpinner;
    private ImageView assetImage;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        majorSpinner = (Spinner) findViewById(R.id.spn_major);
        minorSpinner = (Spinner) findViewById(R.id.spn_minor);
        assetImage = (ImageView) findViewById(R.id.iv_asset);
        registerButton = (Button) findViewById(R.id.btn_register);

        ArrayAdapter majorAdapter = ArrayAdapter.createFromResource(this, R.array.major_key, R.layout.support_simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);

        final int[] minorItems = { R.array.minor_key_real_estate, R.array.minor_key_art, R.array.minor_key_antique };

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter minorAdapter = ArrayAdapter.createFromResource(RegisterActivity.this, minorItems[position], R.layout.support_simple_spinner_dropdown_item);
                minorSpinner.setAdapter(minorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO("not implemented")
            }
        });

        assetImage.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle(R.string.title_image_asset_source)
                    .setItems(R.array.array_image_source, (dialog, which) -> {
                        switch (which) {
                            case 0: {   // camera
                                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[] { Manifest.permission.CAMERA }, GlobalVariable.REQUEST_CODE_CAMERA);
                                    return;
                                }
                                takePhotoWithCamera();
                                break;
                            }
                            case 1: {   // gallery
                                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, GlobalVariable.REQUEST_CODE_GALLERY);
                                    return;
                                }
                                readImageFromGallery();
                                break;
                            }
                        }
                    })
                    .create();
            alertDialog.show();
        });

        registerButton.setOnClickListener(view -> {
            String majorCategory = (String) majorSpinner.getSelectedItem();
            String minorCategory = (String) minorSpinner.getSelectedItem();
            Log.d(TAG, String.format("Major: %s, Minor: %s", majorCategory, minorCategory));
            Toast.makeText(RegisterActivity.this, "Register button clicked.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            Log.d(TAG, "onActivityResult/Result Code: " + resultCode);
            Toast.makeText(this, "Result Code: " + resultCode, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case GlobalVariable.IMAGE_CAPTURE: {
                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    assetImage.setImageBitmap(thumbnail);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            }
            case GlobalVariable.READ_STORAGE: {
                loadImageOnView(data);
                break;
            }
        }
    }

    private void takePhotoWithCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, GlobalVariable.IMAGE_CAPTURE);
    }

    private void readImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK)
                .setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType("image/*");
        String title = getResources().getString(R.string.title_image_asset_source);
        startActivityForResult(Intent.createChooser(intent, title), GlobalVariable.READ_STORAGE);
    }

    private void loadImageOnView(Intent intent) {
        Uri imageUri = intent.getData();
        String imagePath = uriToPath(imageUri);
        if (imagePath == null) return;

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90: exifDegree = 90; break;
            case ExifInterface.ORIENTATION_ROTATE_180: exifDegree = 180; break;
            case ExifInterface.ORIENTATION_ROTATE_270: exifDegree = 270; break;
            default: exifDegree = 0;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if (exifDegree != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(exifDegree, ((float) bitmap.getWidth()) / 2, ((float) bitmap.getHeight()) / 2);
            try {
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != rotatedBitmap) {
                    bitmap.recycle();
                    bitmap = rotatedBitmap;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        assetImage.setImageBitmap(bitmap);
    }

    private String uriToPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        String path = null;
        try {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
            cursor.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }
}
