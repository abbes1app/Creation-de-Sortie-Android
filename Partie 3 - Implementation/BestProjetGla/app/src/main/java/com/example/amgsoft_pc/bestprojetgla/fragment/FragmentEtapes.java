package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.autre.ButtonEtape;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import java.util.ArrayList;

public class FragmentEtapes extends android.support.v4.app.Fragment {
    ButtonEtape[] listeBoutonsEtapes;

    Sortie sortieEnCours;

    public FragmentEtapes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Choisir les étapes");
        View rootView = inflater.inflate(R.layout.layout_etapes, container, false);


        // Initialisation des boutons
        int[] idBoutons = new int[]{R.id.bouton_type_1, R.id.bouton_type_2, R.id.bouton_type_3,
                R.id.bouton_type_4, R.id.bouton_type_5, R.id.bouton_type_6};
        int[] idTextes = new int[]{R.id.texte_type_1, R.id.texte_type_2, R.id.texte_type_3,
                R.id.texte_type_4, R.id.texte_type_5, R.id.texte_type_6};

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ArrayList<TypeEtape> listeType = ManipulationTypeEtape.liste(db);
        Log.d("taille ", listeType.get(0).getNom() + "");

        listeBoutonsEtapes = new ButtonEtape[6];

        for (int i = 0; i < Math.min(listeType.size(), 6); i++) {
            TypeEtape type = listeType.get(i);
            listeBoutonsEtapes[i] = new ButtonEtape((Button) rootView.findViewById(idBoutons[i]),
                    Integer.valueOf(type.getImage()), type.getIdType(), getActivity());

            ((TextView) rootView.findViewById(idTextes[i])).setText(type.getNom());
        }

        // Réception ID sortie
        if (getArguments().containsKey("IDSortie")) {
            // Récupération de l'ID et de la sortie
            String IDSortie = getArguments().getString("IDSortie");
            sortieEnCours = ManipulationSortie.getAvecID(db, IDSortie);

            for (Etape etape : ManipulationEtape.liste(db, IDSortie)) {
                listeBoutonsEtapes[Integer.valueOf(etape.getIdType())].pseudoClic();
            }
        }
        DatabaseManager.getInstance().closeDatabase();


        Button reset = (Button) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonEtape.reset();
            }
        });

        Button valide = (Button) rootView.findViewById(R.id.valider);
        valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                ManipulationDatabase.supprimerEtapesSortie(db, sortieEnCours.getIdSortie());

                for (ButtonEtape bouton : listeBoutonsEtapes) {
                    if (bouton.getPosition() != -1) {
                        Etape etape = new Etape();

                        etape.setIdSortie(sortieEnCours.getIdSortie());
                        etape.setIdType(bouton.getType());
                        etape.setPosition(Integer.toString(bouton.getPosition()));

                        ManipulationEtape.inserer(db, etape);
                    }
                }

                DatabaseManager.getInstance().closeDatabase();

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}