package com.omeredut.encryptor;

import android.graphics.Bitmap;
import android.util.Log;

public class Encryption {

    private int mask;
    private int revMask;


    public Encryption(){
        mask = 0b11110000111100001111000011110000;
        revMask = ~mask;
    }


    public Bitmap encryptImage(Bitmap coverImage, Bitmap encryptionImage){





        int widthCoverImage = coverImage.getWidth();
        int heightCoverImage = coverImage.getHeight();
        int widthEncryptionImage = encryptionImage.getWidth();
        int heightEncryptionImage = encryptionImage.getHeight();
        int widthStegoImage = widthCoverImage;
        if (widthEncryptionImage > widthCoverImage){
            widthStegoImage = widthEncryptionImage;
        }
        int heightStegoImage = heightCoverImage;
        if (heightEncryptionImage > heightCoverImage){
            heightStegoImage = heightEncryptionImage;
        }


        Bitmap stegoImage = Bitmap.createBitmap(widthStegoImage, heightStegoImage, Bitmap.Config.ARGB_8888);
        Log.d("steganography", "stegoImage alpha: " + stegoImage.hasAlpha());
        for (int row = 0; row < heightStegoImage; row++) {
            for (int col = 0; col < widthStegoImage; col++) {
                int coverImageBitMask = 0b00000000000000000000000000000000;
                int encryptionImageBitMask = 0b00000000000000000000000000000000;
                if (col < widthCoverImage && row < heightCoverImage){
                    coverImageBitMask = clearLSB(coverImage.getPixel(col, row), 3);
                }
                if (col < widthEncryptionImage && row < heightEncryptionImage){
                    encryptionImageBitMask = dense(encryptionImage.getPixel(col, row), 5);
                }
                stegoImage.setPixel(col, row, coverImageBitMask | encryptionImageBitMask);



                /*//int encryptionBits = 0b1111001100110011 & encryptionImage.getPixel(col, row);
                //int coverBits = 0b11111100110011001100 & coverImage.getPixel(col, row);
                //Log.d("byte", "encryptionbits: " +encryptionBits);
                int encryptionBits =    0b11111100111111001111110011111100 & encryptionImage.getPixel(col, row);
                encryptionBits = encryptionBits >> 2;
                //Log.d("steganography", "byte: " +Byte.(Integer.toBinaryString(encryptionBits)));
                int coverBits =         0b11000000110000001100000011000000 & coverImage.getPixel(col, row);
                //int stegoBits = Byte.decode(Integer.toBinaryString(pix));
                stegoImage.setPixel(col, row, encryptionBits|coverBits);*/
            }
        }
        return stegoImage;
    }

