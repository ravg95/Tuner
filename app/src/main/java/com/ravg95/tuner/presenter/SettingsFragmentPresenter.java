package com.ravg95.tuner.presenter;


import android.widget.ArrayAdapter;

import com.ravg95.tuner.R;
import com.ravg95.tuner.data.SettingsManager;
import com.ravg95.tuner.exception.SettingsFormatException;
import com.ravg95.tuner.fragment.PresetCreatorFragment;
import com.ravg95.tuner.fragment.SettingsFragment;
import com.ravg95.tuner.util.Preset;
import com.ravg95.tuner.util.SpinnerAdapterUpdater;

import java.util.ArrayList;

public class SettingsFragmentPresenter {

    SettingsFragment settingsFragment;
    ArrayAdapter<String> adapter;
    ArrayList<Preset> presets;
    ArrayList<String> presetNames;
    public SettingsFragmentPresenter(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }


    public void cancel() {
        settingsFragment.close();
    }

    public void save() {
        try {
            SettingsManager.saveSettings(settingsFragment.getFreqText(),settingsFragment.getToleranceText(),settingsFragment.getSpinnerText(), settingsFragment.getContext());
            settingsFragment.close();
        } catch (SettingsFormatException e) {
            settingsFragment.displayWarning("Settings: wrong format");
        }
    }

    public void openPresetCreator() {
        PresetCreatorFragment newFragment = new PresetCreatorFragment();
        newFragment.setSpinnerAdapterUpdater(new SpinnerAdapterUpdater() {
            @Override
            public void updateSpinnerItem(String item) {
                presetNames.add(presetNames.size()-1,item);
                adapter.notifyDataSetChanged();
                settingsFragment.setSpinnerSelection(adapter.getPosition(item));
            }
        });
        settingsFragment.showCreatorFragment(newFragment);
    }

    public void presetSelected() {
        settingsFragment.closePresetCreator();
    }

    public void askToDeletePreset() {
        String selectedPreset = settingsFragment.getSpinnerText();
        settingsFragment.showDeleteWarningDialog(selectedPreset);
    }

    public void deletePreset(){
        String selectedPreset = settingsFragment.getSpinnerText();
        if(adapter.getPosition(selectedPreset) >=0 ){
            presetNames.remove(adapter.getPosition(selectedPreset));
            adapter.remove(selectedPreset);
            adapter.notifyDataSetChanged();
            settingsFragment.setSpinnerSelection(0);
            SettingsManager.removePreset(selectedPreset, settingsFragment.getContext());
        }

    }

    public String getCurrentPresetName() {
        return SettingsManager.getCurrentPreset(settingsFragment.getContext()).getName();
    }

    public void initPresets() {
        ArrayList<Preset> presets = SettingsManager.getPresets(settingsFragment.getContext());
        ArrayList<String> presetNames = new ArrayList<>(presets.size()+1);

        for(int i = 0 ; i < presets.size(); i++){
            presetNames.add(i, presets.get(i).getName());
        }
        presetNames.add(presets.size(), SettingsFragment.ADD_PRESET_STRING);
        adapter = new ArrayAdapter(settingsFragment.getContext(),
                android.R.layout.simple_spinner_item, presetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        settingsFragment.setSpinnerAdapter(adapter);
    }

    public void selectCurrentPreset() {
        settingsFragment.setSpinnerSelection(adapter.getPosition(getCurrentPresetName()));
    }
}
