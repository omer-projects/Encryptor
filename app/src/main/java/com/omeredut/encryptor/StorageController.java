package com.omeredut.encryptor;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

public class StorageController {

    private Context context;
    private File cachePath;
    private String imagesDirectory;

    private InternalStorage internalStorage;
    private ExternalStorage externalStorage;

    public StorageController(Context context){
        this.context = context;
        imagesDirectory = context.getString(R.string.images_directory);
        File cacheDir = context.getCacheDir();
        cachePath = new File(cacheDir,imagesDirectory);
        internalStorage = new InternalStorage(cachePath);
        externalStorage = new ExternalStorage();
    }




    public String saveImageToInternalStorage(Bitmap bitmapImage, String fileName){
        internalStorage = new InternalStorage(cachePath);
        return internalStorage.saveImageToInternalStorage(bitmapImage, fileName);
    }
    public String saveImageToInternalStorage(Bitmap bitmapImage){
        internalStorage = new InternalStorage(cachePath);
        return internalStorage.saveImageToInternalStorage(bitmapImage, "");
    }


    public Bitmap loadImageFromInternalStorage(String fileName){
        internalStorage = new InternalStorage(cachePath);
        Bitmap loadImage = internalStorage.loadImageFromInternalStorage(fileName);
        return loadImage;
    }


    public void saveImageToExternalStorage(){
        externalStorage = new ExternalStorage();
    }

}
