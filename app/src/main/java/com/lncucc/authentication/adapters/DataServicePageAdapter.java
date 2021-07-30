package com.lncucc.authentication.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DataServicePageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mList;


    public DataServicePageAdapter(ArrayList<Fragment> list, FragmentManager fm) {
        super(fm);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
}
