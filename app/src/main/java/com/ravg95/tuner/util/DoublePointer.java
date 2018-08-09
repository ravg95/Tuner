package com.ravg95.tuner.util;


import lombok.Getter;
import lombok.Setter;
public class DoublePointer {
    @Getter
    private double value = 0;
    @Setter
    private OnValueChangedListener onValueChangedListener;

    public DoublePointer(double val, OnValueChangedListener listener) {
        value = val;
        onValueChangedListener = listener;
    }

    public void setValue(double val){
        value = val;
        if(onValueChangedListener != null)
            onValueChangedListener.valueChanged(value);
    }

   public interface OnValueChangedListener {
        void valueChanged(double newValue);
    }
}
