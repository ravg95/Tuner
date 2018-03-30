package com.ravg95.tuner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View {

    private final float CIRCLE_CX = (float) 225;
    private final float CIRCLE_CY = (float) 325;
    private final float CIRCLE_R1 = 100;
    private final float CIRCLE_R2 = 150;
    private final float CIRCLE_BAR_MARGIN = 10;
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

    String lastNote;
    double angle;
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
        if(lastNote == null || !lastNote.equals(note)){
            lastNote = note;
            angle = 0;
        }   else {
            angle+=(dist*10.0);
        }
        //draw the rotating circle
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R1, paint);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R2, paint);
        for(int i=0; i < 360 ; i+= 10){
            float x1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN)*Math.sin(Math.PI*(angle+i)/180)) + CIRCLE_CX;
            float y1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN)*Math.cos(Math.PI*(angle+i)/180)) + CIRCLE_CY;
            float x2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN)*Math.sin(Math.PI*(angle+i)/180)) + CIRCLE_CX;
            float y2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN)*Math.cos(Math.PI*(angle+i)/180)) + CIRCLE_CY;
            canvas.drawLine(x1,y1,x2,y2,paint);
        }
    }

    public void setPitchProperties(String note, String freq, double dist) {
        this.note = note;
        this.freq = freq;
        this.dist = dist;
    }
}