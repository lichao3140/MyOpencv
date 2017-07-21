package com.lqh.lichao.myopencv.com.lqh.lichao.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-20.
 */

public class CommandData extends CommandConstants {
    private long id;
    private String command;
    private String name;

    public CommandData(String command, long id) {
        this.id = id;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<CommandData> getCommandList() {
        List<CommandData> cList = new ArrayList<>();
        cList.add(new CommandData(TEST_ENV_COMMAND, 1));
        cList.add(new CommandData(MAT_PIXEL_INVERT_COMMAND, 2));
        cList.add(new CommandData(BITMAP_PIXEL_INVERT_COMMAND, 3));
        cList.add(new CommandData(PIXEL_SUBSTRACT_COMMAND, 4));
        cList.add(new CommandData(PIXEL_ADD_COMMAND, 5));
        cList.add(new CommandData(ADJUST_CONTRAST_COMMAND, 6));
        cList.add(new CommandData(IMAGE_CONTAINER_COMMAND, 7));
        cList.add(new CommandData(SUB_IMAGE_COMMAND, 8));
        cList.add(new CommandData(BLUR_IMAGE_COMMAND, 9));
        cList.add(new CommandData(GAUSSIAN_BLUR_COMMAND, 10));
        cList.add(new CommandData(BI_BLUR_COMMAND, 11));
        cList.add(new CommandData(CUSTOM_BLUR_COMMAND, 12));
        cList.add(new CommandData(CUSTOM_EDGE_COMMAND, 13));
        cList.add(new CommandData(CUSTOM_SHARPEN_COMMAND, 14));
        cList.add(new CommandData(ERODE_COMMAND, 15));
        cList.add(new CommandData(DILATE_COMMAND, 16));
        cList.add(new CommandData(OPEN_COMMAND, 17));
        cList.add(new CommandData(CLOSE_COMMAND, 18));
        cList.add(new CommandData(MORPH_LINE_COMMAND, 19));
        cList.add(new CommandData(THRESHOLD_BINARY_COMMAND, 20));
        cList.add(new CommandData(THRESHOLD_BINARY_INV_COMMAND, 21));
        cList.add(new CommandData(THRESHOLD_TRUNCAT_COMMAND, 22));
        cList.add(new CommandData(THRESHOLD_ZERO_COMMAND, 23));
        cList.add(new CommandData(THRESHOLD_ZERO_INV_COMMAND, 24));
        // 自适应阈值
        cList.add(new CommandData(ADAPTIVE_THRESHOLD_COMMAND, 25));
        cList.add(new CommandData(ADAPTIVE_GAUSSIAN_COMMAND, 26));
        // 直方图均衡化
        cList.add(new CommandData(HISTOGRAM_EQ_COMMAND, 27));
        // 图像梯度
        cList.add(new CommandData(GRADIENT_SOBEL_X_COMMAND, 28));
        cList.add(new CommandData(GRADIENT_SOBEL_Y_COMMAND, 29));
        cList.add(new CommandData(GRADIENT_IMG_COMMAND, 30));
        // 边缘提取
        cList.add(new CommandData(CANNY_EDGE_COMMAND, 31));
        // 霍夫变换
        cList.add(new CommandData(HOUGH_LINES_COMMAND, 32));
        cList.add(new CommandData(HOUGH_CIRCLE_COMMAND, 33));
        // 模板匹配
        cList.add(new CommandData(TEMPLATE_MATCH_COMMAND, 34));
        // 轮廓发现
        cList.add(new CommandData(FIND_CONTOURS_COMMAND, 35));
        // 对象测量
        cList.add(new CommandData(MEASURE_OBJECT_COMMAND, 36));
        // 人脸检测
        cList.add(new CommandData(FIND_FACE_COMMAND, 37));
        return cList;
    }
}
