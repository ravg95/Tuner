package com.ravg95.tuner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

    private String freq = "440Hz", dist = "0";
    private String note = "A4";
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint;
    Context context;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(100, 600, getWidth() - 100, 1400), -135, 90, true, paint);
        paint.setTextSize(200);
        canvas.drawText(note, getWidth()/2 - 100, 300,  paint);
        paint.setTextSize(100);
        canvas.drawText(freq, getWidth()/2 - 100, 500,  paint);
    }

    public void setPitchProperties(String note, String freq, String dist) {
        this.note = note;
        this.freq = freq;
        this.dist = dist;
    }
}