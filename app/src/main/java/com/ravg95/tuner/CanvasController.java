package com.ravg95.tuner;

/**
 * Created by rafal on 17/05/2018.
 */

public interface CanvasController {

   void refreshCanvas();
    void setPitchProperties(String note, String format, double value);

   void invalidateCanvas();
}