    public Bitmap decrypt(Bitmap decryptionBitmap){
        int width = decryptionBitmap.getWidth();
        int height = decryptionBitmap.getHeight();
        Bitmap decryptBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                decryptBitmap.setPixel(j, i, (decryptionBitmap.getPixel(j, i)&0b01111111011111110111111101111111) << 1);
            }
        }
        return decryptBitmap;
    }


    private int dense(int bits, int n){
        int denseMask = 0b11111110111111101111111011111110 & bits;
        //int dense = denseMask >> 5;
        return denseMask >> 1;
    }
    private int clearLSB(int bits, int n){
        //int clearMask = ~(2^n-1);
        int clearMask = 0b11110000111100001111000011110000;
        return bits & clearMask;
    }


















    /*public Bitmap encryptImage(Bitmap coverImage, Bitmap encryptionImage) {
        Log.d("Encryption", "width cover image: " + coverImage.getWidth());
        Log.d("Encryption", "height cover image: " + coverImage.getHeight());
        Log.d("Encryption", "width encryption image: " + encryptionImage.getWidth());
        Log.d("Encryption", "height encryption image: " + encryptionImage.getHeight());
        int a = 9;
        int numLSBs = 0;//0x00000000;//
        int maxWidth = coverImage.getWidth() - 1;
        int maxHeight = coverImage.getHeight() - 1;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap stegoImage = null;
        stegoImage = Bitmap.createBitmap(coverImage);


        for (int row = 2; row < maxHeight && row < encryptionImage.getHeight()-1; row++) {
            int col = 2 + (row % 2);
            while (col < maxWidth && col < encryptionImage.getWidth()-1) {
                int q = stegoImage.getPixel(col, row-1) ^ stegoImage.getPixel(col, row+1) ^ stegoImage.getPixel(col-1, row) ^ stegoImage.getPixel(col+1, row);
                if (q <= a){
                    numLSBs = 0b0000000011111111;
                } else {
                    numLSBs = 0b0000000011111111 & (int) Math.ceil(q / 2.0);
                }
                int stegoLsb = numLSBs & coverImage.getPixel(col, row);
                stegoImage.setPixel(col, row, stegoLsb);
                col += 2;
            }
        }
        return stegoImage;
    }*/

    public void setMASK(int mask) {
        this.mask = mask;
    }

    public Bitmap newEncryption(Bitmap coverImage, Bitmap encryptionImage){
        int widthCoverImage = coverImage.getWidth();
        int heightCoverImage = coverImage.getHeight();
        int widthEncryptionImage = encryptionImage.getWidth();
        int heightEncryptionImage = encryptionImage.getHeight();
        int widthStegoImage = widthCoverImage;
        if (widthEncryptionImage > widthCoverImage){
            widthStegoImage = widthEncryptionImage;
        }
        int heightStegoImage = heightCoverImage;
        if (heightEncryptionImage > heightCoverImage){
            heightStegoImage = heightEncryptionImage;
        }


        Bitmap stegoImage = Bitmap.createBitmap(widthStegoImage, heightStegoImage, Bitmap.Config.ARGB_8888);
        //Log.d("steganography", "check 1: " + 0b11110000111100001111000011110000);
        //Log.d("steganography", "check 2: " + (0b11110000111100001111000011110000>>>4));
        Log.d("steganography", "stegoImage alpha: " + stegoImage.hasAlpha());
        for (int row = 0; row < heightStegoImage; row++) {
            for (int col = 0; col < widthStegoImage; col++) {
                //Log.d("steganography", "col: " + col + " row: " + row);
                int coverImageBitMask = 0b111111111111111111111111111111;
                int encryptionImageBitMask = 0b11111111111111111111111111111111;
                if (col < widthCoverImage && row < heightCoverImage){
                    int coverBits = 0b11000000110000001100000011000000 & coverImage.getPixel(col, row);
                    int encryptionBits = ((0b00001100000011000000110000001100 & encryptionImage.getPixel(col, row)) << 2);
                    coverImageBitMask = coverBits | encryptionBits;
                }
                if (col < widthEncryptionImage && row < heightEncryptionImage){
                    encryptionImageBitMask = ((0xf0f0f0f0 & encryptionImage.getPixel(col, row)) >>> 4);
                    //Log.d("steganography", "col: " + col + encryptionImageBitMask);
                }
                stegoImage.setPixel(col, row, coverImageBitMask | encryptionImageBitMask);
            }
        }
        //return stegoImage;

        int width = stegoImage.getWidth();
        int height = stegoImage.getHeight();
        Bitmap decryptBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int endDecryptedImage = ((0b00110000001100000011000000110000 & stegoImage.getPixel(col, row)) >>> 2);
                int startDecryptedImage = ((0x0f0f0f0f & stegoImage.getPixel(col, row)) << 4);
                //Log.d("steganography", "stegoImage alpha: " + endDecryptedImage);
                decryptBitmap.setPixel(col, row, startDecryptedImage | endDecryptedImage);
            }
        }
        return decryptBitmap;

    }


    public Bitmap newDecrypt(Bitmap encryptedBitmap){
        int width = encryptedBitmap.getWidth();
        int height = encryptedBitmap.getHeight();
        Bitmap decryptBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int endDecryptedImage = ((0b00110000001100000011000000110000 & encryptedBitmap.getPixel(col, row)) >>> 2);
                int startDecryptedImage = ((0x0f0f0f0f & encryptedBitmap.getPixel(col, row)) << 4);
                //Log.d("steganography", "stegoImage alpha: " + endDecryptedImage);
                decryptBitmap.setPixel(col, row, startDecryptedImage | endDecryptedImage);
            }
        }
        return decryptBitmap;
    }
}
