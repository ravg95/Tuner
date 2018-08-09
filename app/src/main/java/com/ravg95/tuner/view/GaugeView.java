package com.ravg95.tuner.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.ravg95.tuner.data.SettingsManager;

/**
 * Created by rafal on 16/05/2018.
 */

public class GaugeView extends TunerView {

    public GaugeView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void drawView(Canvas canvas) {
        initConstants();
        canvas.drawColor(Color.BLACK);
        drawNote(canvas);
        drawGauge(canvas);
        drawRotatingCircle(canvas);
    }

    private void drawNote(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        canvas.drawText(tunerViewPresenter.getNote(), getWidth() / 2 - 40, 90, paint);
        paint.setTextSize(40);
        canvas.drawText(tunerViewPresenter.getFreq(), getWidth() / 2 - 35, 140, paint);
    }

    private void drawGauge(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), -135, 90, true, paint);
        //arc in arc to represent distnce
        paint.setStyle(Paint.Style.STROKE);
        if (tunerViewPresenter.getDist() < -SettingsManager.getToleranceInCents(getContext()) || tunerViewPresenter.getDist() > SettingsManager.getToleranceInCents(getContext()))
            paint.setColor(Color.RED);
        else paint.setColor(Color.GREEN);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), -135, 90, true, paint);
        canvas.drawArc(new RectF(50, 220, getWidth() - 50, 120 + getWidth()), (float) (-135 + 90 * (tunerViewPresenter.getDist() + 50) / 100), (float) (90 - 90 * (tunerViewPresenter.getDist() + 50) / 100), true, paint);
    }


}
