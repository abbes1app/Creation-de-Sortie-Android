package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.EtapeArrayAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;

import java.util.ArrayList;

public class FragmentCalculeeListeEtapes extends ListFragment {

    EtapeArrayAdapter adapter;

    String IDSortie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().containsKey("IDSortie")) {
            IDSortie = getArguments().getString("IDSortie");
        }

        View view = inflater.inflate(R.layout.layout_calculee_etapes, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        // Chargement de la liste des étapes
        ArrayList<Etape> listeEtape = ManipulationEtape.liste(db, IDSortie);

        // Chargement de la liste des adresses liées aux étapes
        ArrayList<Adresse> listeAdresse = ManipulationDatabase.listeAdressesEtapes(db, IDSortie);

        // Chargement de la sortie
        Sortie sortie = ManipulationSortie.getAvecID(db, IDSortie);

        // Ajout du lieux de départ aux liste étapes et adresse
        Etape etapeInitial = new Etape();
        etapeInitial.setIdSortie(IDSortie);
        etapeInitial.setDebut(sortie.getDate());
        etapeInitial.setFin(sortie.getDate());
        etapeInitial.setPosition("-1");

        Adresse adresseInitial = new Adresse();
        adresseInitial.setNom("Départ");
        adresseInitial.setAdresse(sortie.getAdresse());
        adresseInitial.setTelephone("");

        listeEtape.add(0, etapeInitial);
        listeAdresse.add(0, adresseInitial);


        int iconTransport = Integer.valueOf(ManipulationTransport.getAvecID(db, sortie.getIdTransport()).getIcon());


        DatabaseManager.getInstance().closeDatabase();


        adapter = new EtapeArrayAdapter(getContext(), listeEtape, listeAdresse, iconTransport);
        setListAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}