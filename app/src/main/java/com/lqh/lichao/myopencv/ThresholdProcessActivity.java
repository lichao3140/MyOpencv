package com.lqh.lichao.myopencv;

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
import android.widget.SeekBar;
import android.widget.TextView;

import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants;
import com.lqh.lichao.myopencv.com.lqh.lichao.util.ImageProcessUtils;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;

public class ThresholdProcessActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private String TAG = "CVLib";
    private int MAX_SIZE = 768;
    private int REQUEST_GET_IMAGE = 1;
    private String command;
    private Bitmap selectedBitmap;
    private Button testBtn;
    private Button selecBbtn;
    private ImageView imageView;
    private SeekBar seekBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_process);
        initLoadOpenCVLib();
        init();
    }

    private void init() {
        command = this.getIntent().getStringExtra("command");
        testBtn = (Button) findViewById(R.id.t_processButton);
        selecBbtn = (Button) findViewById(R.id.t_select_imgButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        seekBar = (SeekBar) findViewById(R.id.seekBarView) ;
        textView = (TextView) findViewById(R.id.seekBarValueTxtView);

        testBtn.setTag("PROCESS");
        testBtn.setText(command);
        testBtn.setOnClickListener(this);
        selecBbtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        //轮廓发现
        selectedBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.happyfish);
        imageView.setImageBitmap(selectedBitmap);
        textView.setText("当前阈值为: " + seekBar.getProgress());
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
            case R.id.t_processButton:
                processCommand(seekBar.getProgress());
                break;
            case R.id.t_select_imgButton:
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

    private void processCommand(int t) {
        if((t % 2) == 0) {
            t = t + 1;
        }
        Bitmap temp = selectedBitmap.copy(selectedBitmap.getConfig(), true);
        if(CommandConstants.THRESHOLD_BINARY_COMMAND.equals(command)) {
            ImageProcessUtils.manualThresholdBinary(t, temp);
        } else if(CommandConstants.ADAPTIVE_THRESHOLD_COMMAND.equals(command)) {
            ImageProcessUtils.adaptiveThresholdBinary(t, temp, false);
        } else if(CommandConstants.ADAPTIVE_GAUSSIAN_COMMAND.equals(command)) {
            ImageProcessUtils.adaptiveThresholdBinary(t, temp, true);
        } else if(CommandConstants.CANNY_EDGE_COMMAND.equals(command)) {
            ImageProcessUtils.cannyEdge(t, temp);
        } else if(CommandConstants.HOUGH_LINES_COMMAND.equals(command)) {
            ImageProcessUtils.houghLinesDet(t, temp);
        } else if(CommandConstants.HOUGH_CIRCLE_COMMAND.equals(command)) {
            ImageProcessUtils.houghCircleDet(t, temp);
        } else if(CommandConstants.FIND_CONTOURS_COMMAND.equals(command)) {
            ImageProcessUtils.findAndDrawContours(t, temp);
        } else if(CommandConstants.MEASURE_OBJECT_COMMAND.equals(command)) {
            double[][] results = ImageProcessUtils.measureObjects(t, temp);
            for (int i = 0; i < results.length; i++) {
                Log.i("Measure Data:", "第 " + i + " 轮廓");
                Log.i("Measure Data:", "周长: " + results[i][0]);
                Log.i("Measure Data:", "面积: " + results[i][1]);
            }
        }
        imageView.setImageBitmap(temp);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int value = seekBar.getProgress();
        textView.setText("当前阈值为: " + value);
        processCommand(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
