package com.omeredut.encryptor;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

public class StorageController {

    private File cachePath;
    private String imagesDirectory;

    private InternalStorage internalStorage;
    private ExternalStorage externalStorage;

    public StorageController(Context context){
        imagesDirectory = context.getString(R.string.images_directory);
        File cacheDir = context.getCacheDir();
        cachePath = new File(cacheDir,imagesDirectory);
        internalStorage = new InternalStorage(cachePath);
        externalStorage = new ExternalStorage();
    }




    public String saveImageToInternalStorage(Bitmap bitmapImage, String fileName){
        return internalStorage.saveImageToInternalStorage(bitmapImage, fileName);
    }
    public String saveImageToInternalStorage(Bitmap bitmapImage){
        return internalStorage.saveImageToInternalStorage(bitmapImage);
    }


    public Bitmap loadImageFromInternalStorage(String fileName){
        return internalStorage.loadImageFromInternalStorage(fileName);
    }


    public String saveImageToExternalStorage(Bitmap imageBitmap){
        return externalStorage.saveImageToExternalStorage(imageBitmap);
    }

}
