package com.example.amgsoft_pc.bestprojetgla.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.AmiArrayAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import java.util.ArrayList;

public class FragmentCalculeeListeAmis extends ListFragment implements DialogInterface.OnClickListener {

    ArrayList<Ami> listeAmis;
    AmiArrayAdapter adapter;

    String messageGroupe;

    AlertDialog.Builder alert;

    String IDSortie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().containsKey("IDSortie")) {
            IDSortie = getArguments().getString("IDSortie");
        }

        View view = inflater.inflate(R.layout.layout_calculee_participants, container, false);

        ImageButton boutonSMS = (ImageButton) view.findViewById(R.id.bouton_calculee_sms);
        boutonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    alert.setMessage(calculMessage(IDSortie));
                    alert.show();
                }
            }
        });

        alert = new AlertDialog.Builder(getContext());
        alert.setTitle("SMS groupé");
        alert.setPositiveButton(android.R.string.yes, this);
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        listeAmis = ManipulationDatabase.listeParticipantsSortie(db, IDSortie);
        DatabaseManager.getInstance().closeDatabase();


        adapter = new AmiArrayAdapter(getContext(), listeAmis);
        setListAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public String calculMessage(String IDSortie) {
        if (messageGroupe == null) {

            ArrayList<String> listeTypeEtapes = new ArrayList<String>();

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ArrayList<Adresse> listeAdresseEtapes = ManipulationDatabase.listeAdressesEtapes(db, IDSortie);
            ArrayList<Etape> listeEtapes = ManipulationEtape.liste(db, IDSortie);
            for(Etape e : listeEtapes){
                String temp = ManipulationTypeEtape.getAvecID(db, e.getIdType()).getArticle();
                temp += " ";
                temp += ManipulationTypeEtape.getAvecID(db, e.getIdType()).getNom();
                listeTypeEtapes.add(temp);
            }
            Sortie sortie = ManipulationSortie.getAvecID(db, IDSortie);
            DatabaseManager.getInstance().closeDatabase();

            messageGroupe = "Bonjour, je t'invite à ma sortie '" + sortie.getNom() +
                    "' pour aller ";

            messageGroupe += listeTypeEtapes.get(0);

            for (int i = 1; i < listeAdresseEtapes.size()-1; i++) {
                messageGroupe += ", " + listeTypeEtapes.get(i);
            }

            if(listeAdresseEtapes.size()>1){
                messageGroupe += " et " + listeTypeEtapes.get(listeTypeEtapes.size()-1);
            }

            messageGroupe += ".\n\n";


            messageGroupe += "Rendez-vous le ";
            messageGroupe += Sortie.dateToString(sortie.getDate());
            messageGroupe += " à ";
            messageGroupe += Sortie.heureToString(sortie.getDate());

            messageGroupe += " à la première étape :\n";
            messageGroupe += listeAdresseEtapes.get(0).getNom() + "\n";
            messageGroupe += listeAdresseEtapes.get(0).getAdresse();
            messageGroupe += ".\n\n";


            messageGroupe += "La sortie devrait se finir le ";
            messageGroupe += Sortie.dateToString(listeEtapes.get(listeEtapes.size()-1).getFin());
            messageGroupe += " vers ";
            messageGroupe += Sortie.heureToString(listeEtapes.get(listeEtapes.size()-1).getFin());
            messageGroupe += ".";
        }

        return "Souhaitez-vous envoyer ceci à tous les participants :\n" + messageGroupe;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SmsManager sms = SmsManager.getDefault();

        ArrayList<String> messageDecoupe = sms.divideMessage(messageGroupe + "\n\nSortie organisée par BestProjetGla");

        Toast.makeText(getContext(), "Envoi des SMS à tous les participants", Toast.LENGTH_SHORT).show();
        for (Ami ami : listeAmis) {
            if (ami.getTelephone() != null && ami.getTelephone().length() > 0) {
                sms.sendMultipartTextMessage(ami.getTelephone(), null, messageDecoupe, null, null);
            }
        }
        Toast.makeText(getContext(), "SMS envoyés", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Aucun SMS ne peut être envoyé sans autorisation", Toast.LENGTH_LONG).show();
            } else {
                ImageButton boutonSMS = (ImageButton) getView().findViewById(R.id.bouton_calculee_sms);
                boutonSMS.callOnClick();
            }
        }
    }
}