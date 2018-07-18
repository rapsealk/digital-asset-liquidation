package com.rapsealk.digital_asset_liquidation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rapsealk.digital_asset_liquidation.network.RetrofitManager;
import com.rapsealk.digital_asset_liquidation.network.body.RegisterAssetBody;
import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.rapsealk.digital_asset_liquidation.schema.AssetCategory;
import com.rapsealk.digital_asset_liquidation.schema.User;
import com.rapsealk.digital_asset_liquidation.util.SharedPreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = RegisterActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;

    private Spinner majorSpinner;
    private Spinner minorSpinner;
    private ImageView assetImage;
    private EditText assetName, buildDate, assetPrice;
    private Switch switchChain;
    private Button registerButton;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        RetrofitManager retrofit = RetrofitManager.instance.create(RetrofitManager.class);
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        majorSpinner = (Spinner) findViewById(R.id.spn_major);
        minorSpinner = (Spinner) findViewById(R.id.spn_minor);
        assetImage = (ImageView) findViewById(R.id.iv_asset);
        assetName = (EditText) findViewById(R.id.et_asset_name);
        buildDate = (EditText) findViewById(R.id.et_build_date);
        assetPrice = (EditText) findViewById(R.id.et_asset_price);
        switchChain = (Switch) findViewById(R.id.switch_chain);
        registerButton = (Button) findViewById(R.id.btn_register);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

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
            setProgressBarVisible(true);
            // TODO("clean")
            String majorCategory = (String) majorSpinner.getSelectedItem();
            String minorCategory = (String) minorSpinner.getSelectedItem();
            // String owner = mCurrentUser.getEmail();
            long timestamp = System.currentTimeMillis();
            Log.d(TAG, "====================================================================");
            Log.d(TAG, String.format("Major: %s, Minor: %s", majorCategory, minorCategory));
            Log.d(TAG, "Asset name: " + assetName.getText().toString());
            Log.d(TAG, "Asset build date: " + buildDate.getText().toString());
            Log.d(TAG, "Asset price: " + assetPrice.getText().toString());
            Log.d(TAG, "Asset on blockchain: " + switchChain.isChecked());
            Log.d(TAG, "====================================================================");
            // Toast.makeText(RegisterActivity.this, "Register button clicked.", Toast.LENGTH_SHORT).show();

            Bitmap bitmap = ((BitmapDrawable) assetImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final StorageReference ref = mFirebaseStorage.getReference(GlobalVariable.DATABASE_ASSET).child(mCurrentUser.getUid() + "/" + timestamp);
            ref.putBytes(baos.toByteArray())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        setProgressBarVisible(false);
                        return;
                    }
                    // String url = ref.getDownloadUrl().toString();   // task.getResult().getDownloadUrl().toString();
                    ref.getDownloadUrl().addOnCompleteListener(urlTask -> {
                        String url = urlTask.getResult().toString();
                        Log.d(TAG, "URL: " + url);
                        Asset asset = new Asset(mCurrentUser.getUid(), timestamp, url)
                                .setCategory(new AssetCategory(majorCategory, minorCategory))
                                .setName(assetName.getText().toString())
                                .setBuildDate(buildDate.getText().toString())
                                .setPrice(Integer.parseInt(assetPrice.getText().toString()))
                                .setOnChain(switchChain.isChecked());
                        mFirebaseDatabase.getReference(GlobalVariable.DATABASE_ASSET).child(String.valueOf(timestamp))
                            .setValue(asset)
                            .addOnCompleteListener(this, _task -> {
                                setProgressBarVisible(false);
                                if (!task.isSuccessful()) return;

                                User user = sharedPreferenceManager.getUser();
                                retrofit.registerAsset(new RegisterAssetBody(user.getAddress(), timestamp))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(result -> {
                                            if (!result.isSucceed()) return;
                                            Toast.makeText(this, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        });
                            });
                    });
                });
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

    // TODO("customize progress bar")
    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }
}
