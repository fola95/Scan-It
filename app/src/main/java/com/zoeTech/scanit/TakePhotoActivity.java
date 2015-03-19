package com.zoeTech.scanit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakePhotoActivity extends ActionBarActivity {

    ImageView mImageView;
    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        mImageView=(ImageView)findViewById(R.id.img);
        dispatchTakePictureIntent();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i(" Take photo result", " I GOT THE RESULT "+mCurrentPhotoPath);
            File f = new File(mCurrentPhotoPath);
            Uri img=Uri.fromFile(f);
            Bitmap imageBitmap=null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        img);
            } catch (IOException e) {
                e.printStackTrace();
            }
             mImageView.setImageBitmap(imageBitmap);

        }
    }

    //take photo helper functions


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Context context = getApplicationContext();
            CharSequence text = "Error while creating image file ..!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));}
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    private File createImageFile() throws IOException{
        String imageFileName;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName= "ScanIt/Scan_"+timestamp+".jpg";
        File storageDir= Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
                //Environment.getExternalStorageDirectory();
        File image = new File(storageDir, imageFileName);

        mCurrentPhotoPath=image.getAbsolutePath();
        return image;

    }
}
