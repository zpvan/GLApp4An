package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.RunOnGrassRenderer;

public class RunOnGrassActivity extends SingleGraphicActivity {

    private static final String TAG = "RunOnGrassActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new RunOnGrassRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
