package com.example.knox.gldemo.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.renderer.RunOnGrassRenderer;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.widget.ButtonCallback;
import com.example.knox.gldemo.widget.NamedTextView;
import com.example.knox.gldemo.widget.TouchGLSurfaceView;

import java.util.ArrayList;

public class RunOnGrassActivity extends AppCompatActivity {

    private static final String TAG = "RunOnGrassActivity";

    protected TouchGLSurfaceView mGlSurfaceView;
    private RunOnGrassRenderer mRunOnGrassRenderer;
    protected boolean rendererSet;

    private RelativeLayout mRootLayout;
    private boolean openLens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mRootLayout = new RelativeLayout(this);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new TouchGLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(2);
            mRunOnGrassRenderer = new RunOnGrassRenderer(this);
            mGlSurfaceView.setRenderer(mRunOnGrassRenderer);
            mGlSurfaceView.setClickable(true);
            mGlSurfaceView.setTouchEventCallback(mRunOnGrassRenderer);
            mRootLayout.addView(mGlSurfaceView);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
        }

        NamedTextView namedTextView = new NamedTextView(this, TAG);
        mRootLayout.addView(namedTextView);

        setPowerLens(this, mRootLayout, mRunOnGrassRenderer);

        setContentView(mRootLayout);
    }

    private void setPowerLens(Context context, RelativeLayout rootLayout, final ButtonCallback callback) {
        View linearlayout_power_lens = LayoutInflater.from(this).inflate(R.layout.linearlayout_power_lens, null);
        float pxDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) pxDimension, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rootLayout.addView(linearlayout_power_lens, layoutParams);

        ArrayList<Button> btns =  new ArrayList<>();
        btns.add((Button) linearlayout_power_lens.findViewById(R.id.scope_2));
        btns.add((Button) linearlayout_power_lens.findViewById(R.id.scope_3));
        btns.add((Button) linearlayout_power_lens.findViewById(R.id.scope_4));
        btns.add((Button) linearlayout_power_lens.findViewById(R.id.scope_6));
        btns.add((Button) linearlayout_power_lens.findViewById(R.id.scope_8));
        if (btns.size() == 0) {
            Log.e(TAG, "setPowerLens: no x btn");
            return;
        }
        openLens = false;
        for (int i = 0; i < btns.size(); i++) {
            Button button = btns.get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof Button) {
                        CharSequence text = ((Button) v).getText();
                        char c = text.charAt(0);
                        try {
                            int ic = Integer.valueOf(String.valueOf(c));
                            if (ic > 0 && ic < 10 && callback != null) {
                                callback.setXScope(ic, !openLens);
                                openLens = !openLens;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
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
