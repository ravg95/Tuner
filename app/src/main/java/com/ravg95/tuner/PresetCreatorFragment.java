package com.ravg95.tuner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

/**
 * Created by rafal on 16/05/2018.
 */

public class PresetCreatorFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.preset_fragment_layout, container, false);
        Button addBtn = (Button) rootView.findViewById(R.id.add_string_button);
        Button saveBtn = (Button) rootView.findViewById(R.id.save_preset_button);
        Button exitBtn = (Button) rootView.findViewById(R.id.cancel_preset_button);
        LinearLayout stringsLayout = (LinearLayout) rootView.findViewById(R.id.strings_container);

        return rootView;
    }
}
