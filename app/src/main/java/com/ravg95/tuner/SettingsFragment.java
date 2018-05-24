package com.ravg95.tuner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by rafal on 16/05/2018.
 */

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.settings_fragment, container, false);
        Button saveBtn = (Button) rootView.findViewById(R.id.save);
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancel);
        final TextView toleranceTextView = (TextView) rootView.findViewById(R.id.toleranceText);
        final TextView frequencyTextView = (TextView) rootView.findViewById(R.id.frequencyText);
        final Spinner presetSpinner = (Spinner) rootView.findViewById(R.id.spinner);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SettingsManager.saveSettings(frequencyTextView.getText().toString(),toleranceTextView.getText().toString(),"", getContext());
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
