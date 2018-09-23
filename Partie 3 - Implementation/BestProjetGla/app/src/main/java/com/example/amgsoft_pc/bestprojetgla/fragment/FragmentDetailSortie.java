package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;


public class FragmentDetailSortie extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("");
        View rootView = inflater.inflate(R.layout.layout_choix_participants, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        String IDSortie = "";

        if (getArguments().containsKey("IDSortie")) {
            IDSortie = getArguments().getString("IDSortie");

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            getActivity().setTitle(ManipulationSortie.getAvecID(db, IDSortie).getNom());
            DatabaseManager.getInstance().closeDatabase();
        }

        FragmentViewPagerSortie myAdapter = new FragmentViewPagerSortie(getFragmentManager(), IDSortie);

        viewPager.setAdapter(myAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

        return rootView;
    }
}



