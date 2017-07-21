package com.lqh.lichao.myopencv.com.lqh.lichao.util;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Administrator on 2017-07-21.
 */

public class ImageProcessUtils {

    /**
     * 转换为灰度图像
     * @param bitmap
     * @return
     */
    public static Bitmap convert2Gray(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst, bitmap);
        src.release();//释放内存
        dst.release();
        return bitmap;
    }

    /**
     * Mat像素取反操作
     * @param bitmap
     * @return
     */
    public static Bitmap invert(Bitmap bitmap) {
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        long startTime = System.currentTimeMillis();
        Core.bitwise_not(src, src);
        long end = System.currentTimeMillis() - startTime;
        Log.i("invert Mat-TIME", "\t" + end);
        Utils.matToBitmap(src, bitmap);
        src.release();
        return bitmap;
    }

    /**
     * Bitmap像素取反操作
     * @param bitmap
     */
    public static void localInvert(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int index = 0;
        int a=0, r=0, g=0, b=0;
        long startTime = System.currentTimeMillis();
        for(int row = 0; row < height; row++) {
            index = width * height;
            for (int col = 0; col < width; col++) {
                int pixel = pixels[index];//获取图像所有像素
                a = (pixel >> 24)&0xff;
                r = (pixel >> 16)&0xff;//把通道取出来
                g = (pixel >> 8)&0xff;
                b = (pixel&0xff);
                r = 255 -r;//通道取反
                g = 255 -g;
                b = 255 -b;
                pixel = ((a&0xff)<<24) | ((r&0xff)<<16) | ((g&0xff)<<8) | (b&0xff);//把通道设回去
                pixels[index] = pixel;
                index++;
            }
        }
        long end = System.currentTimeMillis() - startTime;
        Log.i("localInvert TIME", "\t" + end);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }
}
