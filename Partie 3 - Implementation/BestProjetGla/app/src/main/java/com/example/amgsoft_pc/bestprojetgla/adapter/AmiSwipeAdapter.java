package com.example.amgsoft_pc.bestprojetgla.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;
import com.example.amgsoft_pc.bestprojetgla.swipe.SupprModifCoordinatorLayout;

import java.util.ArrayList;

public class AmiSwipeAdapter extends RecyclerView.Adapter<AmiSwipeAdapter.ViewHolder> {

    private ArrayList<Ami> listeAmis;
    private ArrayList<Adresse> listeAdresses;
    private OnItemListener onItemListener;
    public AmiSwipeAdapter(ArrayList<Ami> listeAmis) {
        this.listeAmis = listeAmis;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        listeAdresses = new ArrayList<>(0);
        for (Ami ami : listeAmis) {
            Adresse adresse = ManipulationAdresse.getAvecID(db, ami.getIdAdresse());

            if (adresse == null) {
                adresse = new Adresse();
                adresse.setNom("");
            }

            listeAdresses.add(adresse);

            if (ami.getPrenom() == null) {
                ami.setPrenom("");
            }

            if (ami.getNom() == null) {
                ami.setNom("");
            }

            if (ami.getTelephone() == null) {
                ami.setTelephone("");
            }
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ami_glisse, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // fill data
        Ami sortie = listeAmis.get(position);

        holder.texteNom.setText(sortie.getNom());
        holder.textePrenom.setText(sortie.getPrenom());
        holder.texteAdresse.setText(listeAdresses.get(position).getAdresse());
        holder.texteTel.setText(sortie.getTelephone());

        holder.supprimer.setOnClickListener(new OnSupprimeListener(sortie));
        holder.modifier.setOnClickListener(new OnModifieListener(sortie));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.coordinatorLayout.sync();
    }

    @Override
    public int getItemCount() {
        return listeAmis.size();
    }

    public void setOnItemClickListener(OnItemListener listener) {
        onItemListener = listener;
    }

    public interface OnItemListener {
        void onItemSupprimer(Ami ami);

        void onItemModifier(Ami ami);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SupprModifCoordinatorLayout coordinatorLayout;
        public ImageButton supprimer;
        public ImageButton modifier;
        private TextView texteNom;
        private TextView textePrenom;
        private TextView texteAdresse;
        private TextView texteTel;

        public ViewHolder(View view) {
            super(view);
            coordinatorLayout = (SupprModifCoordinatorLayout) view;
            texteNom = (TextView) view.findViewById(R.id.nom);
            textePrenom = (TextView) view.findViewById(R.id.prenom);
            texteAdresse = (TextView) view.findViewById(R.id.adresse);
            texteTel = (TextView) view.findViewById(R.id.telephone);

            supprimer = (ImageButton) view.findViewById(R.id.supprimer);
            modifier = (ImageButton) view.findViewById(R.id.modifier);
        }
    }

    private class OnSupprimeListener implements View.OnClickListener {

        private Ami ami;

        public OnSupprimeListener(Ami ami) {
            this.ami = ami;
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemSupprimer(ami);
        }
    }

    private class OnModifieListener implements View.OnClickListener {

        private Ami ami;

        public OnModifieListener(Ami ami) {
            this.ami = ami;
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemModifier(ami);
        }
    }
}