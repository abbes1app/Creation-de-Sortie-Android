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

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.SortieSwipeAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;

import java.util.ArrayList;

public class FragmentListeSorties extends Fragment implements SortieSwipeAdapter.OnItemListener {

    public static final String TAG = "FragmentListeSorties";
    ArrayList<Sortie> listeSortie;
    Sortie sortieModifie;
    RecyclerView recyclerView;
    private SortieSwipeAdapter mAdapter;

    public static Fragment newInstance() {
        return new FragmentListeSorties();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Liste des Sorties");
        View view = inflater.inflate(R.layout.layout_liste_sorties, container, false);
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
        listeSortie = ManipulationSortie.liste(dbTemp);
        mAdapter = new SortieSwipeAdapter(listeSortie);
        DatabaseManager.getInstance().closeDatabase();

        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemSupprimer(Sortie sortie) {
        int index = listeSortie.indexOf(sortie);
        listeSortie.remove(index);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ManipulationDatabase.supprimerSortieComplette(db, sortie.getIdSortie());
        DatabaseManager.getInstance().closeDatabase();

        mAdapter.notifyItemRemoved(index);
        mAdapter.notifyItemRangeChanged(index, mAdapter.getItemCount());
    }

    @Override
    public void onItemModifier(Sortie sortie) {

        Fragment fragment = new FragmentDetailSortie();

        // Envoie de l'ID de sortie
        Bundle bundle = new Bundle();
        bundle.putString("IDSortie", sortie.getIdSortie());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        if (sortieModifie != null) {
            listeSortie.clear();

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            listeSortie.addAll(ManipulationSortie.liste(db));
            DatabaseManager.getInstance().closeDatabase();

            mAdapter.notifyDataSetChanged();

            for (int i = 0; i < listeSortie.size(); i++) {
                if (listeSortie.get(i).getIdSortie().compareTo(sortieModifie.getIdSortie()) == 0) {
                    recyclerView.smoothScrollToPosition(i);
                }
            }

            sortieModifie = null;
        }

        super.onResume();
    }
}
