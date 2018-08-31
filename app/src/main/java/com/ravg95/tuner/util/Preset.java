package com.ravg95.tuner.util;


import lombok.Getter;

/**
 * Created by rafal on 15/02/2018.
 */
public class Preset {
    @Getter
    int numOfStrings;
    @Getter
    String name;
    @Getter
    String[] strings;

    public Preset(int numOfStrings, String name, String[] strings){
        this.numOfStrings = numOfStrings;
        this.name = name;
        this.strings = strings;
    }
}
