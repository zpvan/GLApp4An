package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.TwoTexRectRenderer;

public class TwoTexRectActivity extends SingleGraphicActivity {

    private static final String TAG = "TwoTexRectActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new TwoTexRectRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
