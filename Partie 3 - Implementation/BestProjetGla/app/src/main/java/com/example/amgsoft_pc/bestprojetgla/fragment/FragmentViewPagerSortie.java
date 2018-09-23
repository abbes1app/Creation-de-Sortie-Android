package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentViewPagerSortie extends FragmentStatePagerAdapter {
    String IDSortie;
    private String[] titles = new String[]{"Étapes", "Participants"};

    public FragmentViewPagerSortie(FragmentManager fm, String IDSortie) {
        super(fm);

        this.IDSortie = IDSortie;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return titles.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Fragment temp;
        Bundle bundle;

        switch (position) {
            case 0:
                temp = new FragmentCalculeeListeEtapes();

                bundle = new Bundle();
                bundle.putString("IDSortie", IDSortie);
                temp.setArguments(bundle);

                return temp;
            case 1:
                temp = new FragmentCalculeeListeAmis();

                bundle = new Bundle();
                bundle.putString("IDSortie", IDSortie);
                temp.setArguments(bundle);

                return temp;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}



