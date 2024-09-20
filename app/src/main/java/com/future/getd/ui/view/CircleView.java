package com.future.getd.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.future.getd.R;


public class CircleView extends ImageView {
    private Paint mPaint; //画笔

    private int mRadius; //圆形图片的半径

    private float mScale; //图片的缩放比例

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        int bg = a.getColor(R.styleable.CircleView_background_color,0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bg);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(int color){
        mPaint.setColor(color);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为是圆形图片，所以应该让宽高保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Bitmap bitmap = drawableToBitmap(getDrawable());
//
//        //初始化BitmapShader，传入bitmap对象
//        if(bitmap != null){
//            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//            //计算缩放比例
//            mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());
//
//            Matrix matrix = new Matrix();
//            matrix.setScale(mScale, mScale);
//            bitmapShader.setLocalMatrix(matrix);
//
//            mPaint.setShader(bitmapShader);
//        }

        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }

    //写一个drawble转BitMap的方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
