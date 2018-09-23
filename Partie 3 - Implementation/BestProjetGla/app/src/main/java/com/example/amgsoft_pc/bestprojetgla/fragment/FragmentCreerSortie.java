package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amgsoft_pc.bestprojetgla.MyBounceInterpolator;
import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.GooglePlacesAutocompleteAdapter;
import com.example.amgsoft_pc.bestprojetgla.autre.CalculPointMoyen;
import com.example.amgsoft_pc.bestprojetgla.autre.DatePickerFragment;
import com.example.amgsoft_pc.bestprojetgla.autre.TimePickerFragment;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.view.ValueSelector;

import java.util.ArrayList;

public class FragmentCreerSortie extends Fragment implements View.OnClickListener {
    static int[][] icones;
    static Button[] listeTransports = new Button[3];
    static String[] listeDistancesTransports = new String[3];

    int transportSelect = -1;

    DatePickerFragment datePicker = new DatePickerFragment();
    TimePickerFragment timePicker = new TimePickerFragment();

    AutoCompleteTextView autoCompView;

    Sortie sortieEnCours;

    public FragmentCreerSortie() {
        icones = new int[2][3];
        listeDistancesTransports = new String[3];

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ArrayList<Transport> liste = ManipulationTransport.liste(db);
        DatabaseManager.getInstance().closeDatabase();

        for (int i = 0; i < 3; i++) {
            icones[0][i] = Integer.valueOf(liste.get(i).getIcon());
            icones[1][i] = Integer.valueOf(liste.get(i).getIconGris());
            listeDistancesTransports[i] = liste.get(i).getRayon();
        }
    }

    private static void actualiseTransport(int id, Animation myAnim) {
        actualiseTransport(id);

        if (id != -1) {
            listeTransports[id].startAnimation(myAnim);
        }
    }

