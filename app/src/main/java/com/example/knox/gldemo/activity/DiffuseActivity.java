package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.DiffuseRenderer;

public class DiffuseActivity extends SingleGraphicActivity {

    private static final String TAG = "DiffuseActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new DiffuseRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
