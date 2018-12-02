package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.TrsTriangleRenderer;

public class TrsTriActivity extends SingleGraphicActivity {

    private static final String TAG = "TrsTriActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new TrsTriangleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
