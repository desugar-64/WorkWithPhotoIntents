package com.sergeyfitis.lesson6;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private PhotoHelper photoHelper;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoHelper = new PhotoHelper(this, new PhotoHelper.OnPhotoPicked() {
            @Override
            public void onPicked(Uri photoUri) {
                final ProcessPhotoAsyncTask photoAsyncTask =
                        new ProcessPhotoAsyncTask(MainActivity.this, listener);
                photoAsyncTask.execute(photoUri);
            }
        });
        photoHelper.takePhoto();

        photo = (ImageView) findViewById(R.id.ivPhoto);

    }

    private final ProcessPhotoAsyncTask.OnPhotoProcessed listener = new ProcessPhotoAsyncTask.OnPhotoProcessed() {
        @Override
        public void onBitmapReady(@Nullable Bitmap bitmap) {
            photo.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelper.onActivityResult(resultCode, requestCode, data);
    }
}
