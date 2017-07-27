package com.lqh.lichao.myopencv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants;
import com.lqh.lichao.myopencv.com.lqh.lichao.util.ImageProcessUtils;
import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProcessImageActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "CVLib";
    private int MAX_SIZE = 768;
    private int REQUEST_GET_IMAGE = 1;
    private String command;
    private Bitmap selectedBitmap;
    private CascadeClassifier faceDetector;
    private Button testBtn;
    private Button selecBbtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initLoadOpenCVLib();
        try {
            initFaceDetector();
        } catch (IOException ioe) {
            Log.i("IOERR", ioe.getMessage());
        }
        init();
    }

    private void init() {
        command = this.getIntent().getStringExtra("command");
        testBtn = (Button) findViewById(R.id.test_button);
        selecBbtn = (Button) findViewById(R.id.select_imgButton);
        imageView = (ImageView) findViewById(R.id.test_imageView);

        testBtn.setTag("PROCESS");
        testBtn.setText(command);
        testBtn.setOnClickListener(this);
        selecBbtn.setOnClickListener(this);

        selectedBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.test1);
        imageView.setImageBitmap(selectedBitmap);
    }

    private void initFaceDetector() throws IOException {
        InputStream input = getResources().openRawResource(R.raw.lbpcascade_frontalface);
        File cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE);
        File file = new File(cascadeDir.getAbsoluteFile(), "lbpcascade_frontalface.xml");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len = input.read(buff)) != -1) {
            output.write(buff, 0, len);
        }
        input.close();
        output.close();
        faceDetector = new CascadeClassifier(file.getAbsolutePath());
        file.delete();
        cascadeDir.delete();
    }

    private void initLoadOpenCVLib() {
        boolean success = OpenCVLoader.initDebug();
        if(success) {
            Log.i(TAG, "load library successfully");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
                int height = options.outHeight;
                int width = options.outWidth;
                int sampleSize = 1;
                int max = Math.max(height, width);
                if(max > MAX_SIZE) {
                    int nw = width / 2;
                    int nh = height / 2;
                    while ((nw / sampleSize) > MAX_SIZE || (nh / sampleSize) > MAX_SIZE) {
                        sampleSize *= 2;
                    }
                }
                options.inSampleSize = sampleSize;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                selectedBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                imageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_button:
                processCommand();
                break;
            case R.id.select_imgButton:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Browser Image..."), REQUEST_GET_IMAGE);
    }

    private void processCommand() {
        Bitmap temp = selectedBitmap.copy(selectedBitmap.getConfig(), true);
        if(CommandConstants.TEST_ENV_COMMAND.equals(command)) {
            temp = ImageProcessUtils.convert2Gray(temp);
        } else if(CommandConstants.MAT_PIXEL_INVERT_COMMAND.equals(command)) {
            temp = ImageProcessUtils.invert(temp);
        } else if(CommandConstants.BITMAP_PIXEL_INVERT_COMMAND.equals(command)) {
            ImageProcessUtils.localInvert(temp);
        } else if(CommandConstants.PIXEL_SUBSTRACT_COMMAND.equals(command)) {
            ImageProcessUtils.subtract(temp);
        } else if(CommandConstants.PIXEL_ADD_COMMAND.equals(command)) {
            ImageProcessUtils.add(temp);
        } else if(CommandConstants.ADJUST_CONTRAST_COMMAND.equals(command)) {
            ImageProcessUtils.adjustContrast(temp);
        } else if(CommandConstants.IMAGE_CONTAINER_COMMAND.equals(command)) {
            temp = ImageProcessUtils.demoMatUsage();
        } else if(CommandConstants.SUB_IMAGE_COMMAND.equals(command)) {
            temp = ImageProcessUtils.getROIArea(temp);
        } else if(CommandConstants.BLUR_IMAGE_COMMAND.equals(command)) {
            ImageProcessUtils.meanBlur(temp);
        } else if(CommandConstants.GAUSSIAN_BLUR_COMMAND.equals(command)) {
            ImageProcessUtils.gaussianBlur(temp);
        } else if(CommandConstants.BI_BLUR_COMMAND.equals(command)) {
            ImageProcessUtils.biBlur(temp);
        } else if(CommandConstants.CUSTOM_BLUR_COMMAND.equals(command)||
                CommandConstants.CUSTOM_EDGE_COMMAND.equals(command)||
                CommandConstants.CUSTOM_SHARPEN_COMMAND.equals(command)) {
            ImageProcessUtils.customFilter(command, temp);
        } else if(CommandConstants.ERODE_COMMAND.equals(command)||
                CommandConstants.DILATE_COMMAND.equals(command)) {
            ImageProcessUtils.erodOrDilate(command, temp);
        } else if(CommandConstants.OPEN_COMMAND.equals(command)||
                CommandConstants.CLOSE_COMMAND.equals(command)) {
            ImageProcessUtils.openOrClose(command, temp);
        } else if(CommandConstants.MORPH_LINE_COMMAND.equals(command)) {
            ImageProcessUtils.morphLineDetection(temp);
        } else if(CommandConstants.THRESHOLD_BINARY_COMMAND.equals(command)||
                CommandConstants.THRESHOLD_BINARY_INV_COMMAND.equals(command)||
                CommandConstants.THRESHOLD_TRUNCAT_COMMAND.equals(command)||
                CommandConstants.THRESHOLD_ZERO_COMMAND.equals(command) ||
                CommandConstants.THRESHOLD_ZERO_INV_COMMAND.equals(command)) {
            ImageProcessUtils.thresholdImg(command, temp);
        } else if(CommandConstants.HISTOGRAM_EQ_COMMAND.equals(command)) {
            ImageProcessUtils.histogramEq(temp);
        } else if(CommandConstants.GRADIENT_SOBEL_X_COMMAND.equals(command)) {
            ImageProcessUtils.sobleGradient(temp, 1);
        } else if(CommandConstants.GRADIENT_SOBEL_Y_COMMAND.equals(command)) {
            ImageProcessUtils.sobleGradient(temp, 2);
        } else if(CommandConstants.GRADIENT_IMG_COMMAND.equals(command)) {
            ImageProcessUtils.sobleGradient(temp, 3);
        } else if(CommandConstants.TEMPLATE_MATCH_COMMAND.equals(command)) {
            Bitmap tpl = BitmapFactory.decodeResource(this.getResources(), R.drawable.sample);
            ImageProcessUtils.templateMatchDemo(tpl, temp);
        } else if(CommandConstants.FIND_FACE_COMMAND.equals(command)) {
            // TODO:zhigang
            ImageProcessUtils.faceDetect(temp, faceDetector);
        }
        imageView.setImageBitmap(temp);
    }

}
