package com.ravg95.tuner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public abstract class TunerView extends View {

    protected String freq = "440Hz";
    protected double dist = 0;
    protected String note = "A4";
    protected Bitmap mBitmap;
    protected Canvas mCanvas;
    protected Paint paint;
    Context context;

    public TunerView(Context c, AttributeSet attrs) {
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

    protected String lastNote;
    protected double angle;
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawView(canvas);
    }

    protected abstract void drawView(Canvas canvas);

    public void setPitchProperties(String note, String freq, double dist) {
        this.note = note;
        this.freq = freq;
        this.dist = dist;
    }
}