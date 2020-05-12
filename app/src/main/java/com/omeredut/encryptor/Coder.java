package com.omeredut.encryptor;

import android.graphics.Bitmap;

public class Coder {


    private Encryption encryption;
    private Decryption decryption;

    public Coder(){
        encryption = new Encryption();
        decryption = new Decryption();
    }


    public Bitmap getEncryptionImage() {
        return null;
    }

    public Bitmap getDecryptionImage(){
        return null;
    }

}
