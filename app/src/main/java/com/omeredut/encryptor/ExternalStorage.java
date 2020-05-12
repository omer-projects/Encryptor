package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExternalStorage {


    private static final String DIRECTORY_APP = "/test/";
    private static final String END_FILE = ".jpeg";
    private static final int COMPRESS_QUALITY = 100;




    public String saveImageToExternalStorage(Bitmap bitmapImage){
        OutputStream outputStream = null;
        String fileName = String.valueOf(System.currentTimeMillis()) + END_FILE;

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()+DIRECTORY_APP);
        dir.mkdir();
        File file = new File(dir, fileName);

        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, outputStream);

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


    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (file.mkdirs()) {
            //if directory not exist
        //    Toast.makeText(getApplicationContext(),
        //            file.getAbsolutePath() + " created",
        //            Toast.LENGTH_LONG).show();
        }else{
        //    Toast.makeText(getApplicationContext(),
        //            "Directory not created", Toast.LENGTH_LONG).show();
        }
        return file;
    }

}
