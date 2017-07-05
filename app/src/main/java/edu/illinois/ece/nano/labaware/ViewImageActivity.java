package edu.illinois.ece.nano.labaware;

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


public class ViewImageActivity extends AppCompatActivity {


    // public Bitmap grayImage;
    //  public Bitmap image;
    //   public LabAware lab;

    ImageView imageView;
    // Bitmap bm;
    int rangX;
    int rangY;
    int x;
    int y;

    public static final String TAG = ViewImageActivity.class.getSimpleName();

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle isnitialization error
            Log.d(TAG, "OpenCV is not loading");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = (ImageView) findViewById(R.id.imageView);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        // Picasso.with(this).load(imageUri).into(imageView);

        if (imageUri == null) {
            Log.d(TAG, "ImageUri does not exist");
        }

        prints(imageUri.toString());


        rangX = 900;
        rangY = 1200;

        x = 100;
        y = 200;


        Bitmap bm = decodeSampledBitmapFromResource(imageUri, rangX, rangY);
        //100, 100

        bm = Bitmap.createScaledBitmap(bm, 900, 1200, false);

        if (bm == null) {
            prints("This doesn't work");
            Toast.makeText(this, "Cannot retrieve picture", Toast.LENGTH_SHORT).show();

        }

        Bitmap grayImage = detectEdges(bm);

        if (grayImage == null) {
            prints("Gray is not working");
            Toast.makeText(this, "Cannot convert Gray!", Toast.LENGTH_SHORT).show();

        }

        grayImage = Bitmap.createScaledBitmap(grayImage, 900, 1200, false);

        if (grayImage == null) {
            prints("Cannot convert Gray");
            Toast.makeText(this, "Cannot convert Gray Scaled!", Toast.LENGTH_SHORT).show();
        }


        int red = 0;
        int blue = 0;
        int green = 0;


        for (int i : Functions.range(0,1200,false)){
            for (int j : Functions.range(0,900,false)){
                int pixel = bm.getPixel(j,i);

                red += Color.red(pixel);
                blue += Color.blue(pixel);
                green += Color.green(pixel);

            }

        }

        int r = red/(1200*900);
        int b = blue/(1200*900);
        int g = green/(1200*900);

        int result = (int) Math.sqrt(0.241*(r*r) + 0.691*(g*g) + 0.068*(b*b));

        int result2 = (int) (0.299*r + 0.587*g + 0.114*b);

        int result3 = (int) (0.2126*r + 0.7152*g + 0.0722*b);

        printInt(result);
        printInt(result2);
        printInt(result);


        imageView.setImageBitmap(grayImage);


       // Toast.makeText(this, "Found Results for you", Toast.LENGTH_LONG).show();

        /*

        !!!! ACTUAL CODE !!!! JUST UNCOMMENT

        LabAware lab = new LabAware();
        lab.calculateChip(grayImage, bm);


        imageView.setVisibility(View.INVISIBLE);


        textView.setVisibility(View.VISIBLE);

        String one = Integer.toString(lab.green1);
        String two = Integer.toString(lab.green2);
        String three = Integer.toString(lab.green3);
        String four = Integer.toString(lab.green4);
        String five = Integer.toString(lab.green5);
        String six = Integer.toString(lab.green6);
        String seven = Integer.toString(lab.green7);
        String eight = Integer.toString(lab.green8);
        String nine = Integer.toString(lab.green9);
        String ten = Integer.toString(lab.green10);

        textView.setText("Channel 1: " + one + "\n" + "Channel 2: " + two + "\n" + "Channel 3: " + three +
                "\n" + "Channel 4: " + four + "\n" + "Channel 5: " + five + "\n" + "Channel 6: " + six + "\n" + "Channel 7: " +
                seven + "\n" + "Channel 8: " + eight +
                "\n" + "Channel 9: " + nine + "\n" + "Channel 10: " + ten);

        */

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


    public void prints(String message) {

        Log.d(TAG, message);

    }

    public void printInt(int num) {

        String number = Integer.toString(num);
        Log.d(TAG, number);

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

            //TODO: copy the right image appropriately




