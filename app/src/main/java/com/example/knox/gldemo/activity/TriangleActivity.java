package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.renderer.TriangleRenderer;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.widget.NamedTextView;

public class TriangleActivity extends AppCompatActivity {

    private static final String TAG = "TriangleActivity";

    private boolean       rendererSet;
    private GLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);

        FrameLayout rootFl = findViewById(R.id.rt_fl_tri);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new GLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(2);
            mGlSurfaceView.setRenderer(new TriangleRenderer(this));
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
        if (rendererSet)
            mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet)
            mGlSurfaceView.onResume();
    }
}
