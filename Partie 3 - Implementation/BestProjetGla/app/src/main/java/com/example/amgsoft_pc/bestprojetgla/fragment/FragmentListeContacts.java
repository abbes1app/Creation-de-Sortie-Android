package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.ContactArrayAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;

import java.util.ArrayList;

public class FragmentListeContacts extends ListFragment {

    ArrayList<Ami> listeContacts;
    ArrayList<Adresse> listeAdresse;
    ContactArrayAdapter adapter;
    private SelectionContactListener listener;

    public void setContactListener(SelectionContactListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Importer un contact");

        this.listeContacts = new ArrayList<Ami>();
        this.listeAdresse = new ArrayList<Adresse>();

        return inflater.inflate(R.layout.layout_liste, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chargerContacts();
        chargerAdresses();
        adapter = new ContactArrayAdapter(getContext(), listeContacts);
        setListAdapter(adapter);

        chargerContacts();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        getActivity().getSupportFragmentManager().popBackStack();

        if (listener != null) {
            Ami ami = listeContacts.get(position);

            for (Adresse adresse : listeAdresse) {
                if (adresse.getIdAdresse().compareTo(ami.getIdAmi()) == 0) {
                    ami.setIdAmi(null);
                    adresse.setIdAdresse(null);
                    listener.onContactLoaded(ami, adresse);
                    return;
                }
            }

            ami.setIdAmi(null);
            listener.onContactLoaded(ami, null);
        }
    }

    private void chargerContacts() {
        ArrayList<Ami> contacts = new ArrayList<Ami>();

        ContentResolver cr = getActivity().getContentResolver();

        Cursor contactCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        try {
            while (contactCursor.moveToNext()) {
                String idContact = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String nomPrenom = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String telephone = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));

                Ami ami = new Ami();

                ami.setIdAmi(idContact);
                if (nomPrenom.contains(" ")) {
                    ami.setNom(nomPrenom.substring(nomPrenom.split(" ")[0].length() + 1));
                    ami.setPrenom(nomPrenom.split(" ")[0]);
                } else {
                    ami.setPrenom(nomPrenom);
                }
                ami.setTelephone(telephone);

                contacts.add(ami);
            }
            contactCursor.close();
        } catch (NullPointerException e) {
            Log.d("ListeContacts contacts", e.toString());
        }

        listeContacts.clear();
        listeContacts.addAll(contacts);
    }

    private void chargerAdresses() {
        ArrayList<Adresse> adresses = new ArrayList<Adresse>();

        ContentResolver cr = getActivity().getContentResolver();

        Cursor adresseCursor = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, null, null, null);

        try {
            while (adresseCursor.moveToNext()) {
                String idContact = adresseCursor.getString(adresseCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID));
                String adresseComplete = adresseCursor.getString(adresseCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));

                Adresse adresse = new Adresse();

                adresse.setIdAdresse(idContact);
                adresse.setAdresse(adresseComplete);

                adresses.add(adresse);
            }
            adresseCursor.close();
        } catch (NullPointerException e) {
            Log.d("ListeContacts adresse", e.toString());
        }

        listeAdresse.clear();
        listeAdresse.addAll(adresses);
    }

    // Gestion du Listener
    interface SelectionContactListener {
        void onContactLoaded(Ami ami, Adresse adresse);
    }
}