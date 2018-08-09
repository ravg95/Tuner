package com.ravg95.tuner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ravg95.tuner.R;
import com.ravg95.tuner.presenter.PresetCreatorFragmentPresenter;
import com.ravg95.tuner.util.SpinnerAdapterUpdater;

import lombok.Setter;

/**
 * Created by rafal on 16/05/2018.
 */

public class PresetCreatorFragment extends Fragment{

    LinearLayout stringsLayout;
    @Setter
    SpinnerAdapterUpdater spinnerAdapterUpdater;
    private EditText nameField;
    PresetCreatorFragmentPresenter presetCreatorFragmentPresenter = new PresetCreatorFragmentPresenter(this);
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
        nameField = (EditText) rootView.findViewById(R.id.preset_name_field);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presetCreatorFragmentPresenter.add();
            }
        });
        rmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetCreatorFragmentPresenter.remove();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presetCreatorFragmentPresenter.save();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetCreatorFragmentPresenter.exit();
            }
        });


        return rootView;
    }

    public View onAddField() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.field, null);
        stringsLayout.addView(v, stringsLayout.getChildCount());
        return v;
    }

    public void onDeleteField(View v) {
        stringsLayout.removeView(v);
    }


    public void displayWarning(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void close() {
        getFragmentManager().popBackStack("PRESET_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void updateSpinner(String name) {
        spinnerAdapterUpdater.updateSpinnerItem(name);
    }

    public String getNameText() {
        return nameField.getText().toString();
    }
}
