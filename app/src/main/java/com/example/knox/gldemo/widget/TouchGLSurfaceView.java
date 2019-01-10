package com.example.knox.gldemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.knox.gldemo.utils.MotionEventUtil;

/**
 * @author Knox.Tsang
 * @time 2019/1/10  18:23
 * @desc ${TODD}
 *
 *
 *
 */

public class TouchGLSurfaceView extends GLSurfaceView {

    private static final String TAG = "TouchGLSurfaceView";

    private int     mPointCount;
    private float   mDownX;
    private float   mDownY;
    private double  mOldDist;
    private float   mOldRota;
    private boolean mDoScaleOrRotate;
    private boolean mCancel;

    private TouchEventCallback mCallback;

    public TouchGLSurfaceView(Context context) {
        super(context, null);
    }

    public TouchGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTouchEventCallback(TouchEventCallback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mPointCount = 1;
                mDownX = event.getX();
                mDownY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCancel) {
                    break;
                }
                if (mPointCount == 1 && !mDoScaleOrRotate) {
                    // one fingle
                    float dx = event.getX() - mDownX;
                    float dy = mDownY - event.getY();
                    mDownX = event.getX();
                    mDownY = event.getY();
//                    Log.e(TAG, "onTouchEvent: ACTION_MOVE [dx, dy]=[" + dx + ", " + dy + "]");
                    if (mCallback != null) {
                        mCallback.moveCallback(dx, dy);
                    }
                } else if (mPointCount == 2) {
                    // two fingle
                    double newDist = MotionEventUtil.getDistance(event);
                    double scale = 0;
                    if (mOldDist != 0) {
                        scale = newDist / mOldDist;
//                        Log.e(TAG, "onTouchEvent: ACTION_MOVE [scale]=[" + scale + "]");
                        if (mCallback != null) {
                            mCallback.scaleCallback(scale);
                        }
                    }

                    float newRota = MotionEventUtil.getRotation(event);
                    float rotate = newRota - mOldRota;
//                    Log.e(TAG, "onTouchEvent: ACTION_MOVE [rotate]=[" + rotate + "]");
                    if (mCallback != null) {
                        mCallback.rotateCallback(rotate);
                    }

                }
                break;

            case MotionEvent.ACTION_UP:
                mPointCount = 0;
                mDownX = 0;
                mDownY = 0;
                mDoScaleOrRotate = false;
                mCancel = false;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mPointCount += 1;
                mOldDist = MotionEventUtil.getDistance(event);
                mOldRota = MotionEventUtil.getRotation(event);
                if (mPointCount == 2) {
                    mDoScaleOrRotate = true;
                } else if (mPointCount > 2) {
                    mCancel = true;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mPointCount -= 1;
                break;
        }
        return true;
    }
}
