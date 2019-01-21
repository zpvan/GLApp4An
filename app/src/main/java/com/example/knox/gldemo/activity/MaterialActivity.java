package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.MaterialRenderer;

public class MaterialActivity extends SingleGraphicActivity {

    private static final String TAG = "MaterialActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new MaterialRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
