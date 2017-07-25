package com.lqh.lichao.myopencv.com.lqh.lichao.util;

import android.graphics.Bitmap;
import android.util.Log;
import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants.CUSTOM_BLUR_COMMAND;
import static com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants.CUSTOM_EDGE_COMMAND;
import static com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants.CUSTOM_SHARPEN_COMMAND;

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
        Core.bitwise_not(src, src);//按位取反，OpenCV在C++中实现
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
            index = row * width;
            for(int col = 0; col < width ; col++) {
                int pixel = pixels[index];//获取图像所有像素
                a = (pixel>>24)&0xff;
                r = (pixel>>16)&0xff;//把通道取出来
                g = (pixel>>8)&0xff;
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

    /**
     *像素相减
     * @param bitmap
     * @return
     */
    public static Bitmap subtract(Bitmap bitmap) {
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat whiteImg = new Mat(src.size(), src.type(), Scalar.all(255));//白色图片
        long startTime = System.currentTimeMillis();
        Core.subtract(whiteImg, src, src);//相减
        long end = System.currentTimeMillis() - startTime;
        Log.i("Subtract TIME", "\t" + end);
        Utils.matToBitmap(src, bitmap);
        src.release();
        return bitmap;
    }

    /**
     * 像素加法
     * @param bitmap
     */
    public static void add(Bitmap bitmap) {
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat whiteImg = new Mat(src.size(), src.type(), Scalar.all(255));//255白色图片--变白；0黑色图片--变黑
        long startTime = System.currentTimeMillis();
        Core.addWeighted(whiteImg, 0.5, src, 0.5, 0.0, src);//相加
        long end = System.currentTimeMillis() - startTime;
        Log.i("Add TIME", "\t" + end);
        Utils.matToBitmap(src, bitmap);
        src.release();
        whiteImg.release();
    }

    /**
     * 调整对比度
     * @param bitmap
     */
    public static void adjustContrast(Bitmap bitmap) {
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        src.convertTo(src, CvType.CV_32F);
        Mat whiteImg = new Mat(src.size(), src.type(), Scalar.all(1.25));//对比度调整1.25
        Mat bgImg = new Mat(src.size(), src.type(), Scalar.all(30));//背景调整30
        long startTime = System.currentTimeMillis();
        Core.multiply(whiteImg, src, src);
        Core.add(bgImg, src, src);
        long end = System.currentTimeMillis() - startTime;
        Log.i("AdjustContrast TIME", "\t" + end);
        src.convertTo(src, CvType.CV_8U);
        Utils.matToBitmap(src, bitmap);
        src.release();
        whiteImg.release();
        bgImg.release();
    }

    /**
     * 图像容器-MAT演示
     * @return
     */
    public static Bitmap demoMatUsage() {
        Bitmap bitmap = Bitmap.createBitmap(400, 600, Bitmap.Config.ARGB_8888);
        Mat dst = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1, new Scalar(100));//单通道
        Mat dst3 = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC3, new Scalar(124, 36, 86));//多通道
        Utils.matToBitmap(dst3, bitmap);
        dst3.release();
        return bitmap;
    }

    /**
     * 图像容器-获取子图
     * @return
     */
    public static Bitmap getROIArea(Bitmap bitmap) {
        Rect roi = new Rect(200, 150, 200, 300);//截取图片矩形的起始位置和大小
        Bitmap roimap = Bitmap.createBitmap(roi.width, roi.height, Bitmap.Config.ARGB_8888);
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat roiMat = src.submat(roi);
        Mat roiDstMat = new Mat();
        Imgproc.cvtColor(roiMat, roiDstMat, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(roiDstMat, roimap);
        roiDstMat.release();
        roiMat.release();
        src.release();
        return roimap;
    }

    /**
     * 均值模糊
     * @param bitmap
     */
    public static void meanBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.blur(src, dst, new Size(30, 30), new Point(-1, -1), Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 高斯模糊
     * @param bitmap
     */
    public static void gaussianBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.GaussianBlur(src, dst, new Size(5, 5), 0, 0, 4);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 双边模糊
     * @param bitmap
     */
    public static void biBlur(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2BGR);
        Imgproc.bilateralFilter(src, dst, 15, 150, 15, Imgproc.BORDER_DEFAULT);//双边模糊
        Mat kernel = new Mat(3, 3, CvType.CV_16S);//锐化
        kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);//锐化算子
        Imgproc.filter2D(dst, dst, -1, kernel, new Point(-1, -1), 0.0, 4);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 自定义算子-模糊
     * @param command
     * @param bitmap
     */
    public static void customFilter(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat kernel = getCustomOperator(command);
        Imgproc.filter2D(src, dst, -1, kernel, new Point(-1, -1), 0.0, 4);
        Utils.matToBitmap(dst, bitmap);
        kernel.release();
        src.release();
        dst.release();
    }

    private static Mat getCustomOperator(String command) {
        Mat kernel = new Mat(3, 3, CvType.CV_32FC1);//为了中心化宽高最好是奇数，32--对应double float，16--int
        if(CUSTOM_BLUR_COMMAND.equals(command)) {
            kernel.put(0, 0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0);
        } else if(CUSTOM_EDGE_COMMAND.equals(command)) {
            kernel.put(0, 0, -1, -1, -1, -1, 8, -1, -1, -1, -1);
        } else if(CUSTOM_SHARPEN_COMMAND.equals(command)) {
            kernel.put(0, 0, -1, -1, -1, -1, 9, -1, -1, -1, -1);
        }
        return kernel;
    }

    /**
     * 腐蚀和膨胀
     * @param command
     * @param bitmap
     */
    public static void erodOrDilate(String command, Bitmap bitmap) {
        boolean erode = command.equals(CommandConstants.ERODE_COMMAND);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat strElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));//MORPH_RECT结构元素
        if(erode) {
            Imgproc.erode(src, dst, strElement, new Point(-1, -1), 5);//腐蚀
        } else {
            Imgproc.dilate(src, dst, strElement, new Point(-1, -1), 1);//膨胀
        }
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 开闭操作
     * @param command
     * @param bitmap
     */
    public static void openOrClose(String command, Bitmap bitmap) {
        boolean open = command.equals(CommandConstants.OPEN_COMMAND);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        Mat strElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        if(open) {
            Imgproc.morphologyEx(src, dst, Imgproc.MORPH_OPEN, strElement);
        } else {
            Imgproc.morphologyEx(src, dst, Imgproc.MORPH_CLOSE, strElement);
        }
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 直线检测
     * @param bitmap
     */
    public static void morphLineDetection(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);//阈值
        Mat strElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(40, 1), new Point(-1, -1));
        Imgproc.morphologyEx(src, dst, Imgproc.MORPH_OPEN, strElement);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    /**
     * 阈值二值化
     * @param command
     * @param bitmap
     */
    public static void thresholdImg(String command, Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(src, dst, 0, 255, getType(command));
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
    }

    private static int getType(String command) {
        if(CommandConstants.THRESHOLD_BINARY_COMMAND.equals(command)) {
            return (Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        } else if(CommandConstants.THRESHOLD_BINARY_INV_COMMAND.equals(command)) {
            return (Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        } else if(CommandConstants.THRESHOLD_TRUNCAT_COMMAND.equals(command)) {
            return (Imgproc.THRESH_TRUNC | Imgproc.THRESH_OTSU);
        } else if(CommandConstants.THRESHOLD_ZERO_COMMAND.equals(command)) {
            return (Imgproc.THRESH_TOZERO | Imgproc.THRESH_OTSU);
        } else if(CommandConstants.THRESHOLD_ZERO_INV_COMMAND.equals(command)) {
            return (Imgproc.THRESH_TOZERO_INV | Imgproc.THRESH_OTSU);
        } else {
            return (Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        }
    }
}
