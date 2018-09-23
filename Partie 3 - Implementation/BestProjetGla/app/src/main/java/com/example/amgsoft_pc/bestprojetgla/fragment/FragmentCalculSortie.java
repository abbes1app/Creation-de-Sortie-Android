package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.EtapeSimpleArrayAdapter;
import com.example.amgsoft_pc.bestprojetgla.autre.CalculSortie;
import com.example.amgsoft_pc.bestprojetgla.autre.LieuGraphe;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.amgsoft_pc.bestprojetgla.autre.LieuGraphe.msToStringNoYear;

public class FragmentCalculSortie extends Fragment {

    private static int[] iconesTransport;
    private static HashMap<String, Integer> iconesEtapes;
    ArrayList<LieuGraphe> listeNoeuds;
    ProgressDialog progressDialog;
    Button modifier, valider;
    boolean mShowingBack = false;
    private String transport;
    private String[] typeEtapes;
    private Sortie sortie;
    private int butProgress;

    public FragmentCalculSortie() {
        iconesTransport = new int[3];

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ArrayList<Transport> listeTransports = ManipulationTransport.liste(db);
        ArrayList<TypeEtape> listeType = ManipulationTypeEtape.liste(db);
        DatabaseManager.getInstance().closeDatabase();

        for (int i = 0; i < 3; i++) {
            iconesTransport[i] = Integer.valueOf(listeTransports.get(i).getIcon());
        }

        iconesEtapes = new HashMap<>(listeType.size());
        for (TypeEtape type : listeType) {
            iconesEtapes.put(type.getIdType(), Integer.valueOf(type.getImage()));
        }
    }

    private void flipCard() {
        mShowingBack = !mShowingBack;

        if (mShowingBack) {
            ResumeEtapesFrament fragment = new ResumeEtapesFrament();

            fragment.initialise(typeEtapes, listeNoeuds);

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_from_right,
                            R.anim.exit_to_left)
                    .replace(R.id.container, fragment)
                    .commit();
        } else {
            ResumeSortieFragment fragment = new ResumeSortieFragment();

            fragment.initialise(transport, typeEtapes,
                    listeNoeuds,
                    listeNoeuds.get(listeNoeuds.size() - 1).dateRepart - Long.valueOf(sortie.getDate()),
                    Long.valueOf(sortie.getDate()),
                    listeNoeuds.get(listeNoeuds.size() - 1).dateRepart);

            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_from_left,
                            R.anim.exit_to_right)
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Calcul des étapes");
        View rootView = inflater.inflate(R.layout.layout_calcul_sortie, container, false);


        // Réception ID sortie
        if (getArguments().containsKey("IDSortie")) {
            // Récupération de l'ID et de la sortie

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            sortie = ManipulationSortie.getAvecID(db, getArguments().getString("IDSortie"));
            ArrayList<Etape> listeEtapes = ManipulationEtape.liste(db, sortie.getIdSortie());
            DatabaseManager.getInstance().closeDatabase();

            this.transport = sortie.getIdTransport();

            typeEtapes = new String[listeEtapes.size()];
            for (int i = 0; i < typeEtapes.length; i++) {
                typeEtapes[i] = listeEtapes.get(i).getIdType();
            }
        }

