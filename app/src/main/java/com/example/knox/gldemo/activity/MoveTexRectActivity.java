package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.MoveTexRectRenderer;

public class MoveTexRectActivity extends SingleGraphicActivity {

    private static final String TAG = "MoveTexRectActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new MoveTexRectRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
