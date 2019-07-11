package com.walter.captureimagesave;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    int REQUEST_CAPTURE_IMAGE=1000;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imgImage);
    }

    public void capture(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_CAPTURE_IMAGE);
        } else {
            Intent pictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
            if(pictureIntent.resolveActivity(getPackageManager()) != null){
                //Create a file to store the image
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
                if (photoFile != null) {
                   // Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.provider", photoFile);
                    Uri photoURI = FileProvider.getUriForFile(this, "com.walter.captureimagesave.provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,   photoURI);
                    startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&    resultCode == RESULT_OK) {

            Log.d("IMAGE_CAPTURED", "onActivityResult: "+imageFilePath);

            /*if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
               // mImageView.setImageBitmap(imageBitmap);
            }*/
            Glide.with(getApplicationContext()).load(imageFilePath).into(imageView);
        }

    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =System.currentTimeMillis()+"";
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",       storageDir     );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


}
