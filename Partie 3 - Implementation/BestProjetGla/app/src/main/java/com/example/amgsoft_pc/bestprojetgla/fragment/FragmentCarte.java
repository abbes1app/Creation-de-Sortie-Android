package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amgsoft_pc.bestprojetgla.R;

public class FragmentCarte extends android.support.v4.app.Fragment {
    public FragmentCarte() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Carte");
        View rootView = inflater.inflate(R.layout.layout_carte, container, false);
        return rootView;
    }

}