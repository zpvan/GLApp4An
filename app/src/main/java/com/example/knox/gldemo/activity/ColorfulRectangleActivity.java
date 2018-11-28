package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.ColorfulRectangleRenderer;

public class ColorfulRectangleActivity extends SingleGraphicActivity {

    private static final String TAG = "ColorfulRectangleActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new ColorfulRectangleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
