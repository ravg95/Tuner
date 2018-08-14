package com.ravg95.tuner.tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ravg95.tuner.exception.DuplicatePresetNameException;
import com.ravg95.tuner.exception.SettingsFormatException;
import com.ravg95.tuner.fragment.SettingsFragment;
import com.ravg95.tuner.util.Preset;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by rafal on 17/11/2017.
 */

public class SettingsManager {

    private static final String PRESET_LIST_STRING = "PresetList";
    private static final String BASE_FREQ_STRING = "BaseFreq";
    private static final String TOLERANCE_STRING = "Tolerance";
    private static final String CURR_PRESET_STRING = "CurrPreset";

    public static double getBaseFreq(Context context){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());

        String readFreq = appSharedPrefs.getString(BASE_FREQ_STRING, "440");

        return Double.parseDouble(readFreq);
    }


    public static int getToleranceInCents(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());

        String readTol = appSharedPrefs.getString(TOLERANCE_STRING, "5");

        return Integer.parseInt(readTol);
    }


    public static Preset getCurrentPreset(Context context){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());

        String presetName = appSharedPrefs.getString(CURR_PRESET_STRING, "");
        Preset currPreset = getPresetByName(presetName, context);
        if(currPreset == null) { //default option
            currPreset = new Preset(6, SettingsFragment.GUITAR_STANDARD, new String[]{"E2","A2", "D3", "G3", "B3", "E4"});
        }
        return currPreset;
    }

    public static void saveSettings(String frequency, String tolerance, String presetName, Context context) throws SettingsFormatException {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        try{
            double frq = Double.parseDouble(frequency);
            int tl = Integer.parseInt(tolerance);
            if(tl<0 || tl > 50 || frq < 200 || frq > 600)
                throw new SettingsFormatException();
        } catch (NumberFormatException e){
            throw new SettingsFormatException();
        }
        prefsEditor.putString(BASE_FREQ_STRING, frequency);
        prefsEditor.putString(TOLERANCE_STRING, tolerance);
        prefsEditor.putString(CURR_PRESET_STRING, presetName);
        prefsEditor.commit();


    }
    public static void addPreset(String name, int num_of_strings, String[] strings, Context context) throws DuplicatePresetNameException {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<Preset> presets = getPresets(context);
        for(Preset preset : presets){
            if(preset.getName().equals(name))
                throw new DuplicatePresetNameException();
        }
        presets.add(new Preset(num_of_strings, name, strings));
        Gson gson = new Gson();
        String json = gson.toJson(presets);
        prefsEditor.putString(PRESET_LIST_STRING, json);
        prefsEditor.commit();

    }
    public static void removePreset(String name, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        ArrayList<Preset> presets = getPresets(context);
        Preset presetToBeRemoved = null;
        for(Preset preset : presets){
            if(preset.getName().equals(name)) {
                presetToBeRemoved = preset;
                break;
            }
        }
        if(presetToBeRemoved!= null)
          presets.remove(presetToBeRemoved);
        Gson gson = new Gson();
        String json = gson.toJson(presets);
        prefsEditor.putString(PRESET_LIST_STRING, json);
        prefsEditor.commit();
    }
    public static ArrayList<Preset> getPresets(Context context){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(PRESET_LIST_STRING, "");
        Type type = new TypeToken<ArrayList<Preset>>(){}.getType();
        ArrayList<Preset> presets = gson.fromJson(json, type);
        if(presets == null ) return new ArrayList<>();
        return presets;
    }
    public static Preset getPresetByName(String name, Context context){
        ArrayList<Preset> presets = getPresets(context);
        Preset ret = null;
        if(presets == null)
            return null;
        for(Preset preset : presets)
            if(preset.getName().equals(name))
                ret =  preset;
        return ret;
    }

}
