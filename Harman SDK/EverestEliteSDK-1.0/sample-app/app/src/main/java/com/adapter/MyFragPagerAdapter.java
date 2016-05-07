package com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Map;

public class MyFragPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private ArrayList<Map<String, Object>> param;
    public MyFragPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<Map<String, Object>> param) {
        super(fm);
        this.fragments = fragments;
        this.param = param;
    }


    public MyFragPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) param.get(position).get("title");
    }

    public int getImageId(int position){
        return (Integer) param.get(position).get("image");
    }

}