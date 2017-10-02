package com.ravg95.tuner;


import lombok.Getter;
import lombok.Setter;


class DoublePointer {
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
        onValueChangedListener.valueChanged(value);
    }

   public interface OnValueChangedListener {
        void valueChanged(double newValue);
    }
}
