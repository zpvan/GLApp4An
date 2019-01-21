package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.SpecularRenderer;

public class SpecularActivity extends SingleGraphicActivity {

    private static final String TAG = "SpecularActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new SpecularRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
