package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.MultiLightRenderer;

public class MultiLightActivity extends SingleGraphicActivity {

    private static final String TAG = "MultiLightActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new MultiLightRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
