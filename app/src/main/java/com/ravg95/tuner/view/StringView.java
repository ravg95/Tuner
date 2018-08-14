package com.ravg95.tuner.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.ravg95.tuner.tools.SettingsManager;
import com.ravg95.tuner.util.DoublePointer;
import com.ravg95.tuner.util.Preset;

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
        initConstants();
        canvas.drawColor(Color.BLACK);
        drawPresetName(canvas);
        drawStringsAndInstructions(canvas);
        drawRotatingCircle(canvas);

    }



    private void drawPresetName(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawText(tunerViewPresenter.getCurrentPreset(getContext()).getName(), getWidth() / 2 - 6 * tunerViewPresenter.getCurrentPreset(getContext()).getName().length(), 90, paint);
    }

    private void drawStringsAndInstructions(Canvas canvas) {
        Preset currentPreset = tunerViewPresenter.getCurrentPreset(getContext());
        DoublePointer distance = new DoublePointer(tunerViewPresenter.getDist(), null);
        int indexOfNearestString = tunerViewPresenter.findNearestString(tunerViewPresenter.getNote(), currentPreset, distance);
        float stringDist = 2 * CIRCLE_R1 / (currentPreset.getNumOfStrings() + 1);
        for (int i = 0, j = currentPreset.getNumOfStrings()-1; i < currentPreset.getNumOfStrings(); i++, j--) {
            float y = CIRCLE_CY - CIRCLE_R1 + (j + 1) * stringDist;
            float relY = CIRCLE_R1 - (j + 1) * stringDist;
            float x1, x2;
            x1 = CIRCLE_CX - (float) Math.sqrt(CIRCLE_R1 * CIRCLE_R1 - relY * relY);
            x2 = CIRCLE_CX + (float) Math.sqrt(CIRCLE_R1 * CIRCLE_R1 - relY * relY);
            if (i == indexOfNearestString && !tunerViewPresenter.getFreq().equals(EMPTY_FREQ)) {
                paint.setTextSize(50);
                if (Math.abs(distance.getValue()) <= SettingsManager.getToleranceInCents(getContext())) {
                    paint.setColor(Color.GREEN);
                    canvas.drawText(IN_TUNE_STRING, getWidth() / 2 - 50, 140, paint);
                } else {
                    paint.setColor(Color.RED);
                    if (distance.getValue() < 0)
                        canvas.drawText(TUNE_UP_STRING, getWidth() / 2 - 50, 140, paint);
                    else
                        canvas.drawText(TUNE_DOWN_STRING, getWidth() / 2 - 50, 140, paint);
                }
                paint.setStyle(Paint.Style.STROKE);
                drawCurve(x1, y, x2, y, distance.getValue()/10.0, canvas, paint );

                paint.setTextSize(25);
                canvas.drawText(currentPreset.getStrings()[i], x1, y, paint);

            } else {
                paint.setColor(Color.GRAY);
                canvas.drawLine(x1, y, x2, y, paint);
                paint.setTextSize(25);
                canvas.drawText(currentPreset.getStrings()[i], x1, y, paint);
            }
        }
    }

    public void drawCurve(float x1, float y1, float x2, float y2, double curveRadius, Canvas canvas, Paint paint) {

        final Path path = new Path();
        float midX            = x1 + ((x2 - x1) / 2);
        float midY            = y1 + ((y2 - y1) / 2);
        float xDiff         = midX - x1;
        float yDiff         = midY - y1;
        double angle        = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) - 90;
        double angleRadians = Math.toRadians(angle);
        float pointX        = (float) (midX + curveRadius * Math.cos(angleRadians));
        float pointY        = (float) (midY + curveRadius * Math.sin(angleRadians));
        path.moveTo(x1, y1);
        path.cubicTo(x1,y1,pointX, pointY, x2, y2);
        canvas.drawPath(path, paint);
    }



}