    private static void actualiseTransport(int id) {
        if (id != -1) {
            for (int i = 0; i < 3; i++) {
                if (i != id) {
                    listeTransports[i].setBackgroundResource(icones[1][i]);
                }
            }

            listeTransports[id].setBackgroundResource(icones[0][id]);
        } else {
            for (int i = 0; i < 3; i++) {
                listeTransports[i].setBackgroundResource(icones[0][i]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Créer Une Sortie");
        View rootView = inflater.inflate(R.layout.layout_creer_sortie, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Réception ID sortie
        if (getArguments().containsKey("IDSortie")) {
            // Récupération de l'ID et de la sortie
            String IDSortie = getArguments().getString("IDSortie");
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            sortieEnCours = ManipulationSortie.getAvecID(db, IDSortie);
            DatabaseManager.getInstance().closeDatabase();

            // Initialisation de l'ecran selon cette sortie
            if (!TextUtils.isEmpty(sortieEnCours.getNom())) {
                EditText champNom = (EditText) rootView.findViewById(R.id.nom_sortie);
                champNom.setText(sortieEnCours.getNom());
            }

            if (!TextUtils.isEmpty(sortieEnCours.getAdresse())) {
                AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.lieu_sortie);
                autoCompView.setText(sortieEnCours.getAdresse());

            }

            if (!TextUtils.isEmpty(sortieEnCours.getDate())) {
                datePicker.setDate(sortieEnCours.getDateAnnee(), sortieEnCours.getDateMois(), sortieEnCours.getDateJour());
                timePicker.setTime(sortieEnCours.getDateHeure(), sortieEnCours.getDateMinute());
            }

            if (!TextUtils.isEmpty(sortieEnCours.getIdTransport())) {
                transportSelect = Integer.valueOf(sortieEnCours.getIdTransport());
            }

            if (!TextUtils.isEmpty(sortieEnCours.getRayon())) {
                ValueSelector valSelect = (ValueSelector) rootView.findViewById(R.id.valueSelector);
                valSelect.setValue(Integer.valueOf(sortieEnCours.getRayon()));
            } else {
                ValueSelector valSelect = (ValueSelector) rootView.findViewById(R.id.valueSelector);
                valSelect.setValue(5);
            }
        }

        ValueSelector valSelect = (ValueSelector) rootView.findViewById(R.id.valueSelector);
        valSelect.setMinValue(1);
        valSelect.setMaxValue(50);

        TextView dateView = (TextView) rootView.findViewById(R.id.choix_date);
        datePicker.setTextView(dateView);
        dateView.setText(datePicker.getToday());
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                datePicker.show(fm, "datePicker");
            }
        });

        rootView.findViewById(R.id.imageViewCalendrier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                datePicker.show(fm, "datePicker");
            }
        });

        TextView heureView = (TextView) rootView.findViewById(R.id.choix_heure);
        timePicker.setTextView(heureView);
        heureView.setText(timePicker.getNow());
        heureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                timePicker.show(fm, "heurePicker");
            }
        });

        rootView.findViewById(R.id.imageViewHorloge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                timePicker.show(fm, "heurePicker");
            }
        });

        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.lieu_sortie);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_tiem));

        rootView.findViewById(R.id.bouton_annuler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Button next = (Button) rootView.findViewById(R.id.bouton_choix_etapes);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentEtapes());
            }
        });

        Button participant = (Button) rootView.findViewById(R.id.bouton_choix_participants);
        participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentAjoutParticipant());
            }
        });

        Button Contraintes = (Button) rootView.findViewById(R.id.bouton_choix_contraintes);
        Contraintes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new FragmentContrainte());
            }
        });

        Button valide = (Button) rootView.findViewById(R.id.bouton_valider_sortie);
        valide.setOnClickListener(this);

        listeTransports[0] = (Button) rootView.findViewById(R.id.bouton_transport_1);
        listeTransports[1] = (Button) rootView.findViewById(R.id.bouton_transport_2);
        listeTransports[2] = (Button) rootView.findViewById(R.id.bouton_transport_3);

        for (int i = 0; i < 3; i++) {
            final int id = i;

            final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 20);
            myAnim.setInterpolator(interpolator);

            listeTransports[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualiseTransport(id, myAnim);
                    transportSelect = id;
                }
            });
        }

        actualiseTransport(transportSelect);

        TextView iteroge = (TextView) rootView.findViewById(R.id.question_lieu);
        iteroge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Choisissez un lieux de départ.\nSi ce champ reste vide, le lieux le plus proche de tous vos amis sera pris comme point de départ.").setTitle("Choix du lieux");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        EditText champNom = (EditText) getView().findViewById(R.id.nom_sortie);
        sortieEnCours.setNom(champNom.getText().toString());

        EditText champVille = (EditText) getView().findViewById(R.id.lieu_sortie);
        sortieEnCours.setAdresse(champVille.getText().toString());

        sortieEnCours.setDate(datePicker.getToday(), timePicker.getNow());

        sortieEnCours.setIdTransport(Integer.toString(transportSelect));

        ValueSelector valSelect = (ValueSelector) getView().findViewById(R.id.valueSelector);
        sortieEnCours.setRayon(Integer.toString(valSelect.getValue()));

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ManipulationSortie.update(db, sortieEnCours);
        DatabaseManager.getInstance().closeDatabase();

        super.onPause();
    }

    private void openFragment(Fragment fragment) {
        // Envoie de l'ID de sortie
        Bundle bundle = new Bundle();
        bundle.putString("IDSortie", sortieEnCours.getIdSortie());
        fragment.setArguments(bundle);

        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        // Vérification que la sortie est valide

        EditText champNom = (EditText) getView().findViewById(R.id.nom_sortie);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        boolean existe = ManipulationSortie.nameExist(db, champNom.getText().toString());
        DatabaseManager.getInstance().closeDatabase();
        if(existe){
            makeToast("Ce nom de sortie existe déjà");
            return;
        }

        if (champVide(R.id.nom_sortie)) {
            makeToast("Vous devez donner un nom à cette sortie");
            return;
        }


        if(!isInternetAvailable()){
            makeToast("Une connexion internet est necessaire pour calculer votre sortie");
            return;
        }


        if (champVide(R.id.lieu_sortie)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Lieu de départ");
            alert.setMessage("Vous n'avez pas renseigné de lieu de départ.\n" +
                    "Voulez-vous que je trouve le lieu arangeant tous les participants ?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CalculPointMoyen temp = new CalculPointMoyen(getActivity(), getContext(), new CalculPointMoyen.Listener() {
                        @Override
                        public void onFinished(String adresse) {
                            Toast.makeText(getContext(), "Point de départ idéal trouvé", Toast.LENGTH_SHORT).show();
                            autoCompView.setText(adresse);
                        }

                        @Override
                        public void onError(String erreur) {
                            Toast.makeText(getContext(), "Erreur : " + erreur, Toast.LENGTH_LONG).show();
                        }
                    });
                    temp.execute(sortieEnCours.getIdSortie());
                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.show();
            return;
        }

        if (transportSelect == -1) {
            makeToast("Vous devez choisir un moyen de transport");
            return;
        }

        db = DatabaseManager.getInstance().openDatabase();
        ArrayList<Etape> listeTemp = ManipulationEtape.liste(db, sortieEnCours.getIdSortie());
        DatabaseManager.getInstance().closeDatabase();
        if (listeTemp.size() == 0) {
            makeToast("Vous devez définir au moins une étape");
            return;
        }

        openFragment(new FragmentCalculSortie());
    }

    private void makeToast(String texte) {
        Toast.makeText(getContext(), texte, Toast.LENGTH_LONG).show();
    }

    private boolean champVide(int id) {
        EditText texteTemp = (EditText) getView().findViewById(id);
        String texte = texteTemp.getText().toString().trim();

        return (texte.length() == 0);
    }
}

