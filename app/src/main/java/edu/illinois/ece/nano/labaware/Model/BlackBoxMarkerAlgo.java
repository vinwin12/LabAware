package edu.illinois.ece.nano.labaware.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.imgproc.Imgproc;


import edu.illinois.ece.nano.labaware.Model.Functions;
import edu.illinois.ece.nano.labaware.Model.LabAware;

import static java.lang.System.loadLibrary;


public class BlackBoxMarkerAlgo {
    ImageView imageView;
    public Uri mMediaUri;
    int rangX;
    int rangY;
    int x;
    int y;
    public static final String TAG = BlackBoxMarkerAlgo.class.getSimpleName();



    public BlackBoxMarkerAlgo(Uri uri){
        mMediaUri = uri;
    }



    public int [] findValuesAlgo(){
        int[] values = new int[10];

        if (mMediaUri == null) {
            Log.d(TAG, "ImageUri does not exist");
        }


        rangX = 900;
        rangY = 1200;



        Bitmap bm = decodeSampledBitmapFromResource(mMediaUri, 900, 1200);
        //100, 100

        bm = Bitmap.createScaledBitmap(bm, 900, 1200, false);

        if (bm == null) {
            prints("This doesn't work");

        }



        int brightness = BrightnessAlgo.findBrightness(bm);

        if(brightness > 0) {

            x = 100;
            y = 200;

        }

        //TODO: Pick brightness thresholds and everything


        Bitmap grayImage = detectEdges(bm);

        if (grayImage == null) {
            prints("Gray is not working");

        }

        grayImage = Bitmap.createScaledBitmap(grayImage, 900, 1200, false);

        if (grayImage == null) {
            prints("Cannot convert Gray");
        }

        LabAware lab = new LabAware();
        values = lab.calculateChip(grayImage, bm);


        return values;
    }















    public Bitmap detectEdges(Bitmap bitmap) {

        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);
        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, x, y, 3, false);

        //TODO: intially set to true, check to see if false makes sense

        Bitmap image = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(edges, image);

        return image;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }

        }

        return inSampleSize;

    }


    public void prints(String message) {

        Log.d(TAG, message);

    }

    public void printInt(int num) {

        String number = Integer.toString(num);
        Log.d(TAG, number);

    }

    public static Bitmap decodeSampledBitmapFromResource(Uri uri,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }


}
