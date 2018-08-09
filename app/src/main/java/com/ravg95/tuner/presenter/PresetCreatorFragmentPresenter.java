package com.ravg95.tuner.presenter;

import android.view.View;
import android.widget.EditText;

import com.ravg95.tuner.data.SettingsManager;
import com.ravg95.tuner.exception.DuplicatePresetNameException;
import com.ravg95.tuner.fragment.PresetCreatorFragment;

import java.util.ArrayList;

public class PresetCreatorFragmentPresenter {

    ArrayList<View> stringViews;
    PresetCreatorFragment presetCreatorFragment;
    public PresetCreatorFragmentPresenter(PresetCreatorFragment presetCreatorFragment){
        stringViews = new ArrayList<>();

    }


    public void add() {
        View newView = presetCreatorFragment.onAddField();
        stringViews.add(newView);
    }

    public void remove() {
        View view = stringViews.get(stringViews.size()-1);
        presetCreatorFragment.onDeleteField(view);
        stringViews.remove(view);
    }

    public void exit() {
        presetCreatorFragment.close();

    }

    public void save() {
        int numOfStrings = stringViews.size();
        String name = presetCreatorFragment.getNameText();
        String[] strings = new String[numOfStrings];
        for(int i = 0; i < numOfStrings; i++){
            strings[i] = ((EditText)stringViews.get(i)).getText().toString();
            if(!strings[i].matches("[A-Ga-g][b#0-9][0-9]?")){
                presetCreatorFragment.displayWarning("Incorrect string name");
                return;
            }
            if(strings[i].charAt(0)>='a'){
                StringBuilder myString = new StringBuilder(strings[i]);
                myString.setCharAt(0, (char) (strings[i].charAt(0) -'a'+'A'));
                strings[i] = myString.toString();
            }
        }
        try {
            SettingsManager.addPreset(name, numOfStrings, strings, presetCreatorFragment.getContext());
            presetCreatorFragment.updateSpinner(name);
            presetCreatorFragment.close();
        } catch (DuplicatePresetNameException e) {
            presetCreatorFragment.displayWarning("Preset name already in use");
        }

    }
}
