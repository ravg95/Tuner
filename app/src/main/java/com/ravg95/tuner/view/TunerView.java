package com.ravg95.tuner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ravg95.tuner.presenter.TunerViewPresenter;

import lombok.Getter;

public abstract class TunerView extends View {

    protected Bitmap mBitmap;
    protected Canvas mCanvas;
    protected Paint paint;

    protected static final String EMPTY_FREQ = " -1,0 Hz";

    Context context;
    @Getter
    TunerViewPresenter tunerViewPresenter;

    float CIRCLE_CX;
    float CIRCLE_CY;
    float CIRCLE_R1;
    float CIRCLE_R2;
    float CIRCLE_BAR_MARGIN;

    public TunerView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        tunerViewPresenter = new TunerViewPresenter();
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void initConstants() {
        CIRCLE_CX = (float) (getWidth() / 2.0);
        CIRCLE_CY = (float) 150 + CIRCLE_CX;
        CIRCLE_R1 = (float) (getWidth() / 2.0) - 50;
        CIRCLE_R2 = CIRCLE_R1 + 40;
        CIRCLE_BAR_MARGIN = 10;
    }

    // override onDraw

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawView(canvas);
    }

    protected abstract void drawView(Canvas canvas);

    public void drawRotatingCircle(Canvas canvas){
        if (tunerViewPresenter.getLastNote() == null || !tunerViewPresenter.getLastNote() .equals(tunerViewPresenter.getNote())) {
            tunerViewPresenter.setLastNote(tunerViewPresenter.getNote());
            tunerViewPresenter.setAngle(0);
        } else {
            tunerViewPresenter.setAngle(tunerViewPresenter.getAngle() + (tunerViewPresenter.getDist()/10.0));
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R1, paint);
        canvas.drawCircle(CIRCLE_CX, CIRCLE_CY, CIRCLE_R2, paint);
        for (int i = 0; i < 360; i += 10) {
            float x1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN) * Math.sin(Math.PI * (tunerViewPresenter.getAngle() + i) / 180)) + CIRCLE_CX;
            float y1 = (float) ((CIRCLE_R1 + CIRCLE_BAR_MARGIN) * Math.cos(Math.PI * (tunerViewPresenter.getAngle() + i) / 180)) + CIRCLE_CY;
            float x2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN) * Math.sin(Math.PI * (tunerViewPresenter.getAngle() + i) / 180)) + CIRCLE_CX;
            float y2 = (float) ((CIRCLE_R2 - CIRCLE_BAR_MARGIN) * Math.cos(Math.PI * (tunerViewPresenter.getAngle() + i) / 180)) + CIRCLE_CY;
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

}