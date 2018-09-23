package com.example.amgsoft_pc.bestprojetgla.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;
import com.example.amgsoft_pc.bestprojetgla.swipe.SupprModifCoordinatorLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class SortieSwipeAdapter extends RecyclerView.Adapter<SortieSwipeAdapter.ViewHolder> {

    private static int[] iconesTransport;
    private static HashMap<String, Integer> iconesEtapes;
    private ArrayList<Sortie> listeSorties;
    private OnItemListener onItemListener;
    public SortieSwipeAdapter(ArrayList<Sortie> sorties) {

        if (iconesTransport == null || iconesEtapes == null) {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            // Récupération des images des Transports
            if (iconesTransport == null) {
                iconesTransport = new int[3];
                ArrayList<Transport> liste = ManipulationTransport.liste(db);

                for (int i = 0; i < 3; i++) {
                    iconesTransport[i] = Integer.valueOf(liste.get(i).getIcon());
                }
            }

            // Récupération des images des TypeEtapes
            if (iconesEtapes == null) {
                ArrayList<TypeEtape> listeType = ManipulationTypeEtape.liste(db);

                iconesEtapes = new HashMap<>(listeType.size());
                for (TypeEtape type : listeType) {
                    iconesEtapes.put(type.getIdType(), Integer.valueOf(type.getImage()));
                }
            }

            DatabaseManager.getInstance().closeDatabase();
        }
        this.listeSorties = sorties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sortie_glisse, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // fill data
        Sortie sortie = listeSorties.get(position);

        holder.texteNom.setText(sortie.getNom());
        holder.texteLieu.setText(sortie.getAdresse());
        holder.texteDate.setText(Sortie.dateToString(sortie.getDate()));
        holder.texteHeure.setText(Sortie.heureToString(sortie.getDate()));

        int transport = -1;
        if (sortie.getIdTransport() != null) {
            transport = Integer.valueOf(sortie.getIdTransport());
        }
        if (transport >= 0 && transport < 3) {
            holder.imageTransport.setImageResource(iconesTransport[transport]);
        } else {
            holder.imageTransport.setImageResource(android.R.color.transparent);
        }

        for (int i = 0; i < holder.imagesEtapes.length; i++) {
            holder.imagesEtapes[i].setImageResource(android.R.color.transparent);
        }

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ArrayList<Etape> listeEtapes = ManipulationEtape.liste(db, sortie.getIdSortie());
        for (int i = 0; i < listeEtapes.size(); i++) {
            holder.imagesEtapes[i].setImageResource(iconesEtapes.get(listeEtapes.get(i).getIdType()));
        }
        DatabaseManager.getInstance().closeDatabase();

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
        return listeSorties.size();
    }

    public void setOnItemClickListener(OnItemListener listener) {
        onItemListener = listener;
    }

    public interface OnItemListener {
        void onItemSupprimer(Sortie sortie);

        void onItemModifier(Sortie sortie);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SupprModifCoordinatorLayout coordinatorLayout;
        public ImageButton supprimer;
        public ImageButton modifier;
        private TextView texteNom;
        private TextView texteLieu;
        private TextView texteDate;
        private TextView texteHeure;
        private ImageView imageTransport;
        private ImageView[] imagesEtapes;

        public ViewHolder(View view) {
            super(view);
            coordinatorLayout = (SupprModifCoordinatorLayout) view;
            texteNom = (TextView) view.findViewById(R.id.nom_sortie);
            texteLieu = (TextView) view.findViewById(R.id.lieu_sortie);
            texteDate = (TextView) view.findViewById(R.id.date_sortie);
            texteHeure = (TextView) view.findViewById(R.id.heure_sortie);
            imageTransport = (ImageView) view.findViewById(R.id.image_transport);

            imagesEtapes = new ImageView[]{
                    (ImageView) view.findViewById(R.id.icone_etape_1),
                    (ImageView) view.findViewById(R.id.icone_etape_2),
                    (ImageView) view.findViewById(R.id.icone_etape_3),
                    (ImageView) view.findViewById(R.id.icone_etape_4),
                    (ImageView) view.findViewById(R.id.icone_etape_5)};

            supprimer = (ImageButton) view.findViewById(R.id.supprimer);
            modifier = (ImageButton) view.findViewById(R.id.modifier);
        }
    }

    private class OnSupprimeListener implements View.OnClickListener {

        private Sortie sortie;

        public OnSupprimeListener(Sortie sortie) {
            this.sortie = sortie;
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemSupprimer(sortie);
        }
    }

    private class OnModifieListener implements View.OnClickListener {

        private Sortie sortie;

        public OnModifieListener(Sortie sortie) {
            this.sortie = sortie;
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemModifier(sortie);
        }
    }
}