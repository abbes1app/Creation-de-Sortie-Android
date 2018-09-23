package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentViewPagerParticipants extends FragmentStatePagerAdapter {
    FragmentAjoutListeAmis ajoutListeAmis;
    FragmentCreerParticipant creerAmi;
    private String[] titles = new String[]{"Choisir des amis", "Créer un ami"};

    public FragmentViewPagerParticipants(FragmentManager fm, String IDSortie) {
        super(fm);

        ajoutListeAmis = new FragmentAjoutListeAmis();

        Bundle bundle = new Bundle();
        bundle.putString("IDSortie", IDSortie);
        ajoutListeAmis.setArguments(bundle);

        creerAmi = new FragmentCreerParticipant();
        creerAmi.previent = new FragmentCreerParticipant.PrevientUpdate() {
            @Override
            public void onUpdate() {
                ajoutListeAmis.update(true);
            }
        };
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return titles.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ajoutListeAmis;
            case 1:
                return creerAmi;
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



