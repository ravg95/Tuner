package com.ravg95.tuner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by rafal on 16/05/2018.
 */

public class PresetCreatorFragment extends Fragment{

    LinearLayout stringsLayout;
    ArrayList<View> stringViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.preset_fragment_layout, container, false);
        Button rmBtn = (Button) rootView.findViewById(R.id.remove_string_button);
        Button addBtn = (Button) rootView.findViewById(R.id.add_string_button);
        Button saveBtn = (Button) rootView.findViewById(R.id.save_preset_button);
        Button exitBtn = (Button) rootView.findViewById(R.id.cancel_preset_button);
        stringsLayout = (LinearLayout) rootView.findViewById(R.id.strings_container);
        final EditText nameField = (EditText) rootView.findViewById(R.id.preset_name_field);
        stringViews = new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View newView = onAddField();
                stringViews.add(newView);
            }
        });
        rmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = stringViews.get(stringViews.size()-1);
                onDelete(view);
                stringViews.remove(view);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfStrings = stringViews.size();
                String name = nameField.getText().toString();
                String[] strings = new String[numOfStrings];
                for(int i = 0; i < numOfStrings; i++){
                    strings[i] = ((EditText)stringViews.get(i)).getText().toString();
                    if(!strings[i].matches("[A-Ga-g][b#0-9][0-9]?")){
                        //TODO:: display warnings
                        return;
                    }
                    if(strings[i].charAt(0)>='a'){
                        StringBuilder myString = new StringBuilder(strings[i]);
                        myString.setCharAt(0, (char) (strings[i].charAt(0) -'a'+'A'));
                        strings[i] = myString.toString();
                    }
                }
                try {
                    SettingsManager.addPreset(name, numOfStrings, strings, getContext());
                    getFragmentManager().popBackStack("PRESET_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (DuplicatePresetNameException e) {
                    //TODO:: display warnings
                }

            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack("PRESET_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        return rootView;
    }


    public View onAddField() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.field, null);
        stringsLayout.addView(v, stringsLayout.getChildCount() - 1);
        return v;
    }

    public void onDelete(View v) {
        stringsLayout.removeView(v);
    }
}
