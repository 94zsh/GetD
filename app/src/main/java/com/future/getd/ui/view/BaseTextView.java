package com.future.getd.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class BaseTextView extends TextView {

    public BaseTextView(Context context) {
        super(context);
        init(context);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        if (LocaleUtil.isEn(context)) {
//            AssetManager assets = context.getAssets();
//            Typeface typeface = Typeface.createFromAsset(assets, "fonts/Arial.ttf");
//            setTypeface(typeface);
//            setIncludeFontPadding(false);
//        }
    }
}
