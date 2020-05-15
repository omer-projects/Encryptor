package com.omeredut.encryptor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class ExternalStorage {


    private static final String DIRECTORY_APP = "/Encryptor/";
    private static final String END_FILE = ".png";
    private static final int COMPRESS_QUALITY = 100;
    private String filePath;


    public ExternalStorage(String filePath){
        this.filePath = filePath;
    }
    public ExternalStorage(){
        this.filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        new ExternalStorage(filePath);
    }




    public String saveImageToExternalStorage(Bitmap bitmapImage){
        OutputStream outputStream = null;
        String fileName = String.valueOf(System.currentTimeMillis()) + END_FILE;

        File dir = new File(filePath+DIRECTORY_APP);
        dir.mkdir();
        File file = new File(dir, fileName);

        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmapImage.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, outputStream);

        if (outputStream != null){
            try {
                outputStream.flush();
            } catch (IOException e) {
                fileName = null;
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                fileName = null;
            }
        }
        return fileName;
    }

}
