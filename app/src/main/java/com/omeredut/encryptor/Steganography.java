package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.util.Log;

public class Steganography {

    private Decryption decryption;
    private Encryption encryption;

    public Steganography(){
        encryption = new Encryption();
        decryption = new Decryption();
    }


    public boolean isGrayScale(){return false;}
    public boolean isSteganography(){return false;}
    public Bitmap decrypt(Bitmap decryptionBitmap){
        return encryption.newDecrypt(decryptionBitmap);
    }



    public Bitmap encrypt(Bitmap coverImageBitmap, Bitmap encryptionBitmap){
        /*Bitmap redChannelEncrypt = encryption.newEncryption(getRedChannel(coverImageBitmap), getRedChannel(encryptionBitmap));
        Bitmap greenChannelEncrypt = encryption.newEncryption(getGreenChannel(coverImageBitmap), getGreenChannel(encryptionBitmap));
        Bitmap blueChannelEncrypt = encryption.newEncryption(getBlueChannel(coverImageBitmap), getBlueChannel(encryptionBitmap));
        Bitmap resultBitmap = getColorImage(redChannelEncrypt, greenChannelEncrypt, blueChannelEncrypt);
        return resultBitmap;*/
        //return encryption.encryptImage(rgb2GrayScale(coverImageBitmap), rgb2GrayScale(encryptionBitmap));
        return encryption.newEncryption(coverImageBitmap, encryptionBitmap);
    }

    public Bitmap getBlueChannel(Bitmap imageBitmap){
        int width = imageBitmap.getWidth();
        int height =imageBitmap.getHeight();
        Bitmap blueChannel = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Log.d("steganography", "blue alpha: " + blueChannel.hasAlpha());
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++)
            {
                blueChannel.setPixel(col, row, imageBitmap.getPixel(col, row) & 0xFF0000FF);
            }
        }
        return blueChannel;
    }

    public Bitmap getRedChannel(Bitmap imageBitmap){
        int width = imageBitmap.getWidth();
        int height =imageBitmap.getHeight();
        Bitmap redChannel = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Log.d("steganography", "red alpha: " + redChannel.hasAlpha());
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++)
            {
                redChannel.setPixel(col, row, imageBitmap.getPixel(col, row) & 0xFFFF0000);
            }
        }
        return redChannel;
    }
    public Bitmap getGreenChannel(Bitmap imageBitmap){
        int width = imageBitmap.getWidth();
        int height =imageBitmap.getHeight();
        Bitmap greenChannel = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Log.d("steganography", "green alpha: " + greenChannel.hasAlpha());
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++)
            {
                greenChannel.setPixel(col, row, imageBitmap.getPixel(col, row) & 0xFF00FF00);
            }
        }
        return greenChannel;
    }

    public Bitmap getColorImage(Bitmap redChannelBitmap, Bitmap greenChannelBitmap, Bitmap blueChannelBitmap){
        Bitmap colorImage = Bitmap.createBitmap(redChannelBitmap);
        int width = redChannelBitmap.getWidth();
        int height = redChannelBitmap.getHeight();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                colorImage.setPixel(col, row, (redChannelBitmap.getPixel(col, row)|greenChannelBitmap.getPixel(col, row)|blueChannelBitmap.getPixel(col, row)));
            }
        }
        return colorImage;
    }

}
