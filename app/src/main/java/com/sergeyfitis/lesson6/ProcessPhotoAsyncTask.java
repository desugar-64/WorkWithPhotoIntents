package com.sergeyfitis.lesson6;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by sergeyfitis on 21.11.16.
 */

public class ProcessPhotoAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

    private WeakReference<Context> weakContext;
    private OnPhotoProcessed onPhotoProcessedListener;

    public ProcessPhotoAsyncTask(Context context, OnPhotoProcessed onPhotoProcessedListener) {
        weakContext = new WeakReference<>(context);
        this.onPhotoProcessedListener = onPhotoProcessedListener;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        Uri photo = uris[0];
        Context context = weakContext.get();
        Bitmap bitmap = null;
        if (context != null) {
            ContentResolver resolver = context.getContentResolver();
            try {

                final InputStream photoStream = resolver.openInputStream(photo);
                bitmap = BitmapFactory.decodeStream(photoStream);
                photoStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        onPhotoProcessedListener.onBitmapReady(bitmap);
    }

    interface OnPhotoProcessed {
        void onBitmapReady(@Nullable Bitmap bitmap);
    }

}
