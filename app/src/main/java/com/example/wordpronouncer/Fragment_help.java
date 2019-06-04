package com.example.wordpronouncer;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class Fragment_help extends DialogFragment {


    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_white_bg));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_help, container, false);

        TextView help_heading_1= v.findViewById(R.id.help_heading_1);
        TextView help_line_count_1 = v.findViewById(R.id.help_line_count_1);
        TextView help_line_1 = v.findViewById(R.id.help_line_1);
        TextView help_line_count_2 = v.findViewById(R.id.help_line_count_2);
        TextView help_line_2 = v.findViewById(R.id.help_line_2);
        TextView help_line_count_3 = v.findViewById(R.id.help_line_count_3);
        TextView help_line_3 = v.findViewById(R.id.help_line_3);
        TextView help_heading_2 = v.findViewById(R.id.help_heading_2);
        TextView help_line_count_5 = v.findViewById(R.id.help_line_count_5);
        TextView help_line_5 = v.findViewById(R.id.help_line_5);
        TextView help_line_count_6 = v.findViewById(R.id.help_line_count_6);
        TextView help_line_6 = v.findViewById(R.id.help_line_6);
        TextView help_line_count_7 = v.findViewById(R.id.help_line_count_7);
        TextView help_line_7 = v.findViewById(R.id.help_line_7);

        Typeface ubuntu_bold = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(),"fonts/Ubuntu-Bold.ttf");
        Typeface ubuntu_regular = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(),"fonts/Ubuntu-Regular.ttf");

        help_heading_1.setTypeface(ubuntu_bold);
        help_heading_2.setTypeface(ubuntu_bold);

        help_line_count_1.setTypeface(ubuntu_regular);
        help_line_1.setTypeface(ubuntu_regular);
        help_line_count_2.setTypeface(ubuntu_regular);
        help_line_2.setTypeface(ubuntu_regular);
        help_line_count_3.setTypeface(ubuntu_regular);
        help_line_3.setTypeface(ubuntu_regular);
        help_line_count_5.setTypeface(ubuntu_regular);
        help_line_5.setTypeface(ubuntu_regular);
        help_line_count_6.setTypeface(ubuntu_regular);
        help_line_6.setTypeface(ubuntu_regular);
        help_line_count_7.setTypeface(ubuntu_regular);
        help_line_7.setTypeface(ubuntu_regular);


        return v;
    }

}
