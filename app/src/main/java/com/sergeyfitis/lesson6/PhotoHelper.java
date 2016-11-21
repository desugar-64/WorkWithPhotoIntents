package com.sergeyfitis.lesson6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

/**
 * Created by sergeyfitis on 21.11.16.
 */

public class PhotoHelper {
    private static final String TAG = "PhotoHelper";

    public static final int REQUEST_IMAGE_OPEN = 11;
    private static final int REQUEST_IMAGE_CAPTURE = 22;

    private Context ctx;
    @NonNull
    private OnPhotoPicked onPhotoPickedListener;


    private Uri filePath; // data/data/app/cache

    public PhotoHelper(Context ctx, @NonNull OnPhotoPicked onPhotoPickedListener) {
        this.ctx = ctx;
        this.onPhotoPickedListener = onPhotoPickedListener;

        Log.d(TAG, "PhotoHelper: ");
    }


    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {

            filePath = createFileUri();

            if (filePath != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                ActivityCompat.startActivityForResult(
                        (AppCompatActivity) ctx, intent, REQUEST_IMAGE_CAPTURE, null);
            }
        }
    }

    private Uri createFileUri() {
        File file = new File(ctx.getCacheDir(), "photo.jpg"); // data/data/package.name/cache/photo.jpg
        return FileProvider.getUriForFile(ctx, "com.sergeyfitis.lesson6.fileprovider", file);
    }

    public void pickPhoto() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickPhotoIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickPhotoIntent.setType("image/*");
        ActivityCompat.startActivityForResult(
                (AppCompatActivity) ctx, pickPhotoIntent,
                REQUEST_IMAGE_OPEN, null);
    }

    public void onActivityResult(int resultCode, int requestCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_OPEN) {
                Uri photoUri = data.getData();
                onPhotoPickedListener.onPicked(photoUri);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE){
                onPhotoPickedListener.onPicked(filePath);
            }
        }
    }


    interface OnPhotoPicked {
        void onPicked(Uri photoUri);
    }

}
