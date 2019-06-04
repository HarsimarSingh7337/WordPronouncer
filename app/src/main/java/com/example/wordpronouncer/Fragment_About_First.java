package com.example.wordpronouncer;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class Fragment_About_First extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about__first, container, false);

        TextView aboutHeading = v.findViewById(R.id.about_heading);
        TextView aboutCount1 = v.findViewById(R.id.about_count_line1);
        TextView aboutLine1 = v.findViewById(R.id.about_line1);
        TextView aboutCount2 = v.findViewById(R.id.about_count_line2);
        TextView aboutLine2 = v.findViewById(R.id.about_line2);
        TextView aboutCount3 = v.findViewById(R.id.about_count_line3);
        TextView aboutLine3 = v.findViewById(R.id.about_line3);
        TextView aboutCount4 = v.findViewById(R.id.about_count_line4);
        TextView aboutLine4 = v.findViewById(R.id.about_line4);

        Typeface ubuntuBold = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(),"fonts/Ubuntu-Bold.ttf");
        Typeface ubuntuRegular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Ubuntu-Regular.ttf");

        aboutHeading.setTypeface(ubuntuBold);

        aboutCount1.setTypeface(ubuntuRegular);
        aboutCount2.setTypeface(ubuntuRegular);
        aboutCount3.setTypeface(ubuntuRegular);
        aboutCount4.setTypeface(ubuntuRegular);
        aboutLine1.setTypeface(ubuntuRegular);
        aboutLine2.setTypeface(ubuntuRegular);
        aboutLine3.setTypeface(ubuntuRegular);
        aboutLine4.setTypeface(ubuntuRegular);

        return v;
    }
}
