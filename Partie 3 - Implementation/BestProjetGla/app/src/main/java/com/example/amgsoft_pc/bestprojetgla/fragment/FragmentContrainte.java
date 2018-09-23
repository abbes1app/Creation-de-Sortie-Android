package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.autre.CustomAdapter;
import com.example.amgsoft_pc.bestprojetgla.autre.SpinnerModel;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienContraintesSorties;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationLienContraintesSorties;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import java.util.ArrayList;
import java.util.List;


public class FragmentContrainte extends Fragment {
    ArrayList<SpinnerModel> listeSpinner = new ArrayList<>();
    CustomAdapter adapter;

    RecyclerView MyRecyclerView;
    RecyclerView MyRecyclerView1;
    Spinner spinnerTypeEtapes;

    String IDSortie;

    ArrayList<TypeEtape> listeTypeEtapes;

    ArrayList<ArrayList<ContraintesBase>> listeListeContraintesAffiche;

    ArrayList<ArrayList<ContraintesPerso>> listeListeContraintesBase;
    ArrayList<ArrayList<ContraintesPerso>> listeListeContraintesPerso;

    RecyclerView.Adapter adapteurContraintesPerso;

    private ArrayList<Integer> nbContraintes;
    private static int NB_MAX_CONTRAINTES = 4;

    public FragmentContrainte() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Contraintes");
        View rootview = inflater.inflate(R.layout.fragment_fragment_contrainte, container, false);

        // Réception ID sortie
        if (getArguments().containsKey("IDSortie")) {
            IDSortie = getArguments().getString("IDSortie");

            spinnerTypeEtapes = (Spinner) rootview.findViewById(R.id.spinner);

            List<String> listeNomEtapes = new ArrayList<String>();

            ArrayList<Integer> listeIconeEtapes = new ArrayList<Integer>();


            listeListeContraintesAffiche = new ArrayList<>();
            listeListeContraintesBase = new ArrayList<>();
            listeListeContraintesPerso = new ArrayList<>();

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            listeTypeEtapes = new ArrayList<>();
            for (Etape etape : ManipulationEtape.liste(db, IDSortie)) {
                TypeEtape typeEtape = ManipulationTypeEtape.getAvecID(db, etape.getIdType());
                listeTypeEtapes.add(typeEtape);

                listeNomEtapes.add(typeEtape.getNom());
                listeIconeEtapes.add(Integer.valueOf(typeEtape.getImage()));

                listeListeContraintesAffiche.add(ManipulationContraintesBase.liste(db, etape.getIdType()));


                listeListeContraintesBase.add(ManipulationContraintesBase.listeAvecAvis(db, IDSortie, etape.getIdType()));
                listeListeContraintesPerso.add(ManipulationContraintesPerso.liste(db, IDSortie, etape.getIdType()));
            }
            DatabaseManager.getInstance().closeDatabase();

            // Remplissage du Spinner
            for (int i = 0; i < listeNomEtapes.size(); i++) {
                final SpinnerModel spn = new SpinnerModel();
                spn.setTxt(listeNomEtapes.get(i));
                spn.setImg(listeIconeEtapes.get(i));
                listeSpinner.add(spn);
            }
        }

