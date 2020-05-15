package com.omeredut.encryptor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import static com.omeredut.encryptor.EditImageFragment.EDIT_IMAGE;
import static com.omeredut.encryptor.EditImageFragment.SELECT_IMAGE;


public class DecryptionActivity extends AppCompatActivity {

    private Button decryptionButton;

    private boolean alertIsOpen;
    private boolean hasDecryptionImage;
    private Steganography steganography;
    //Model View
    private StorageController storageController;

    private Toolbar decryptionToolbar;

    private Bitmap decryptionBitmap;
    private ImageView decryptionImageView;
    private LinearLayout decryptionImageContainer;
    private CardView decryptionCardView;

    private Bitmap coverBitmap;
    private ImageView coverImageView;
    private LinearLayout coverImageContainer;
    private CardView coverDecryptionCardView;
    private boolean hasCoverImage;

    private EditImageFragment editImageFragment;


    private boolean decryptWithCoverImage;
    private LinearLayout coverDecryptionContainer;


    private boolean isCoverImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decryption);


        decryptWithCoverImage = false;
        coverDecryptionContainer = findViewById(R.id.cover_decryption_container);
        if (decryptWithCoverImage){
            coverDecryptionContainer.setVisibility(View.VISIBLE);
        } else {
            coverDecryptionContainer.setVisibility(View.GONE);
        }

        decryptionToolbar = findViewById(R.id.decryption_toolbar);
        setSupportActionBar(decryptionToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        storageController = new StorageController(this);
        steganography = new Steganography();

        decryptionImageView = findViewById(R.id.decryption_image_view);
        decryptionImageContainer = findViewById(R.id.container_choose_decryption_image);
        decryptionCardView = findViewById(R.id.decryption_card_view);
        decryptionCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.LTGRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.WHITE);
                        isCoverImage = false;
                        if (!editImageFragment.isAdded()) {
                            if (hasDecryptionImage) {
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


        coverImageView = findViewById(R.id.cover_decryption_image_view);
        coverImageContainer = findViewById(R.id.container_choose_cover_decryption_image);
        coverDecryptionCardView = findViewById(R.id.cover_decryption_card_view);
        coverDecryptionCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.LTGRAY);
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.WHITE);
                        isCoverImage = true;
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


        editImageFragment = new EditImageFragment(this);
        editImageFragment.setOnImageChangeListener(new EditImageFragment.OnImageChangeListener() {
            @Override
            public void onImageChange(Bitmap bitmap) {
                changeImage(bitmap);
            }
        });

        decryptionButton = findViewById(R.id.decryption_button);
        decryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (decryptionBitmap == null || (coverBitmap == null && decryptWithCoverImage)) {
                    String titleAlert = "Failure";
                    String messageAlert = "";
                    if (decryptWithCoverImage && coverBitmap == null && decryptionBitmap == null) {
                        messageAlert = "You are missing a cover image and for decryption image";
                    } else if (decryptWithCoverImage && coverBitmap == null) {
                        messageAlert = "You are missing a cover image";
                    } else {
                        messageAlert = "You are missing a for decryption image";
                    }
                    ApplicationAlerts.simpleAlert(DecryptionActivity.this, titleAlert, messageAlert);
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


    private Dialog newFragment;

    private void sendToDecryption(){
        newFragment = ApplicationAlerts.progressBarAlert(DecryptionActivity.this);
        new AsyncTask<Bitmap, Integer, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alertIsOpen = true;
                newFragment.show();//.show(getSupportFragmentManager(), "dialog");
            }

            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                Bitmap resultBitmap = steganography.decrypt(bitmaps[0]);
                String path = storageController.saveImageToInternalStorage(resultBitmap);
                return path;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                newFragment.dismiss();
                alertIsOpen = false;
                passToResultActivity(s);
            }
        }.execute(decryptionBitmap);
    }


    private void passToResultActivity(String path){
        if (path != null){
            Intent encryptionIntent = new Intent(this, ResultActivity.class);
            encryptionIntent.putExtra(getString(R.string.source), R.id.decryption_image_nav_item);
            encryptionIntent.putExtra(getString(R.string.name_file_image), path);
            startActivity(encryptionIntent);
        } else {
            Toast.makeText(DecryptionActivity.this, "failure", Toast.LENGTH_LONG).show();
        }
    }



    private void changeImage(Bitmap imageBitmap){
        if (isCoverImage){
            changeBitmapCoverImage(imageBitmap);
        } else {
            changeDecryptionImage(imageBitmap);
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
            coverImageView.setImageBitmap(coverBitmap);
            hasCoverImage = true;
        }
    }


    private void changeDecryptionImage(Bitmap bitmapImage) {
        decryptionBitmap = bitmapImage;
        if (bitmapImage == null) {
            decryptionImageContainer.setVisibility(View.VISIBLE);
            decryptionImageView.setVisibility(View.GONE);
            hasDecryptionImage = false;
        } else {
            decryptionImageContainer.setVisibility(View.GONE);
            decryptionImageView.setVisibility(View.VISIBLE);
            decryptionImageView.setImageBitmap(decryptionBitmap);
            hasDecryptionImage = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editImageFragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.decrypt_with_cover_image:
                decryptWithCoverImageState(item);
                break;
        }
        return true;
    }

    private void decryptWithCoverImageState(MenuItem item){
        if (decryptWithCoverImage){
            decryptWithCoverImage = false;
            item.setIcon(R.drawable.ic_photo_library_white_24dp);
            coverDecryptionContainer.setVisibility(View.GONE);
            changeBitmapCoverImage(null);
        } else {
            decryptWithCoverImage = true;
            item.setIcon(R.drawable.ic_photo_white_24dp);
            coverDecryptionContainer.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (coverBitmap != null) {
            String coverBitmapPath = storageController.saveImageToInternalStorage(coverBitmap, "coverImage");
            outState.putString("coverBitmapPath", coverBitmapPath);
        }
        if (decryptionBitmap != null) {
            String decryptionBitmapPath = storageController.saveImageToInternalStorage(decryptionBitmap, "decryptionImage");
            outState.putString("decryptionBitmapPath", decryptionBitmapPath);
        }
        outState.putBoolean("decryptWithCoverImage", decryptWithCoverImage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String coverPath = savedInstanceState.getString("coverBitmapPath", "");
        String decryptionPath = savedInstanceState.getString("decryptionBitmapPath", "");
        alertIsOpen = savedInstanceState.getBoolean("alertIsOpen", false);
        decryptWithCoverImage = savedInstanceState.getBoolean("decryptWithCoverImage", false);

        if (!coverPath.equals("")){
            Bitmap coverBit = storageController.loadImageFromInternalStorage(coverPath);
            changeBitmapCoverImage(coverBit);
        }
        if (!decryptionPath.equals("")){
            Bitmap decryptionBit = storageController.loadImageFromInternalStorage(decryptionPath);
            changeImage(decryptionBit);
        }
    }
}
