package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class InternalStorage {

    public static final int COMPRESS_QUALITY = 100;

    private File cachePath;


    public InternalStorage(File cachePath){
        this.cachePath = cachePath;
        cachePath.mkdirs(); // make the directory
    }

    public String saveImageToInternalStorage(Bitmap imageBitmap) {
        return saveImageToInternalStorage(imageBitmap, "");
    }

    public String saveImageToInternalStorage(Bitmap imageBitmap, String fileName) {
        // save bitmap to cache directory
        if (fileName.equals("")){
            fileName = "/image.png";
        } else {
            fileName = '/' + fileName;
        }
        try {
            FileOutputStream stream = new FileOutputStream(cachePath + fileName); // overwrites this image every time
            imageBitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }



    public Bitmap loadImageFromInternalStorage(String fileName) {
        String imagePath = cachePath.getPath() + "/" + fileName;
        return BitmapFactory.decodeFile(imagePath);
    }


}
