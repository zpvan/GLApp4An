package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.renderer.ColorfulRectangleRenderer;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.widget.NamedTextView;

public class ColorfulRectangleActivity extends AppCompatActivity {

    private static final String TAG = "ColorfulRectangleActivity";

    private GLSurfaceView mGlSurfaceView;
    private boolean rendererSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorful_rectangle);

        FrameLayout rootFl = findViewById(R.id.rl_fl_rec);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new GLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(2);
            mGlSurfaceView.setRenderer(new ColorfulRectangleRenderer(this));
            rootFl.addView(mGlSurfaceView);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
        }

        NamedTextView namedTextView = new NamedTextView(this, TAG);
        rootFl.addView(namedTextView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) mGlSurfaceView.onResume();
    }
}
