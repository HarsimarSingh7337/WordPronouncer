package com.example.wordpronouncer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Objects;

public class Fragment_About_Container extends DialogFragment {

    private ViewPager viewPager;
    private CheckBox ch1,ch2,ch3,ch4;

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_white_bg));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about__container, container, false);

        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setClipToPadding(false);
        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        slidePagerAdapter.addFrag(new Fragment_About_First(),"First Fragment");

        slidePagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(slidePagerAdapter);

        return v;
    }

    private class SlidePagerAdapter extends FragmentStatePagerAdapter{

        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private ArrayList<String> mFragmentTitleList = new ArrayList<>();

        SlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

       @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title){
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
