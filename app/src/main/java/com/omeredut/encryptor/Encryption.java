package com.omeredut.encryptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

public class Encryption {

    private int mask;
    private int notMask;


    public Encryption(){
        new Encryption(0b11110000111100001111000011110000);
    }

    public Encryption(int mask){
        this.mask = mask;
        notMask = ~mask;
    }




    /*private int dense(int bits, int n){
        int denseMask = 0b11111110111111101111111011111110 & bits;
        return denseMask >> 1;
    }
    private int clearLSB(int bits, int n){
        //int clearMask = ~(2^n-1);
        int clearMask = 0b11110000111100001111000011110000;
        return bits & clearMask;
    }
    public void setMASK(int mask) {
        this.mask = mask;
    }*/






    public Bitmap encrypt(Bitmap coverImage, Bitmap encryptionImage){
        int widthCoverImage = coverImage.getWidth();
        int heightCoverImage = coverImage.getHeight();
        int widthEncryptionImage = encryptionImage.getWidth();
        int heightEncryptionImage = encryptionImage.getHeight();
        int widthStegoImage = widthCoverImage;

        if (heightCoverImage >= heightEncryptionImage && widthCoverImage >= widthEncryptionImage) {
            return encryptBitmap(coverImage, encryptionImage);
        } else if (2*heightCoverImage >= heightEncryptionImage && 2*widthCoverImage >= widthEncryptionImage){
            Bitmap bitmapExtended = extendBimap(coverImage);
            return encryptBitmap(coverImage, encryptionImage);
        } else {
            return null;
        }
    }


    private Bitmap extendBimap(Bitmap imageBitmap){
        return extendBimap(imageBitmap, 2);
    }

    private Bitmap extendBimap(Bitmap imageBitmap, int n){
        int width = imageBitmap.getWidth()*n;
        int height = imageBitmap.getHeight()*n;

        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                resultBitmap.setPixel(col, row, imageBitmap.getPixel(col%n, row%n));
            }
        }
        return resultBitmap;
    }


    private Bitmap encryptBitmap(Bitmap coverImage, Bitmap encryptionImage){
        int widthCoverImage = coverImage.getWidth();
        int heightCoverImage = coverImage.getHeight();
        int widthEncryptionImage = encryptionImage.getWidth();
        int heightEncryptionImage = encryptionImage.getHeight();


        Bitmap stegoImage = Bitmap.createBitmap(widthCoverImage, heightCoverImage, Bitmap.Config.ARGB_8888);

        if (heightCoverImage >= heightEncryptionImage && widthCoverImage >= heightEncryptionImage) {
            for (int row = 0; row < heightCoverImage; row++) {
                for (int col = 0; col < widthCoverImage; col++) {
                    int coverImageBitMask = 0;
                    int encryptionImageBitMask = 0;
                    if (col < widthEncryptionImage && row < heightEncryptionImage) {
                        coverImageBitMask = 0b11110000111100001111000011110000 & coverImage.getPixel(col, row);
                        encryptionImageBitMask = (0b11110000111100001111000011110000 & encryptionImage.getPixel(col, row)) >>> 4;
                    } else {
                        coverImageBitMask = 0b11110000111100001111000011110000 & coverImage.getPixel(col, row);
                    }
                    stegoImage.setPixel(col, row, coverImageBitMask | encryptionImageBitMask);
                }
            }
        }
        return stegoImage;
    }

}
