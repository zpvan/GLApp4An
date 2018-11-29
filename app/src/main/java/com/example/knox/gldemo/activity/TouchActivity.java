package com.example.knox.gldemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.MotionEventUtil;

/**
 * translate with one fingle
 * scale     with two fingle
 * rotate    with two fingle
 */
public class TouchActivity extends AppCompatActivity {

    private static final String TAG = "TouchActivity";

    private int     mPointCount;
    private float   mDownX;
    private float   mDownY;
    private double  mOldDist;
    private float   mOldRota;
    private boolean mDoScaleOrRotate;
    private boolean mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
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
                    Log.e(TAG, "onTouchEvent: ACTION_MOVE [dx, dy]=[" + dx + ", " + dy + "]");
                } else if (mPointCount == 2) {
                    // two fingle
                    double newDist = MotionEventUtil.getDistance(event);
                    double scale = 0;
                    if (mOldDist != 0) {
                        scale = newDist / mOldDist;
                        Log.e(TAG, "onTouchEvent: ACTION_MOVE [scale]=[" + scale + "]");
                    }

                    float newRota = MotionEventUtil.getRotation(event);
                    float rotate = newRota - mOldRota;
                    Log.e(TAG, "onTouchEvent: ACTION_MOVE [rotate]=[" + rotate + "]");

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
