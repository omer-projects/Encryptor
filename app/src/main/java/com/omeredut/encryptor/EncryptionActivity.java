package com.omeredut.encryptor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.omeredut.encryptor.EditImageFragment.EDIT_IMAGE;
import static com.omeredut.encryptor.EditImageFragment.SELECT_IMAGE;


public class EncryptionActivity extends AppCompatActivity {


    private Button encryptButton;

    private Dialog newFragment;
    private Steganography steganography;

    //cover image
    private Bitmap coverBitmap;
    private ImageView coverImageView;
    private LinearLayout coverImageContainer;
    private CardView coverCardView;

    //encryption image
    private Bitmap encryptionBitmap;
    private ImageView encryptionImageView;
    private LinearLayout encryptionImageContainer;
    private CardView encryptionCardView;

    private boolean alertIsOpen;
    private boolean hasCoverImage;
    private boolean hasEncryptionImage;
    private boolean isCoverImage;

    private EditImageFragment editImageFragment;

    private StorageController storageController;
    private Coder coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        steganography = new Steganography();

        //toolbar settings
        Toolbar encryptionToolbar = findViewById(R.id.encryption_toolbar);
        setSupportActionBar(encryptionToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageController = new StorageController(this);
        coder = new Coder();

        //cover image settings
        coverImageView = findViewById(R.id.cover_image_view);
        coverImageContainer = findViewById(R.id.container_choose_cover_image);
        coverCardView = findViewById(R.id.cover_card_view);
        coverCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.LTGRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        isCoverImage = true;
                        view.setBackgroundColor(Color.WHITE);
                        if (!editImageFragment.isAdded()) {
                            if (hasCoverImage) {
                                editImageFragment.show(getSupportFragmentManager(), EDIT_IMAGE);
                            } else {//choose image
                                editImageFragment.show(getSupportFragmentManager(), SELECT_IMAGE);
                            }
                        }
                        return true;
                }
                return false;
            }
        });


        //cover image settings
        encryptionImageView = findViewById(R.id.encryption_image_view);
        encryptionImageContainer = findViewById(R.id.container_choose_encryption_image);
        encryptionCardView = findViewById(R.id.encryption_card_view);
        encryptionCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.LTGRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        isCoverImage = false;
                        view.setBackgroundColor(Color.WHITE);
                        if (!editImageFragment.isAdded()) {
                            if (hasEncryptionImage) {
                                editImageFragment.show(getSupportFragmentManager(), EDIT_IMAGE);
                            } else {//choose image
                                editImageFragment.show(getSupportFragmentManager(), SELECT_IMAGE);
                            }
                        }
                        return true;
                }
                return false;
            }
        });




        encryptButton = findViewById(R.id.encrypt_button);
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coverBitmap == null || encryptionBitmap == null){
                    String titleAlert = "Failure";
                    String messageAlert = "";
                    if (coverBitmap == null && encryptionBitmap == null){
                        messageAlert = "You are missing a cover image and for encryption image";
                    } else if (coverBitmap == null) {
                        messageAlert = "You are missing a cover image";
                    } else {
                        messageAlert = "You are missing a for encryption image";
                    }
                    ApplicationAlerts.simpleAlert(EncryptionActivity.this, titleAlert, messageAlert);
                } else {
                    sendToDecryption();
                }
            }
        });

        editImageFragment = new EditImageFragment(this);
        editImageFragment.setOnImageChangeListener(new EditImageFragment.OnImageChangeListener() {
            @Override
            public void onImageChange(Bitmap bitmap) {
                changeImage(bitmap);
            }
        });

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }*/


    private void sendToDecryption(){
        new AsyncTask<Bitmap, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alertIsOpen = true;
                showProgressDialog();
            }

            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                Bitmap resultBitmap = steganography.encrypt(bitmaps[0], bitmaps[1]);
                String path = storageController.saveImageToInternalStorage(resultBitmap);
                return path;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dismissProgressDialog();
                alertIsOpen = false;
                passToResultActivity(s);
            }
        }.execute(coverBitmap, encryptionBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editImageFragment.onActivityResult(requestCode, resultCode, data);
    }

    private void changeImage(Bitmap imageBitmap){
        if (isCoverImage){
            changeBitmapCoverImage(imageBitmap);
        } else {
            changeBitmapEncryptionImage(imageBitmap);
        }
    }


    private void changeBitmapCoverImage(Bitmap imageBitmap){
        coverBitmap = imageBitmap;
        if (imageBitmap == null) {
            coverImageContainer.setVisibility(View.VISIBLE);
            coverImageView.setVisibility(View.GONE);
            hasCoverImage = false;
        } else {
            coverImageContainer.setVisibility(View.GONE);
            coverImageView.setVisibility(View.VISIBLE);
            coverImageView.setImageBitmap(/*getRoundedCornerBitmap(squareImage(*/coverBitmap/*))*/);
            hasCoverImage = true;
        }
    }
    private void changeBitmapEncryptionImage(Bitmap imageBitmap){
        encryptionBitmap = imageBitmap;
        if (imageBitmap == null) {
            encryptionImageContainer.setVisibility(View.VISIBLE);
            encryptionImageView.setVisibility(View.GONE);
            hasEncryptionImage = false;
        } else {
            encryptionImageContainer.setVisibility(View.GONE);
            encryptionImageView.setVisibility(View.VISIBLE);
            encryptionImageView.setImageBitmap(/*getRoundedCornerBitmap(squareImage(*/encryptionBitmap/*))*/);
            hasEncryptionImage = true;
        }
    }

    public String saveImage(Bitmap bitmapImage){
        OutputStream outputStream = null;
        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpeg";

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath+"/test/");
        dir.mkdir();
        File file = new File(dir, fileName);
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

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





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //loadUrl();
            }
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    private void passToResultActivity(String path){
        if (path != null){
            Intent encryptionIntent = new Intent(this, ResultActivity.class);
            encryptionIntent.putExtra(getString(R.string.source), R.id.encryption_image_nav_item);
            encryptionIntent.putExtra(getString(R.string.name_file_image), path);
            startActivity(encryptionIntent);
        } else {
            ApplicationAlerts.simpleAlert(this, "Failure", "Something went wrong...");
        }
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (coverBitmap != null) {
            String coverBitmapPath = storageController.saveImageToInternalStorage(coverBitmap, "coverImage");
            outState.putString("coverBitmapPath", coverBitmapPath);
        }
        if (encryptionBitmap != null) {
            String encryptionBitmapPath = storageController.saveImageToInternalStorage(encryptionBitmap, "encryptionImage");
            outState.putString("encryptionBitmapPath", encryptionBitmapPath);
        }
        //outState.putBoolean("alertIsOpen", alertIsOpen);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String coverPath = savedInstanceState.getString("coverBitmapPath", "");
            String encryptionPath = savedInstanceState.getString("encryptionBitmapPath", "");
            alertIsOpen = savedInstanceState.getBoolean("alertIsOpen", false);
            if (!coverPath.equals("")){
                Bitmap coverBit = storageController.loadImageFromInternalStorage(coverPath);
                changeBitmapCoverImage(coverBit);
            }
            if (!encryptionPath.equals("")){
                Bitmap encryptionBit = storageController.loadImageFromInternalStorage(encryptionPath);
                changeBitmapEncryptionImage(encryptionBit);
            }
            /*if (alertIsOpen){
                showProgressDialog();
            }*/
        }
    }

    private void showProgressDialog(){
        if (newFragment == null){
            newFragment = ApplicationAlerts.progressBarAlert(EncryptionActivity.this);;
        }
        newFragment.show();
    }

    private void dismissProgressDialog(){
        if (newFragment != null && newFragment.isShowing()){
            newFragment.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
}
