package com.omeredut.encryptor;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;


public class ResultActivity extends AppCompatActivity {

    private static final String EXTERNAL_PATH = Environment.getExternalStorageState();
    private static final int COMPRESS_QUALITY = 100;

    //Model View
    private StorageController storageController;

    private String title;
    private int source;
    private ImageView resultImageView;
    private Bitmap resultBitmapImage;
    private String path;
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








    /*private void writeToExternalStorage(Bitmap imageBitmap){

        //BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        //Bitmap bitmap = bitmapDrawable.getBitmap();


        String DIRECTORY_FILES = "/MyAppFiles";
        String state = Environment.getExternalStorageState();
        String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Bird", "Image of bird");
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(savedImageURL);
            if (!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir, "test.jpeg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                //fileOutputStream.write("Hello World!!!".getBytes());
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fileOutputStream);
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error1", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT).show();
        }
    }*/


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
        if (true) {
            String savedImageURL = MediaStore.Images.Media.insertImage(getContentResolver(), resultBitmapImage, "Bird", "Image of bird");
        } else {
            ApplicationAlerts.simpleAlert(this, "Success", "Image saved successfully");
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
}
