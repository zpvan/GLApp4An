package com.example.knox.gldemo.utils;

import android.view.MotionEvent;

public class MotionEventUtil {
    public static double getDistance(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float dx = event.getX(0) - event.getX(1);
            float dy = event.getY(0) - event.getY(1);
            return Math.sqrt(dx * dx + dy * dy);
        }
        return 0f;
    }

    public static float getRotation(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            double dx = event.getX(0) - event.getX(1);
            double dy = event.getY(0) - event.getY(1);
            double radians = Math.atan2(dy, dx);
            return (float) Math.toDegrees(radians);
        }
        return 0f;
    }
}
