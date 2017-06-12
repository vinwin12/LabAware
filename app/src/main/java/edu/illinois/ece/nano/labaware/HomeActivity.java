package edu.illinois.ece.nano.labaware;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity{

    public static final String TAG = HomeActivity.class.getSimpleName();
    public static final int  REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_PICK_PHOTO = 2;

    public static final int MEDIA_TYPE_IMAGE = 4;

    public Uri mMediaUri;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK){

            if(requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO){
              //  if (data != null){
             //       mMediaUri = data.getData();
              //  }




                Intent intent = new Intent(this, ViewImageActivity.class);
               intent.setData(mMediaUri);

                startActivity(intent);

            }

        }

        else if (resultCode != RESULT_CANCELED){

            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }

    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMediaUri != null){
            outState.putString("cameraImageUri",mMediaUri.toString());
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.containsKey("cameraImageUri")){
            mMediaUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }


    <provider
            android:authorities="${applicationId}.provider"
            android:name="android.support.v4.content.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"/>

        </provider>*/

    @OnClick(R.id.cameraButton)
    void takePhoto(){

        mMediaUri  = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        if (mMediaUri == null){
            Toast.makeText(this, "There was a problem accessing your device's external storage.", Toast.LENGTH_LONG).show();
        }

        else{

            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent , REQUEST_TAKE_PHOTO);

        }
    }

    private Uri getOutputMediaFileUri(int mediaType) {

        //check for external storage
        if (isExternalStorageAvailable()){

            //get the URI
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            fileName = "IMG_" + timeStamp;
            fileType = ".jpg";

            File mediaFile;
            try {
                 mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
         //       Log.i(TAG, "File: " + FileProvider.getUriForFile(HomeActivity.this,BuildConfig.APPLICATION_ID + ".provider",mediaFile));
             //   return FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider",mediaFile);

                return Uri.fromFile(mediaFile);

            }
            catch (IOException e){
                Log.e(TAG,"Error creating file: " + mediaStorageDir.getAbsolutePath() + fileName + fileType);
            }
        }
        //something went wrong
        return null;
    }


    private boolean isExternalStorageAvailable(){

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        else{
            return false;
        }

    }


    @OnClick(R.id.viewImage)
    void pickPhoto(){

        Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent,REQUEST_PICK_PHOTO);



    }





}


