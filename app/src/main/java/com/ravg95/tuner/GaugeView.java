package com.ravg95.tuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by rafal on 16/05/2018.
 */

public class GaugeView extends TunerView {

    public GaugeView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void drawView(Canvas canvas) {
        float CIRCLE_CX = (float) (getWidth()/2.0);
        float CIRCLE_CY = (float) 170 + CIRCLE_CX;
        float CIRCLE_R1 = (float) (getWidth()/2.0) - 50;
        float CIRCLE_R2 = CIRCLE_R1 + 40;
        float CIRCLE_BAR_MARGIN = 10;
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        canvas.drawText(note, getWidth()/2 - 40, 90,  paint);
        paint.setTextSize(40);
        canvas.drawText(freq, getWidth()/2 - 35, 140,  paint);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), -135, 90, true, paint);
        //arc in arc to represent distnce
        paint.setStyle(Paint.Style.STROKE);
        if(dist<-Settings.getToleranceInCents() || dist>Settings.getToleranceInCents()) paint.setColor(Color.RED);
        else paint.setColor(Color.GREEN);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), -135, 90, true, paint);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), (float)(-135 + 90*(dist + 50)/100),(float)( 90 - 90*(dist + 50)/100), true, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(lastNote == null || !lastNote.equals(note)){
            lastNote = note;
            angle = 0;
        }   else {
            angle+=(dist)/10;
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
}
