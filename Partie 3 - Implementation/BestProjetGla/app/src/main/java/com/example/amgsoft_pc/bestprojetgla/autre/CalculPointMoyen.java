package com.example.amgsoft_pc.bestprojetgla.autre;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculPointMoyen extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    private Listener listener;
    private Context context;

    private String IDSortie;

    private ArrayList<Adresse> listeAdresses;

    public CalculPointMoyen(Activity activity, Context context, Listener listener) {
        this.progressDialog = new ProgressDialog(activity);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMax(100);
        progressDialog.setMessage("Démarage....");
        progressDialog.setTitle("Calcul de votre sortie en cours...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private double[] getLocation(String adresse){
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(adresse, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                return new double[]{latitude, longitude};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAdresse(double latitude, double longitude){
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            if (addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0);
                for(int i=1;i<addresses.get(0).getMaxAddressLineIndex();i++){
                    address += ", " + addresses.get(0).getAddressLine(i);
                }
                address += ", " + addresses.get(0).getCountryName();

                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String doInBackground(String... params) {
        IDSortie = params[0];

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        listeAdresses = ManipulationDatabase.listeAdressesParticipantsSortie(db, IDSortie);
        DatabaseManager.getInstance().closeDatabase();

        progressDialog.setMax(listeAdresses.size());

        if(listeAdresses.size() == 0){
            return "0;Calcul impossible, il n'y a aucun participant à cette sortie";
        }

        Double latitude = null;
        Double longitude = null;
        int nbResultats = 0;

        for(Adresse adresse : listeAdresses){
            double[] location = new double[2];

            if(adresse.getLatitude() != null && adresse.getLatitude().length()>0){
                // Si deja calculé ne pas refaire
                location[0] = Double.valueOf(adresse.getLatitude());
                location[1] = Double.valueOf(adresse.getLongitude());
            }else {
                // Sinon calcul des coordonées
                location = getLocation(adresse.getAdresse());
            }

            // Si on a un résultat
            if(location != null){
                // On l'ajoute dans l'adresse
                adresse.setLatitude(Double.toString(location[0]));
                adresse.setLongitude(Double.toString(location[1]));

                if(latitude == null){
                    latitude = location[0];
                    longitude = location[1];
                }else{
                    latitude += location[0];
                    longitude += location[1];
                }
                nbResultats++;
            }

            // On incrémente la progress bar
            progressDialog.incrementProgressBy(1);
        }


        // MaJ des adresses dans la BD
        db = DatabaseManager.getInstance().openDatabase();
        for(Adresse adresse : listeAdresses){
            ManipulationAdresse.update(db, adresse);
        }
        DatabaseManager.getInstance().closeDatabase();


        // Si aucune coordonées n'a été trouvée
        if(nbResultats == 0){
            return "0;Aucune adresse valide parmis vos contacts";
        }else{
            latitude /= nbResultats;
            longitude /= nbResultats;
        }

        return "1;" + getAdresse(latitude, longitude);
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


        if(result == null){
            listener.onError("Erreur inconue.\nVérifiez votre connection internet");
        }else{
            String[] results = result.split(";");

            int resultat = Integer.valueOf(results[0]);

            if(resultat == 0){
                listener.onError(results[1]);
            }else{
                listener.onFinished(results[1]);
            }
        }
    }


    public interface Listener {
        void onFinished(String adresse);
        void onError(String erreur);
    }

}