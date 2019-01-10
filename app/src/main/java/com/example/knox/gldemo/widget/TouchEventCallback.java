package com.example.knox.gldemo.widget;

/**
 * @author Knox.Tsang
 * @time 2019/1/10  19:03
 * @desc ${TODD}
 */

public interface TouchEventCallback {
    void moveCallback(float x, float y);
    void scaleCallback(double scale);
    void rotateCallback(float rotate);
}
