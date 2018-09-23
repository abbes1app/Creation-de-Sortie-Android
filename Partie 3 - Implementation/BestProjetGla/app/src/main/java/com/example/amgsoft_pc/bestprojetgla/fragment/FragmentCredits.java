package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.amgsoft_pc.bestprojetgla.R;

public class FragmentCredits extends Fragment {
    String texte = "Application pensée et codée par :\n\n" +
            "Abbes CHOUKCHOU BRAHAM\n" +
            "Thierno Ismaila NDIAYE\n" +
            "Cédric VACHAUDEZ\n" +
            "\n\n\n\n\n\n\n\n" +
            "Icones téléchargées sur\nicons8.com";

    public FragmentCredits() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Crédits");
        View rootView = inflater.inflate(R.layout.layout_credits, container, false);

        final TextSwitcher mSwitcher = (TextSwitcher) rootView.findViewById(R.id.texte_switcher);
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(getActivity());
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(18);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        // Lancement asynchrone
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwitcher.setText(texte);
            }
        }, 400);


        return rootView;
    }

}