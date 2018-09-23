package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.AmiSwipeAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAmi;

import java.util.ArrayList;

public class FragmentListeAmis extends Fragment implements AmiSwipeAdapter.OnItemListener {

    public static final String TAG = "FragmentListeSorties";
    ArrayList<Ami> listeAmis;
    Ami amiModifie;
    RecyclerView recyclerView;
    private AmiSwipeAdapter mAdapter;

    public static Fragment newInstance() {
        return new FragmentListeAmis();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Liste des Amis");
        View view = inflater.inflate(R.layout.layout_liste_amis, container, false);

        ImageButton bouton = (ImageButton) view.findViewById(R.id.bouton_ajoute_ami);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentCreerModifAmi();

                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initRecyclerView(getView());
            }
        });
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.swipe_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        SQLiteDatabase dbTemp = DatabaseManager.getInstance().openDatabase();

        listeAmis = ManipulationAmi.liste(dbTemp);
        mAdapter = new AmiSwipeAdapter(listeAmis);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void onItemSupprimer(Ami ami) {
        int index = listeAmis.indexOf(ami);
        listeAmis.remove(index);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ManipulationDatabase.supprimerAmiPropre(db, ami.getIdAmi());
        DatabaseManager.getInstance().closeDatabase();

        mAdapter.notifyItemRemoved(index);
        mAdapter.notifyItemRangeChanged(index, mAdapter.getItemCount());
    }

    @Override
    public void onItemModifier(Ami ami) {
        Fragment fragment = new FragmentCreerModifAmi();

        // Envoie de l'ID de sortie
        Bundle bundle = new Bundle();
        bundle.putString("IDAmi", ami.getIdAmi());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        amiModifie = ami;
    }

    @Override
    public void onResume() {
        if (amiModifie != null) {
            listeAmis.clear();

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            listeAmis.addAll(ManipulationAmi.liste(db));
            DatabaseManager.getInstance().closeDatabase();

            mAdapter.notifyDataSetChanged();

            for (int i = 0; i < listeAmis.size(); i++) {
                if (listeAmis.get(i).getIdAmi().compareTo(amiModifie.getIdAmi()) == 0) {
                    recyclerView.smoothScrollToPosition(i);
                }
            }

            amiModifie = null;
        }

        super.onResume();
    }
}