package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.GooglePlacesAutocompleteAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAmi;

import java.io.IOException;
import java.util.regex.Pattern;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class FragmentCreerParticipant extends Fragment implements View.OnClickListener {
    Button importer, add;
    EditText name;

    String IDAmi;
    PrevientUpdate previent;

    public FragmentCreerParticipant() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Créer un participant");
        View rootView = inflater.inflate(R.layout.layout_creer_participant, container, false);

        if (getArguments() != null && getArguments().containsKey("IDAmi")) {
            getActivity().setTitle("Modifier un participant");
            IDAmi = getArguments().getString("IDAmi");


            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

            Ami ami = ManipulationAmi.getAvecID(db, IDAmi);

            remplisSiPlein(rootView.findViewById(R.id.nom), ami.getNom());
            remplisSiPlein(rootView.findViewById(R.id.prenom), ami.getPrenom());
            remplisSiPlein(rootView.findViewById(R.id.telephone), ami.getTelephone());

            Adresse adresse = ManipulationAdresse.getAvecID(db, ami.getIdAdresse());
            if (adresse != null) {
                remplisSiPlein(rootView.findViewById(R.id.Adresse), adresse.getAdresse());
            }

            DatabaseManager.getInstance().closeDatabase();
        }

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.Adresse);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_tiem));

        importer = (Button) rootView.findViewById(R.id.Importer);
        add = (Button) rootView.findViewById(R.id.add);
        name = (EditText) rootView.findViewById(R.id.nom);

        add.setOnClickListener(this);

        importer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    FragmentListeContacts fragment = new FragmentListeContacts();

                    // Initialisation du Listener pour retourner un contact
                    fragment.setContactListener(new FragmentListeContacts.SelectionContactListener() {
                        @Override
                        public void onContactLoaded(Ami ami, Adresse adresse) {
                            remplisChampsContact(ami);
                            remplisChampsAdresse(adresse);
                        }
                    });

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_content7, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return rootView;
    }

    private void viderChamps() {
        remplisSiPlein(getView().findViewById(R.id.nom), "");
        remplisSiPlein(getView().findViewById(R.id.prenom), "");
        remplisSiPlein(getView().findViewById(R.id.telephone), "");
        remplisSiPlein(getView().findViewById(R.id.Adresse), "");
    }

    private void changeOnglet() {
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        viewPager.setCurrentItem(0);
        previent.onUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Aucun contact ne peut être chargé sans autorisation.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Vous pouvez maintenant importer l'un de vos contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void remplisSiPlein(View champTexte, String texte) {
        if (texte != null) {
            ((EditText) champTexte).setText(texte);
        } else {
            ((EditText) champTexte).setText("");
        }
    }

    private void remplisChampsContact(Ami ami) {
        if (ami != null) {
            remplisSiPlein(getView().findViewById(R.id.nom), ami.getNom());
            remplisSiPlein(getView().findViewById(R.id.prenom), ami.getPrenom());
            remplisSiPlein(getView().findViewById(R.id.telephone), ami.getTelephone());
        }
    }

    private void remplisChampsAdresse(Adresse adresse) {
        if (adresse != null) {
            remplisSiPlein(getView().findViewById(R.id.Adresse), adresse.getAdresse());
        }
    }

    @Override
    public void onClick(View v) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Ami ami = new Ami();

        EditText champTexte = (EditText) getView().findViewById(R.id.telephone);
        String tel = champTexte.getText().toString();

        if(isValidMobile(tel)){
            ami.setTelephone(tel);
        }else{
            ami.setTelephone(null);
        }

        champTexte = (EditText) getView().findViewById(R.id.nom);
        ami.setNom(champTexte.getText().toString());

        champTexte = (EditText) getView().findViewById(R.id.prenom);
        ami.setPrenom(champTexte.getText().toString());

        Adresse adresse = new Adresse();
        AutoCompleteTextView champTexte1 = (AutoCompleteTextView) getView().findViewById(R.id.Adresse);
        adresse.setAdresse(champTexte1.getText().toString());

        try {
            if (ami.getNom().compareTo("") == 0 && ami.getPrenom().compareTo("") == 0) {
                throw new IOException();
            }


            // Si c'est une modification
            if (IDAmi != null) {
                Ami oldAmi = ManipulationAmi.getAvecID(db, IDAmi);

                // Si l'adresse a été modifié on la MaJ
                Adresse oldAdresse = ManipulationAdresse.getAvecID(db, oldAmi.getIdAdresse());
                if (oldAdresse != null) {
                    adresse.setIdAdresse(oldAdresse.getIdAdresse());
                    ManipulationAdresse.update(db, adresse);

                    ami.setIdAdresse(oldAmi.getIdAdresse());
                } else if (adresse.getAdresse().length() != 0) {
                    Toast.makeText(getContext(), "Creation " + adresse.getAdresse(), Toast.LENGTH_SHORT).show();

                    long idAdresse = ManipulationAdresse.inserer(db, adresse);
                    ami.setIdAdresse(Long.toString(idAdresse));
                }

                ami.setIdAmi(oldAmi.getIdAmi());
                ManipulationAmi.update(db, ami);

                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                if (adresse.getAdresse().length() != 0) {
                    long idAdresse = ManipulationAdresse.inserer(db, adresse);
                    ami.setIdAdresse(Long.toString(idAdresse));
                }

                ManipulationAmi.inserer(db, ami);

                viderChamps();

                changeOnglet();
            }
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getContext(), "Ajout impossible, un ami similaire existe déjà", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ajout impossible, il faut renseigner le nom et/ou prénom", Toast.LENGTH_LONG).show();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    interface PrevientUpdate {
        void onUpdate();
    }
}