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

    public static final String IN_TUNE_STRING = "IN TUNE";
    public static final String TUNE_UP_STRING = "TUNE UP";
    public static final String TUNE_DOWN_STRING = "TUNE DOWN";


    @Override
    protected void drawView(Canvas canvas) {
        Preset currentPreset = SettingsManager.getCurrentPreset(getContext());
        DoublePointer distance = new DoublePointer(dist / 100.0, null);
        int indexOfNearestString = findNearestString(note, currentPreset, distance);
        float CIRCLE_CX = (float) (getWidth() / 2.0);
        float CIRCLE_CY = (float) 150 + CIRCLE_CX;
        float CIRCLE_R1 = (float) (getWidth() / 2.0) - 50;
        float CIRCLE_R2 = CIRCLE_R1 + 40;
        float CIRCLE_BAR_MARGIN = 10;
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawText(currentPreset.name, getWidth() / 2 - 6 * currentPreset.name.length(), 90, paint);
        float stringDist = 2 * CIRCLE_R1 / (currentPreset.numOfStrings + 1);
        for (int i = 0; i < currentPreset.numOfStrings; i++) {
            float y = CIRCLE_CY - CIRCLE_R1 + (i + 1) * stringDist;
            float relY = CIRCLE_R1 - (i + 1) * stringDist;
            float x1, x2;
            x1 = CIRCLE_CX - (float) Math.sqrt(CIRCLE_R1 * CIRCLE_R1 - relY * relY);
            x2 = CIRCLE_CX + (float) Math.sqrt(CIRCLE_R1 * CIRCLE_R1 - relY * relY);
            if (i == indexOfNearestString) {
                paint.setTextSize(50);
                if (Math.abs(distance.getValue()) <= SettingsManager.getToleranceInCents(getContext())) {
                    paint.setColor(Color.GREEN);
                    canvas.drawText(IN_TUNE_STRING, getWidth() / 2 - 50, 140, paint);
                } else {
                    paint.setColor(Color.RED);
                    if (distance.getValue() > 0)
                        canvas.drawText(TUNE_UP_STRING, getWidth() / 2 - 50, 140, paint);
                    else
                        canvas.drawText(TUNE_DOWN_STRING, getWidth() / 2 - 50, 140, paint);
                }
                paint.setStyle(Paint.Style.STROKE);
                float startAngle = -180;
                float sweepAngle = 180;
                if (distance.getValue() <= 0) {
                    canvas.drawArc(new RectF(x1, (float) (y + distance.getValue()), x2, y), startAngle, sweepAngle, false, paint);
                }else {
                    canvas.drawArc(new RectF(x1, y, x2, (float) (y + distance.getValue())), startAngle, sweepAngle, false, paint);
                }
                paint.setTextSize(25);
                canvas.drawText(currentPreset.strings[i], x1, y, paint);

            } else {
                paint.setColor(Color.GRAY);
                canvas.drawLine(x1, y, x2, y, paint);
                paint.setTextSize(25);
                canvas.drawText(currentPreset.strings[i], x1, y, paint);
            }
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (lastNote == null || !lastNote.equals(note)) {
            lastNote = note;
            angle = 0;
        } else {
            angle += (dist / 10);
        }
        //draw the rotating circle
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R1, paint);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R2, paint);
        for (int i = 0; i < 360; i += 10) {
            float x1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN) * Math.sin(Math.PI * (angle + i) / 180)) + CIRCLE_CX;
            float y1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN) * Math.cos(Math.PI * (angle + i) / 180)) + CIRCLE_CY;
            float x2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN) * Math.sin(Math.PI * (angle + i) / 180)) + CIRCLE_CX;
            float y2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN) * Math.cos(Math.PI * (angle + i) / 180)) + CIRCLE_CY;
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

    private int findNearestString(String note, Preset currentPreset, DoublePointer distance) {
        double[] distances = new double[currentPreset.numOfStrings];
        int semiNote = ToneAnalyzer.getSemitonesFromNoteName(note);
        double minDist = Double.MAX_VALUE;
        int retIndex = 0;
        for (int i = 0; i < currentPreset.numOfStrings; i++) {
            int semiString = ToneAnalyzer.getSemitonesFromNoteName(currentPreset.strings[i]);
            distances[i] = semiNote - semiString + distance.getValue();
            if (Math.abs(distances[i]) < minDist) {
                minDist = distances[i];
                retIndex = i;
            }
        }
        distance.setValue(minDist);
        return retIndex;
    }
}