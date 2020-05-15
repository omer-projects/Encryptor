package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.util.Log;

public class Decryption {




    public Bitmap decrypt (Bitmap imageBitmap) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        Bitmap decryptedBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int startDecryptedImage = ((0b00001111000011110000111100001111 & imageBitmap.getPixel(col, row)) << 4);
                decryptedBitmap.setPixel(col, row, startDecryptedImage);
            }
        }
        int x = getXforCrop(decryptedBitmap);
        int y = getYforCrop(decryptedBitmap);
        decryptedBitmap = crop(decryptedBitmap, 0, 0, x, y);
        return decryptedBitmap;
    }

    private int getXforCrop(Bitmap imageBitmap){
        int x = imageBitmap.getWidth();
        int width = x - 1;
        Log.d("crop", "width = " + width);
        boolean checkCrop = true;
        for (int i = width; checkCrop && i > -1; i--) {
            if (imageBitmap.getPixel(i, 0) != 0) {
                checkCrop = false;
                x = i;
            }
        }
        Log.d("crop", "x = " + x);
        return x;
    }


    private int getYforCrop(Bitmap imageBitmap){
        int y = imageBitmap.getHeight();
        int height = y - 1;
        Log.d("crop", "height = " + height);
        boolean checkCrop = true;
        for (int i = height; checkCrop && i > -1; i--) {
            if (imageBitmap.getPixel(0, i) != 0) {
                checkCrop = false;
                y = i;
            }
        }
        Log.d("crop", "y = " + y);
        return y;
    }

    private Bitmap crop (Bitmap imageBitmap, int x, int y, int width, int height) {
        Bitmap cropedBitmap = Bitmap.createBitmap(imageBitmap, x, y, width, height);
        return cropedBitmap;
    }

}
