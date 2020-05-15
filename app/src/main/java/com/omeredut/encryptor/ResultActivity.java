package com.omeredut.encryptor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

    private static final File EXTERNAL_PATH = Environment.getExternalStorageDirectory();

    //Model View
    private StorageController storageController;

    private String title;
    private int source;
    private ImageView resultImageView;
    private Bitmap resultBitmapImage;
    //private String path;
    private String fileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar resultToolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageController = new StorageController(this);

        resultImageView = (ImageView) findViewById(R.id.result_image_view);


        source = getIntent().getIntExtra(getString(R.string.source), 0);
        fileName = getIntent().getStringExtra(getString(R.string.name_file_image));
        //resultBitmapImage = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/test/" + fileName);
        //resultBitmapImage = BitmapFactory.decodeFile(fileName);
        resultBitmapImage = storageController.loadImageFromInternalStorage(fileName);
        resultImageView.setImageBitmap(resultBitmapImage);
        setTitleBySource(source);

        /*if (savedInstanceState != null){
            //TODO: pull the saved state.
            resultBitmapImage = savedInstanceState.getParcelable(getString(R.string.result_image));
            resultImageView.setImageBitmap(resultBitmapImage);
        }*/
    }

    private void checkWritePermissionAndSaveImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
            //writeToExternalStorage(resultImageView);
            writeImageToGallery(resultBitmapImage);
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                // Re-attempt file write
                writeImageToGallery(resultBitmapImage);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.share_result_item:
                shareResult(fileName);
                break;
            case R.id.save_result_item:
                saveResult();
                break;
        }
        return true;
    }

    private void shareResult(String fileName){
        File imagePath = new File(getCacheDir(), getString(R.string.images_directory));
        File imageFile = new File(imagePath, fileName);
        Uri contentUri = FileProvider.getUriForFile(this, getPackageName()+".provider", imageFile);
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image_title)));
        }
    }

    private void saveResult() {
        checkWritePermissionAndSaveImage();
    }

    private void writeImageToGallery(Bitmap imageBitmap) {
        String fileName = storageController.saveImageToExternalStorage(imageBitmap);
        if (fileName != null) {
            showImageInGallery(fileName);
            Toast.makeText(ResultActivity.this, "The image saved successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ResultActivity.this, "The image saved failed", Toast.LENGTH_LONG).show();
        }
    }


    private void setTitleBySource(int source){
        switch (source){
            case R.id.encryption_image_nav_item:
                title = "Encryption Result";
                break;
            case R.id.decryption_image_nav_item:
                title = "Decryption Result";
                break;
            default:
                title = "Result Activity";
                break;
        }
        setTitle(title);
    }
    /*
    //this code its work but just now its not necessary
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (resultBitmapImage != null) {
            String resultBitmapPath = storageController.saveImageToInternalStorage(resultBitmapImage, "resultImage");
            outState.putString("resultBitmapPath", resultBitmapPath);        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String resultPath = savedInstanceState.getString("resultBitmapPath", "");
        if (!resultPath.equals("")){
            resultBitmapImage = storageController.loadImageFromInternalStorage(resultPath);
            resultImageView.setImageBitmap(resultBitmapImage);
        }
    }*/


    /**
     * This function show the given image file in the gallery
     * @param fileName the given image file name
     */
    private void showImageInGallery(String fileName){
        String pathImageFile = EXTERNAL_PATH+"/"+getResources().getString(R.string.app_name)+"/"+fileName;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, getResources().getString(R.string.app_name));
        //values.put(MediaStore.Images.Media.DESCRIPTION, "some description");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, (pathImageFile.toLowerCase(Locale.US).hashCode()));
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, fileName.toLowerCase(Locale.US));
        values.put("_data", pathImageFile);

        ContentResolver cr = getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
