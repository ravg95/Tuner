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

    private String freq = "440Hz";
    private double dist = 0;
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
        paint.setTextSize(80);
        canvas.drawText(note, getWidth()/2 - 40, 70,  paint);
        paint.setTextSize(40);
        canvas.drawText(freq, getWidth()/2 - 20, 120,  paint);
        canvas.drawArc(new RectF(10, 300, getWidth() - 10, 700), -135, 90, true, paint);
        //arc in arc to represent distnce
        paint.setStyle(Paint.Style.STROKE);
        if(dist<-Settings.getToleranceInCents() || dist>Settings.getToleranceInCents()) paint.setColor(Color.RED);
        else paint.setColor(Color.GREEN);
        canvas.drawArc(new RectF(10, 300, getWidth() - 10, 700), -135, 90, true, paint);
        canvas.drawArc(new RectF(10, 300, getWidth() - 10, 700), (float)(-135 + 90*(dist + 50)/100),(float)( 90 - 90*(dist + 50)/100), true, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setPitchProperties(String note, String freq, double dist) {
        this.note = note;
        this.freq = freq;
        this.dist = dist;
    }
}