package com.example.knox.gldemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class NamedTextView extends TextView {

    private final String mText;

    public NamedTextView(Context context, @NonNull String text) {
        super(context, null);
        mText = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(getText())) {
            setTextSize(30f);
            setTextColor(Color.GRAY);
            setText(mText);
        }
        super.onDraw(canvas);
    }
}
