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

public class StringView extends TunerView {

    public StringView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void drawView(Canvas canvas) {
        Preset currentPreset = Settings.getCurrentPreset();

        float CIRCLE_CX = (float) (getWidth()/2.0);
        float CIRCLE_CY = (float) 150 + CIRCLE_CX;
        float CIRCLE_R1 = (float) (getWidth()/2.0) - 50;
        float CIRCLE_R2 = CIRCLE_R1 + 40;
        float CIRCLE_BAR_MARGIN = 10;
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawText(currentPreset.name, getWidth()/2 - 6*currentPreset.name.length(), 90,  paint);
        float stringDist = 2 * CIRCLE_R1 / (currentPreset.numOfStrings + 1);
        for(int i=0; i < currentPreset.numOfStrings; i++){
            float y = CIRCLE_CY - CIRCLE_R1 + (i + 1) * stringDist;
            float relY = CIRCLE_R1 - (i + 1) * stringDist;
            float x1, x2;
            x1 = CIRCLE_CX - (float) Math.sqrt(CIRCLE_R1*CIRCLE_R1 - relY*relY);
            x2 = CIRCLE_CX + (float) Math.sqrt(CIRCLE_R1*CIRCLE_R1 - relY*relY);
            paint.setColor(Color.GRAY);
            canvas.drawLine(x1, y, x2, y, paint);
            canvas.drawText(currentPreset.strings[i], x1, y,  paint);

        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if(lastNote == null || !lastNote.equals(note)){
            lastNote = note;
            angle = 0;
        }   else {
            angle+=(dist/10);
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