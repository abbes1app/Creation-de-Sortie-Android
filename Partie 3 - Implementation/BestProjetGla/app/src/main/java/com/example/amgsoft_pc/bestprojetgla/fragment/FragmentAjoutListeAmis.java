package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.AmiChoixArrayAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienParticipants;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAmi;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationLienParticipants;

import java.util.ArrayList;

public class FragmentAjoutListeAmis extends ListFragment {

    ArrayList<Ami> listeAmis;
    ArrayList<Boolean> participe;
    AmiChoixArrayAdapter adapter;

    String IDSortie;

    public void update(boolean ajout) {
        listeAmis.clear();

        SQLiteDatabase dbTemp = DatabaseManager.getInstance().openDatabase();
        listeAmis.addAll(ManipulationAmi.liste(dbTemp));
        DatabaseManager.getInstance().closeDatabase();

        while (listeAmis.size() > participe.size()) {
            participe.add(true);
        }

        adapter.notifyDataSetChanged();

        if (ajout) {
            getListView().smoothScrollToPosition(listeAmis.size() - 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().containsKey("IDSortie")) {
            IDSortie = getArguments().getString("IDSortie");
        }

        View view = inflater.inflate(R.layout.layout_liste_selection_ami, container, false);

        Button bouton = (Button) view.findViewById(R.id.bouton_ami_valider);
        bouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                ManipulationDatabase.supprimerLienParticipants(db, IDSortie);

                for (int i = 0; i < listeAmis.size(); i++) {

                    if (participe.get(i)) {
                        LienParticipants lien = new LienParticipants();
                        lien.setIdAmi(listeAmis.get(i).getIdAmi());
                        lien.setIdSortie(IDSortie);
                        ManipulationLienParticipants.inserer(db, lien);
                    }
                }
                DatabaseManager.getInstance().closeDatabase();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        participe = new ArrayList<Boolean>(0);
        ArrayList<Ami> participants = new ArrayList<Ami>(0);

        listeAmis = new ArrayList<Ami>(0);
        adapter = new AmiChoixArrayAdapter(getContext(), listeAmis, participe);
        setListAdapter(adapter);


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        listeAmis.addAll(ManipulationAmi.liste(db));
        participants.addAll(ManipulationDatabase.listeParticipantsSortie(db, IDSortie));
        DatabaseManager.getInstance().closeDatabase();

        for (int i = 0; i < listeAmis.size(); i++) {
            Ami ami = listeAmis.get(i);
            participe.add(false);
            for (Ami temp : participants) {
                if (temp.getIdAmi().compareTo(ami.getIdAmi()) == 0) {
                    participe.set(i, true);
                    break;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        participe.set(position, !participe.get(position));
        adapter.notifyDataSetChanged();
    }
}