package com.status.statusdownloader;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

class Worker extends AsyncTask<Void ,Void, Bitmap> {

    ImageView imageView;
    String path;
    public Worker(ImageView imageView, String path) {
        this.imageView=imageView;
        this.path=path;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        return thumb;
    }

    @Override
    protected void onPostExecute(Bitmap thumb) {
        super.onPostExecute(thumb);
        if(thumb!=null){
            imageView.setImageBitmap(thumb);
        }else{
        }


    }
}
