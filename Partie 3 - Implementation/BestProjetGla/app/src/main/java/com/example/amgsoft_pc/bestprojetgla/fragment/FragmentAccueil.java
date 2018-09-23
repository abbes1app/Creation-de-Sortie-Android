package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.activity.CreationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import static com.example.amgsoft_pc.bestprojetgla.autre.LieuGraphe.msToStringNoYear;

public class FragmentAccueil extends android.support.v4.app.Fragment {
    Button btnsortir;

    Sortie sortie;

    public FragmentAccueil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Accueil");
        View rootView = inflater.inflate(R.layout.layout_accueil, container, false);

        btnsortir = (Button) rootView.findViewById(R.id.btnsortir);
        btnsortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), CreationSortie.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(mainIntent);
            }
        });

        View layout = rootView.findViewById(R.id.item_etape);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortie != null){
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
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        View rootView = getView();

        // Recherche de la première étape pas encore fini
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Etape etape = ManipulationDatabase.prochaineEtape(db, Long.toString(System.currentTimeMillis()));
        if(etape != null) {
            // Si on est est resté la moitiée du temps à cette étape on affiche la suivante
            long nouvelleDate = System.currentTimeMillis();
            nouvelleDate += (Long.valueOf(etape.getFin()) - Long.valueOf(etape.getDebut())) / 2;

            Etape etapeSuivante = ManipulationDatabase.prochaineEtape(db, Long.toString(nouvelleDate));
            if(etapeSuivante != null && etapeSuivante.getIdSortie().compareTo(etape.getIdSortie()) == 0){
                etape = etapeSuivante;
            }


            TypeEtape typeEtape = ManipulationTypeEtape.getAvecID(db, etape.getIdType());
            sortie = ManipulationSortie.getAvecID(db, etape.getIdSortie());
            Transport transport = ManipulationTransport.getAvecID(db, sortie.getIdTransport());
            Adresse adresse = ManipulationAdresse.getAvecID(db, etape.getIdAdresse());


            ((LinearLayout) rootView.findViewById(R.id.layout_temps_trajet)).removeAllViews();
            rootView.findViewById(R.id.layout_temps_trajet).setVisibility(View.INVISIBLE);


            TextView texteArrive = (TextView) rootView.findViewById(R.id.texte_arrive);
            texteArrive.setText(msToStringNoYear(Long.valueOf(etape.getDebut())));

            TextView texteDepart = (TextView) rootView.findViewById(R.id.texte_depart);
            texteDepart.setText(msToStringNoYear(Long.valueOf(etape.getFin())));


            ImageView imageTypeEtape = (ImageView) rootView.findViewById(R.id.image_type_etape);
            imageTypeEtape.setImageResource(Integer.valueOf(typeEtape.getImage()));

            TextView texteNom = (TextView) rootView.findViewById(R.id.texte_nom_etape);
            texteNom.setText(adresse.getNom());

            TextView texteAdresse = (TextView) rootView.findViewById(R.id.texte_adresse_etape);
            texteAdresse.setText(adresse.getAdresse());

            TextView texteTelephone = (TextView) rootView.findViewById(R.id.texte_telephone_etape);
            texteTelephone.setText(adresse.getTelephone());
        }else{
            View layout = rootView.findViewById(R.id.item_etape);
            layout.setVisibility(View.INVISIBLE);
        }
        DatabaseManager.getInstance().closeDatabase();

        super.onResume();
    }
}