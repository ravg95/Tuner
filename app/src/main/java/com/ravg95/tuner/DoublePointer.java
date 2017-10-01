package com.ravg95.tuner;



import lombok.Getter;
import lombok.Setter;


class DoublePointer {
    @Getter
    @Setter
    private double value = 0;

    public DoublePointer(double val) {
        value = val;
    }
}