        FrameLayout bulle = (FrameLayout) rootView.findViewById(R.id.container);
        bulle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flipCard();
            }
        });

        modifier = (Button) rootView.findViewById(R.id.calcul_bouton_modifier);
        modifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        valider = (Button) rootView.findViewById(R.id.calcul_bouton_accepter);
        valider.setVisibility(View.INVISIBLE);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save dans la BD
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

                ArrayList<Etape> listeEtapes = ManipulationEtape.liste(db, sortie.getIdSortie());
                ManipulationDatabase.supprimerEtapesSortie(db, sortie.getIdSortie());

                for (int i = 0; i < listeEtapes.size(); i++) {
                    Etape ancienne = listeEtapes.get(i);
                    LieuGraphe nouvelle = listeNoeuds.get(i);
                    Adresse adresse = new Adresse();

                    ancienne.setAttenteAvant(Long.toString(nouvelle.attente));

                    ancienne.setDebut(Long.toString(nouvelle.dateArrivee));
                    ancienne.setFin(Long.toString(nouvelle.dateRepart));

                    adresse.setNom(nouvelle.nom);
                    adresse.setAdresse(nouvelle.adresse);
                    adresse.setTelephone(nouvelle.telephone);
                    adresse.setLatitude(Double.toString(nouvelle.latitude));
                    adresse.setLongitude(Double.toString(nouvelle.longitude));

                    ancienne.setIdAdresse(Long.toString(ManipulationAdresse.inserer(db, adresse)));

                    ManipulationEtape.inserer(db, ancienne);
                }

                sortie.valider();
                ManipulationSortie.update(db, sortie);


                DatabaseManager.getInstance().closeDatabase();


                Fragment fragment = new FragmentDetailSortie();

                // Envoie de l'ID de sortie
                Bundle bundle = new Bundle();
                bundle.putString("IDSortie", sortie.getIdSortie());
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content, fragment);
                transaction.commit();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Démarage....");
        progressDialog.setTitle("Calcul de votre sortie en cours...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        lanceCalcul();
    }

    private void fini(LieuGraphe result) {
        listeNoeuds = new ArrayList<LieuGraphe>(0);

        LieuGraphe initial = result;

        long attenteTotal = 0;

        while (result.fils != null && result.fils.size() > 0) {
            result = result.fils.get(0);

            Log.d("Resultat", result.toString());

            attenteTotal += result.attente;

            listeNoeuds.add(result);
        }


        ResumeSortieFragment fragment = new ResumeSortieFragment();

        fragment.initialise(
                transport,
                typeEtapes,
                listeNoeuds,
                listeNoeuds.get(listeNoeuds.size() - 1).dateRepart - Long.valueOf(sortie.getDate()),
                Long.valueOf(sortie.getDate()),
                listeNoeuds.get(listeNoeuds.size() - 1).dateRepart);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    public void lanceCalcul() {

        valider.setVisibility(View.INVISIBLE);

        progressDialog.show();


        CalculSortie.Listener listener = new CalculSortie.Listener() {
            @Override
            public void onFinished(LieuGraphe result) {

                if (result != null) {
                    fini(result);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            valider.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                } else {
                    progressDialog.setCancelable(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            try {
                                getActivity().onBackPressed();
                            }catch(Exception e){
                            }
                        }
                    }, 3000);
                }

            }

            @Override
            public void onProgress(String texte, double avance) {
                butProgress = (int) avance;
                startAnimation();
                afficheSecure(texte);
            }

            @Override
            public void onLog(String texte) {

            }
        };


        CalculSortie temp = new CalculSortie(listener, sortie.getIdSortie());
        temp.execute();
    }

    private void afficheSecureListe() {
        String temp = "";

        for (int i = 0; i < listeNoeuds.size(); i++) {
            temp += listeNoeuds.get(i).toString();
        }

        afficheSecure(temp);
    }

    private void afficheSecure(final String texte) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(texte);
            }
        });
    }

    private void startAnimation() {
        progressDialog.setProgress(butProgress);
    }

    public static class ResumeSortieFragment extends Fragment {

        String valTransport;
        String[] valEtapes;
        long valDuree, valAttente, valDebut, valFin;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_resume_sortie, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            if (valTransport != null)
                remplisFinal(valTransport, valEtapes, valDuree, valAttente, valDebut, valFin);
        }

        private String tempToString(long date){
            date = date/(1000*60);

            return (date/60) + "h" + ((date%60)<10 ? ("0" + (date%60)) : (date%60));
        }

        public void initialise(String valTransport, String[] valEtapes, ArrayList<LieuGraphe> listeNoeuds, long valDuree, long valDebut, long valFin) {
            this.valTransport = valTransport;
            this.valEtapes = valEtapes;
            this.valDuree = valDuree;

            this.valAttente = 0;
            for(LieuGraphe lieu : listeNoeuds){
                this.valAttente += lieu.attente;
            }

            this.valDebut = valDebut;
            this.valFin = valFin;
        }

        private void remplisFinal(String valTransport, String[] valEtapes, long valDuree, long valAttente, long valDebut, long valFin) {
            int[] listeEtapes = new int[]{
                    R.id.sortie_calcule_etape_1,
                    R.id.sortie_calcule_etape_2,
                    R.id.sortie_calcule_etape_3,
                    R.id.sortie_calcule_etape_4,
                    R.id.sortie_calcule_etape_5};

            for (int i = 0; i < valEtapes.length; i++) {
                ((ImageView) getView().findViewById(listeEtapes[i])).setImageResource(iconesEtapes.get(valEtapes[i]));
            }

            ImageView transport = (ImageView) getView().findViewById(R.id.sortie_calcule_transport);
            transport.setImageResource(iconesTransport[Integer.valueOf(valTransport)]);

            TextView duree = (TextView) getView().findViewById(R.id.sortie_calcule_duree);
            duree.setText(tempToString(valDuree));

            TextView attente = (TextView) getView().findViewById(R.id.sortie_calcule_attente);
            attente.setText(tempToString(valAttente));

            TextView debut = (TextView) getView().findViewById(R.id.sortie_calcule_debut);
            debut.setText(msToStringNoYear(valDebut));

            TextView fin = (TextView) getView().findViewById(R.id.sortie_calcule_fin);
            fin.setText(msToStringNoYear(valFin));

            CardView bulle = (CardView) getView().findViewById(R.id.bulle_resume_sortie);
            bulle.setVisibility(View.VISIBLE);
        }
    }

    public static class ResumeEtapesFrament extends Fragment {
        EtapeSimpleArrayAdapter adapter;

        String[] valEtapes;
        ArrayList<LieuGraphe> listeNoeuds;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_resume_etapes, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);


            if (valEtapes != null)
                remplisFinal(valEtapes, listeNoeuds);
        }

        public void initialise(String[] valEtapes, ArrayList<LieuGraphe> listeNoeuds) {
            this.valEtapes = valEtapes;
            this.listeNoeuds = listeNoeuds;
        }

        private void remplisFinal(String[] valEtapes, ArrayList<LieuGraphe> listeNoeuds) {
            ArrayList<Integer> listeIconEtape = new ArrayList<Integer>(0);
            ArrayList<String> listeNomEtape = new ArrayList<String>(0);


            for(int i=0;i<listeNoeuds.size();i++){
                listeIconEtape.add(iconesEtapes.get(valEtapes[i]));
                listeNomEtape.add(listeNoeuds.get(i).nom);
            }


            ListView listeView = (ListView) getView().findViewById(R.id.liste_etapes);
            adapter = new EtapeSimpleArrayAdapter(getContext(), listeIconEtape, listeNomEtape);
            listeView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }
}

