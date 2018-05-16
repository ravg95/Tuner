package com.ravg95.tuner;

import java.util.HashMap;

import lombok.AllArgsConstructor;

/**
 * Created by rafal on 15/02/2018.
 */
public class Preset {
    int numOfStrings;
    String name;
    String[] strings;

    public Preset(int numOfStrings, String name, String[] strings){
        this.numOfStrings = numOfStrings;
        this.name = name;
        this.strings = strings;
    }
}
