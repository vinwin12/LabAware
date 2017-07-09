package edu.illinois.ece.nano.labaware.Model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class BrightnessAlgo {

    public static int findBrightness(Bitmap bm){

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


        return result;
    }

}
