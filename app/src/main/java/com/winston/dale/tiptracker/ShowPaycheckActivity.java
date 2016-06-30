package com.winston.dale.tiptracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowPaycheckActivity extends AppCompatActivity {

    Button takePictureBtn;
    ImageView checkImage;
    String mCurrentPhotoPath;
    int week;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_paycheck);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Paycheck");
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Start the Add Tip Activity
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        takePictureBtn = (Button) findViewById(R.id.take_photo_btn);
        checkImage = (ImageView) findViewById(R.id.paycheck_image);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Intent thisIntent = getIntent();
        if (thisIntent != null & thisIntent.getExtras() != null) {
            week = thisIntent.getExtras().getInt("weekInt");
        }


    }

    public void onWindowFocusChanged(boolean hasFocus) {
        File theImage = new File(Environment.getExternalStorageDirectory() + File.separator + "image" + week + ".jpg");
        if (theImage.exists()) {
            Bitmap bitmap = decodeSampledBitmapFromFile(theImage.getAbsolutePath());
            checkImage.setImageBitmap(bitmap);
            takePictureBtn.setText("RETAKE PICTURE");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "image" + week + ".jpg");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));

        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image" + week + ".jpg");
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath());
            checkImage.setImageBitmap(bitmap);
        }
    }


    public Bitmap decodeSampledBitmapFromFile(String path)
    { // BEST QUALITY MATCH

        // Get the dimensions of the View
        int targetW = checkImage.getWidth();
        int targetH = checkImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return  bitmap;
    }
}
