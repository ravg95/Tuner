package com.ravg95.tuner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rafal on 16/05/2018.
 */

public class SettingsFragment extends Fragment {
    private static final String ADD_PRESET_STRING = "Add preset...";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.settings_fragment, container, false);
        Button saveBtn = (Button) rootView.findViewById(R.id.save);
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancel);
        Button delPresBtn = (Button) rootView.findViewById(R.id.delpreset);
        final TextView toleranceTextView = (TextView) rootView.findViewById(R.id.toleranceText);
        final TextView frequencyTextView = (TextView) rootView.findViewById(R.id.frequencyText);
        final Spinner presetSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        final ArrayList<Preset> presets = SettingsManager.getPresets(getContext());
        final ArrayList<String> presetNames = new ArrayList<>(presets.size()+1);
        Preset currentPreset = SettingsManager.getCurrentPreset(getContext());
        for(int i = 0 ; i < presets.size(); i++){
            presetNames.add(i, presets.get(i).name);
        }
        presetNames.add(presets.size(), ADD_PRESET_STRING);
        final ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, presetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        presetSpinner.setAdapter(adapter);
        frequencyTextView.setText((int) SettingsManager.getBaseFreq(getContext())+"");
        toleranceTextView.setText(SettingsManager.getToleranceInCents(getContext())+"");

        //TODO: set selection for current preset in spinner

        delPresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:: popup warning
                String selectedPreset = presetSpinner.getSelectedItem().toString();
                if(adapter.getPosition(selectedPreset) >=0 ){
                    presets.remove(adapter.getPosition(selectedPreset));
                    presetNames.remove(adapter.getPosition(selectedPreset));
                    adapter.remove(selectedPreset);
                    adapter.notifyDataSetChanged();
                    presetSpinner.setSelection(0);
                    SettingsManager.removePreset(selectedPreset, getContext());
                }
            }
        });

        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals(ADD_PRESET_STRING))
                {
                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.presetFragmenContainer,new PresetCreatorFragment(),"PRESET_FRAGMENT")
                            .addToBackStack("PRESET_FRAGMENT")
                            .commit();
                }
                else{
                    getFragmentManager().popBackStack("PRESET_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SettingsManager.saveSettings(frequencyTextView.getText().toString(),toleranceTextView.getText().toString(),presetSpinner.getSelectedItem().toString(), getContext());
                    getActivity().getSupportFragmentManager().popBackStack("SETTINGS", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (SettingsFormatException e) {
                    e.printStackTrace();
                    //TODO::display wrong format info
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("SETTINGS", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return rootView;
    }
}
