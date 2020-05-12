package com.omeredut.encryptor;

import android.graphics.Bitmap;

public class Decryption {


    public Bitmap decryptImage(Bitmap decryptionImage){
        Bitmap resultImage = null;
        return resultImage;
    }





    public Bitmap decrypt(Bitmap coverImage, Bitmap encryptedBitmap){
        //int coverWidth = coverImage.getWidth();
        //int coverHeight = coverImage.getHeight();



        int width = encryptedBitmap.getWidth();
        int height = encryptedBitmap.getHeight();
        Bitmap decryptBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                decryptBitmap.setPixel(j, i, (encryptedBitmap.getPixel(j, i)&0b01111111011111110111111101111111) << 1);
            }
        }
        return decryptBitmap;
    }

}