        if(listeListeContraintesAffiche.size() == 0){
            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getContext(), "Vous ne pourrez ajouter des contraintes qu'après avoir au moins une étape",Toast.LENGTH_LONG).show();
            return rootview;
        }

        spinnerTypeEtapes = (Spinner) rootview.findViewById(R.id.spinner);
        adapter = new CustomAdapter(getContext(), listeSpinner);
        spinnerTypeEtapes.setAdapter(adapter);
        spinnerTypeEtapes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyRecyclerView.setAdapter(new MyAdapter(listeListeContraintesAffiche.get(position), listeListeContraintesBase.get(position)));

                adapteurContraintesPerso = new MyAdapter1(listeListeContraintesPerso.get(position));
                MyRecyclerView1.setAdapter(adapteurContraintesPerso);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MyRecyclerView = (RecyclerView) rootview.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);

        MyRecyclerView1 = (RecyclerView) rootview.findViewById(R.id.cardView1);
        MyRecyclerView1.setHasFixedSize(true);

        Button AjouterC = (Button) rootview.findViewById(R.id.AjouterC);
        AjouterC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        Button ValiderC = (Button) rootview.findViewById(R.id.ValiderC);
        ValiderC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                ManipulationContraintesPerso.remove(db, IDSortie);
                ManipulationLienContraintesSorties.remove(db, IDSortie);

                for(int i=0; i<listeTypeEtapes.size(); i++){

                    ArrayList<ContraintesPerso> listeContraintesBase = listeListeContraintesBase.get(i);
                    ArrayList<ContraintesBase> listeContraintesAffiche = listeListeContraintesAffiche.get(i);
                    for(int j=0; j<listeContraintesBase.size(); j++){
                        LienContraintesSorties lien = new LienContraintesSorties();

                        lien.setAvis(listeContraintesBase.get(j).getAvis());
                        lien.setIdContrainteBase(listeContraintesAffiche.get(j).getIdContrainteBase());
                        lien.setIdSortie(IDSortie);

                        ManipulationLienContraintesSorties.inserer(db, lien);
                    }


                    ArrayList<ContraintesPerso> listeContraintesPerso = listeListeContraintesPerso.get(i);
                    for(ContraintesPerso cp : listeContraintesPerso){
                        ManipulationContraintesPerso.inserer(db, cp);
                    }

                }
                DatabaseManager.getInstance().closeDatabase();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if(listeListeContraintesAffiche.size() > 0){
            MyRecyclerView.setAdapter(new MyAdapter(listeListeContraintesAffiche.get(0), listeListeContraintesBase.get(0)));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        LinearLayoutManager MyLayoutManager1 = new LinearLayoutManager(getActivity());
        MyLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        if(listeListeContraintesPerso.size() > 0){
            adapteurContraintesPerso = new MyAdapter1(listeListeContraintesPerso.get(0));
            MyRecyclerView1.setAdapter(adapteurContraintesPerso);
        }
        MyRecyclerView1.setLayoutManager(MyLayoutManager1);


        nbContraintes = new ArrayList<>();
        for(int i=0;i<listeTypeEtapes.size();i++){
            int nbChoix = 0;
            for(ContraintesPerso cp : listeListeContraintesBase.get(i)){
                if(cp.getAvis().compareTo("0") != 0){
                    nbChoix++;
                }
            }

            for(ContraintesPerso cp : listeListeContraintesPerso.get(i)){
                if(cp.getAvis().compareTo("0") != 0){
                    nbChoix++;
                }
            }

            nbContraintes.add(nbChoix);
        }


        return rootview;
    }


    public void showDialog() {
        int i = spinnerTypeEtapes.getSelectedItemPosition();
        if(nbContraintes.get(i) == NB_MAX_CONTRAINTES){
            Toast.makeText(getContext(), "Impossible d'ajouter plus de " + NB_MAX_CONTRAINTES + " contraites", Toast.LENGTH_SHORT).show();
            return ;
        }

        final Dialog mydialog = new Dialog(getContext());
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.fragment_my_dialog);

        final EditText boite = (EditText) mydialog.findViewById(R.id.contrainte_perso);

        mydialog.show();

        Button aime = (Button) mydialog.findViewById(R.id.jaime);
        aime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boite.getText().toString().trim().length() == 0){
                    mydialog.dismiss();
                    return ;
                }

                int i = spinnerTypeEtapes.getSelectedItemPosition();

                ContraintesPerso item = new ContraintesPerso();
                item.setAvis("1");
                item.setNom(boite.getText().toString());
                item.setIdType(listeTypeEtapes.get(i).getIdType());
                item.setIdSortie(IDSortie);


                if(trop(1)){
                    mydialog.dismiss();
                    return;
                }

                listeListeContraintesPerso.get(i).add(item);
                adapteurContraintesPerso.notifyDataSetChanged();
                mydialog.dismiss();
            }
        });

        Button aimePas = (Button) mydialog.findViewById(R.id.jaime_pas);
        aimePas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boite.getText().toString().trim().length() == 0){
                    mydialog.dismiss();
                    return ;
                }

                int i = spinnerTypeEtapes.getSelectedItemPosition();

                ContraintesPerso item = new ContraintesPerso();
                item.setAvis("2");
                item.setNom(boite.getText().toString());
                item.setIdType(listeTypeEtapes.get(i).getIdType());
                item.setIdSortie(IDSortie);

                if(trop(1)){
                    mydialog.dismiss();
                    return;
                }

                listeListeContraintesPerso.get(i).add(item);
                adapteurContraintesPerso.notifyDataSetChanged();
                mydialog.dismiss();
            }
        });
    }

    private boolean trop(int add){
        int i = spinnerTypeEtapes.getSelectedItemPosition();
        if(add>0 && nbContraintes.get(i) == NB_MAX_CONTRAINTES){
            Toast.makeText(getContext(), "Impossible d'ajouter plus de " + NB_MAX_CONTRAINTES + " contraites", Toast.LENGTH_SHORT).show();
            return true;
        }
        nbContraintes.set(i, nbContraintes.get(i) + add);
        return false;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            ImageView coverImageView;
            ImageView likeImageView;
            ImageView DeslikeImageView;

            MyViewHolder(View v) {
                super(v);

                titleTextView = (TextView) v.findViewById(R.id.titleTextView);
                coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
                likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
                DeslikeImageView = (ImageView) v.findViewById(R.id.DeslikeImageView);
            }
        }

        private ArrayList<ContraintesBase> listeAffichage;
        private ArrayList<ContraintesPerso> listeChoix;

        MyAdapter(ArrayList<ContraintesBase> listeAffichage, ArrayList<ContraintesPerso> listeChoix) {
            this.listeAffichage = listeAffichage;
            this.listeChoix = listeChoix;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contrainte_item, parent, false);
            return new MyViewHolder(view);
        }

        private void actualiseChoix(MyViewHolder holder, int position){
            int avis = Integer.valueOf(listeChoix.get(position).getAvis());

            if(avis == 1){
                holder.likeImageView.setImageResource(R.drawable.avis_like);
                holder.DeslikeImageView.setImageResource(R.drawable.avis_dislike_gris);
            }else if(avis == 2){
                holder.likeImageView.setImageResource(R.drawable.avis_like_gris);
                holder.DeslikeImageView.setImageResource(R.drawable.avis_dislike);
            }else{
                holder.likeImageView.setImageResource(R.drawable.avis_like_gris);
                holder.DeslikeImageView.setImageResource(R.drawable.avis_dislike_gris);
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.titleTextView.setText(listeAffichage.get(position).getNom());
            holder.coverImageView.setImageResource(Integer.valueOf(listeAffichage.get(position).getImage()));

            holder.likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int avis = Integer.valueOf(listeChoix.get(position).getAvis());

                    if(avis == 1){
                        listeChoix.get(position).setAvis("0");
                        if(trop(-1) && avis==0){
                            return ;
                        }

                    }else{
                        if(trop(1) && avis==0){
                            return ;
                        }

                        listeChoix.get(position).setAvis("1");
                    }

                    actualiseChoix(holder, position);
                }
            });

            holder.DeslikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int avis = Integer.valueOf(listeChoix.get(position).getAvis());

                    if(avis == 2){
                        listeChoix.get(position).setAvis("0");
                        if(trop(-1) && avis==0){
                            return ;
                        }
                    }else{
                        if(trop(1) && avis==0){
                            return ;
                        }
                        listeChoix.get(position).setAvis("2");
                    }

                    actualiseChoix(holder, position);
                }
            });

            actualiseChoix(holder, position);
        }

        @Override
        public int getItemCount() {
            return listeAffichage.size();
        }
    }

    public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.MyViewHolder1> {

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView text;

            ImageView supprItem;
            ImageView iconAvis;

            MyViewHolder1(View v) {
                super(v);
                text = (TextView) v.findViewById(R.id.text);
                iconAvis = (ImageView) v.findViewById(R.id.icon_avis);
                supprItem = (ImageView) v.findViewById(R.id.suppr_item);
            }
        }

        private ArrayList<ContraintesPerso> listeContraintesPerso;

        MyAdapter1(ArrayList<ContraintesPerso> listeContraintesPerso) {
            this.listeContraintesPerso = listeContraintesPerso;
        }

        @Override
        public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contrainte_item1, parent, false);
            return new MyViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder1 holder1, final int position) {
            holder1.text.setText(listeContraintesPerso.get(position).getNom());

            if(Integer.valueOf(listeContraintesPerso.get(position).getAvis()) == 1){
                holder1.iconAvis.setImageResource(R.drawable.avis_like);
            }else{
                holder1.iconAvis.setImageResource(R.drawable.avis_dislike);
            }

            holder1.supprItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeContraintesPerso.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                    if(trop(-1)){
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listeContraintesPerso.size();
        }
    }
}
