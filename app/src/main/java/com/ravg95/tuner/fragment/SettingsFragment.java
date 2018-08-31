package com.ravg95.tuner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ravg95.tuner.R;
import com.ravg95.tuner.tools.SettingsManager;
import com.ravg95.tuner.presenter.SettingsFragmentPresenter;

/**
 * Created by rafal on 16/05/2018.
 */

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    public static final String ADD_PRESET_STRING = "Add preset...";
    public static final String GUITAR_STANDARD = "Guitar Standard";


    Spinner presetSpinner;
    TextView toleranceTextView;
    TextView frequencyTextView;
    SettingsFragmentPresenter settingsFragmentPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.settings_fragment, container, false);
        Button saveBtn = (Button) rootView.findViewById(R.id.save);
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancel);
        ImageButton delPresBtn = (ImageButton) rootView.findViewById(R.id.delpreset);
        toleranceTextView = (TextView) rootView.findViewById(R.id.toleranceText);
        frequencyTextView = (TextView) rootView.findViewById(R.id.frequencyText);
        presetSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        settingsFragmentPresenter = new SettingsFragmentPresenter(this);
        settingsFragmentPresenter.initPresets();
        frequencyTextView.setText((int) SettingsManager.getBaseFreq(getContext())+"");
        toleranceTextView.setText(SettingsManager.getToleranceInCents(getContext())+"");
        settingsFragmentPresenter.selectCurrentPreset();

        delPresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsFragmentPresenter.askToDeletePreset();
            }
        });

        presetSpinner.setOnItemSelectedListener(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              settingsFragmentPresenter.save();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsFragmentPresenter.cancel();
            }
        });
        return rootView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        if(selectedItem.equals(ADD_PRESET_STRING))
        {
            settingsFragmentPresenter.openPresetCreator();
        }
        else{
            settingsFragmentPresenter.presetSelected();
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void close() {
        getActivity().getSupportFragmentManager().popBackStack("SETTINGS", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public String getFreqText() {
        return frequencyTextView.getText().toString();
    }

    public String getToleranceText() {
        return toleranceTextView.getText().toString();
    }

    public String getSpinnerText() {
        return presetSpinner.getSelectedItem().toString();
    }

    public void displayWarning(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void closePresetCreator() {
        getFragmentManager().popBackStack("PRESET_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setSpinnerSelection(int position) {
        presetSpinner.setSelection(position);
    }

    public void setSpinnerAdapter(ArrayAdapter<String> adapter) {
        presetSpinner.setAdapter(adapter);
    }

    public void showCreatorFragment(PresetCreatorFragment newFragment) {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.presetFragmenContainer,newFragment,"PRESET_FRAGMENT")
                .addToBackStack("PRESET_FRAGMENT")
                .commit();
    }

    public void showDeleteWarningDialog(String currPresetName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this preset: \""+currPresetName+"\"?")
                .setTitle("Hold on");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                settingsFragmentPresenter.deletePreset();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.show();

    }

    public int getSpinnerSize() {
        return presetSpinner.getAdapter().getCount();
    }
}
