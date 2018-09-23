package com.example.amgsoft_pc.bestprojetgla.autre;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.amgsoft_pc.bestprojetgla.MyBounceInterpolator;
import com.example.amgsoft_pc.bestprojetgla.R;

import java.util.ArrayList;

public class ButtonEtape {

    private static ArrayList<ButtonEtape> listeEtapes = new ArrayList<>();
    private static int[] listeNumeros = new int[]{
            R.drawable.rond_etape_1,
            R.drawable.rond_etape_2,
            R.drawable.rond_etape_3,
            R.drawable.rond_etape_4,
            R.drawable.rond_etape_5
    };
    private Button bouton;
    private int imageOriginal;
    private String type;

    private Activity activity;


    public ButtonEtape(Button bouton, int image, String type, Activity activity) {
        this.bouton = bouton;
        this.imageOriginal = image;
        this.type = type;
        this.activity = activity;

        this.bouton.setBackgroundResource(image);
        this.bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliqueBouton();
            }
        });

        listeEtapes = new ArrayList<>();
    }

    public static void reset() {
        while (!listeEtapes.isEmpty()) {
            ButtonEtape bouton = listeEtapes.get(0);
            listeEtapes.remove(0);
            bouton.actualiseNum();
        }
    }

    public static ArrayList<ButtonEtape> getListeEtapes() {
        return listeEtapes;
    }

    private void actualiseNum() {
        int idThis = listeEtapes.indexOf(this);

        final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 20);
        myAnim.setInterpolator(interpolator);

        if (idThis == -1) {
            bouton.setBackgroundResource(imageOriginal);
        } else {
            bouton.setBackgroundResource(listeNumeros[idThis]);
        }

        bouton.startAnimation(myAnim);
    }

    private void cliqueBouton() {
        if (listeEtapes.indexOf(this) != -1) {
            // On retourne au logo de base
            bouton.setBackgroundResource(imageOriginal);

            // On retire cette étape de la liste des étapes
            int idThis = listeEtapes.indexOf(this);
            listeEtapes.remove(idThis);
            actualiseNum();

            // On met à jours les numéros
            for (int i = idThis; i < listeEtapes.size(); i++) {
                listeEtapes.get(i).actualiseNum();
            }
        } else {
            if (listeEtapes.size() < listeNumeros.length) {
                listeEtapes.add(this);
                actualiseNum();
            }
        }
    }

    public void pseudoClic() {
        listeEtapes.add(this);
        actualiseNum();
    }

    public int getPosition() {
        return listeEtapes.indexOf(this);
    }

    public String getType() {
        return type;
    }
}
