package com.example.amgsoft_pc.bestprojetgla.autre;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.amgsoft_pc.bestprojetgla.autre.Parsing.parseGooglePlace;
import static com.example.amgsoft_pc.bestprojetgla.autre.Parsing.parseGooglePrecise;
import static com.example.amgsoft_pc.bestprojetgla.autre.Parsing.parseToLocation;

public class CalculSortie extends AsyncTask<Void, String, LieuGraphe> {
    private static final String KEY_API_PLACE = "AIzaSyB5047UJpKy9PjQ2q24hVIz7ZSqN_oDEtY";
    private static final String KEY_API_DETAIL = "AIzaSyBbR8Ofw8FTD6qzoMPj_MIwrjFJH02X8FM";
    private static final String KEY_API_DISTANCE = "AIzaSyDY5pPKCXAnccFisiUsqeGBJQJot3wihVI";
    private static final String KEY_API_GEOCODING = "AIzaSyDzyyhXBGe4cvw8iqzxmJ6GBfFDF5t-NW8";

    private static final String BASE_URL_PLACE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String BASE_URL_PLACE_RADAR = "https://maps.googleapis.com/maps/api/place/radarsearch/json";
    private static final String BASE_URL_DETAIL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String BASE_URL_DISTANCE = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String BASE_URL_GEOCODING = "https://maps.googleapis.com/maps/api/geocode/json";

    private double progressBar;
    private double pas;
    private Listener listener;

    private String IDSortie;

    private EtapeGraphe etapeBase;
    private LieuGraphe lieuBase;

    public CalculSortie(Listener listener, String IDSortie) {
        this.listener = listener;
        this.IDSortie = IDSortie;
        progressBar = 0;
    }

    public CalculSortie(Listener listener) {
        this.listener = listener;
        progressBar = 0;
    }

    private String flattenToAscii(String string) {
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out);
    }

    private double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // convert to meters
    }

    private double[] nomVersCoord(String adresse) throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_GEOCODING).newBuilder();
        urlBuilder.addQueryParameter("key", KEY_API_GEOCODING);
        urlBuilder.addQueryParameter("address", adresse);
        String url = urlBuilder.build().toString();

        return parseToLocation(getSafeJSON(url));
    }

    @Override
    public void onPreExecute() {
        listener.onProgress("Démarage...", progressBar);


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Sortie sortie = ManipulationSortie.getAvecID(db, IDSortie);
        Transport transport = ManipulationTransport.getAvecID(db, sortie.getIdTransport());
        ArrayList<Etape> listeEtapes = ManipulationEtape.liste(db, IDSortie);


        ArrayList<EtapeGraphe> suivantes = new ArrayList<>(0);

        for (Etape etape : listeEtapes) {
            TypeEtape typeEtape = ManipulationTypeEtape.getAvecID(db, etape.getIdType());

            EtapeGraphe etapeSuivante = new EtapeGraphe();
            etapeSuivante.suivant = null;
            etapeSuivante.nbEmbranchements = 1;
            etapeSuivante.duree = Long.valueOf(typeEtape.getDuree());
            etapeSuivante.type = typeEtape.getType();

            // Recuperer les contraintes
            ArrayList<ContraintesPerso> listeContraintes = ManipulationDatabase.listeContrainteSortie(db, IDSortie, typeEtape.getIdType());

            etapeSuivante.contraintes = new ArrayList<String>(0);
            etapeSuivante.nonContraintes = new ArrayList<String>(0);


            for (ContraintesPerso contrainte : listeContraintes) {
                String requette = contrainte.getNom();
                requette = requette.trim();
                requette = flattenToAscii(requette);
                requette = requette.replaceAll("/[^A-Za-z0-9 ]/", "");

                if (contrainte.getAvis().compareTo("1") == 0) {
                    etapeSuivante.contraintes.add(requette);
                } else if (contrainte.getAvis().compareTo("2") == 0) {
                    etapeSuivante.nonContraintes.add(requette);
                }
            }


            etapeSuivante.transportQuery = transport.getQuery();
            etapeSuivante.transportRayon = Integer.valueOf(sortie.getRayon())*1000;


            suivantes.add(etapeSuivante);

            if (suivantes.size() > 1) {
                suivantes.get(suivantes.size() - 2).suivant = etapeSuivante;
            }
        }

        etapeBase = suivantes.get(0);


        lieuBase = new LieuGraphe();
        lieuBase.dateRepart = Long.valueOf(sortie.getDate());
        lieuBase.adresse = sortie.getAdresse();

        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    protected LieuGraphe doInBackground(Void... params) {


        try {
            double[] coord = nomVersCoord(lieuBase.adresse);
            // Recherche de l'adresse de départ
            lieuBase.latitude = coord[0];
            lieuBase.longitude = coord[1];
            Log.d("Recherche adr", coord[0] + ";" + coord[1]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Recherche adr", "Echec lors de la recherche d'adresse");
            return null;
        }


        try {
            int nbEtapes = 1;
            EtapeGraphe temp = etapeBase;
            while (temp.suivant != null) {
                nbEtapes++;
                temp = temp.suivant;
            }


            pas = 100.0 / (nbEtapes * 5);
            recherche(lieuBase, etapeBase);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lieuBase;
    }

    private void addAll(int respect, ArrayList<LieuGraphe> liste1, ArrayList<LieuGraphe> liste2) {
        for (LieuGraphe lieu : liste2) {
            lieu.respectContraintes = respect;
            liste1.add(lieu);
        }
    }

    private void recherche(LieuGraphe lieuPere, EtapeGraphe etape) throws IOException, JSONException, InterruptedException {
        // chargement de la liste des lieux respectant 0, 1 ou plusieurs contraintes
        listener.onProgress("Chargement des lieux possibles...", progressBar);

        ArrayList<LieuGraphe> listeValide = new ArrayList<LieuGraphe>();

        if (etape.contraintes.size() != 0) {
            for (String contrainte : etape.contraintes) {
                addAll(1, listeValide, getListeLieux(lieuPere.latitude, lieuPere.longitude, etape.transportRayon, etape.type, contrainte, false, false));
            }
        }

        if(listeValide.size() < 2){
            addAll(0, listeValide, getListeLieux(lieuPere.latitude, lieuPere.longitude, etape.transportRayon, etape.type, "", false, false));
        }


        // Si on a trouvé trop peu on change de mode de recherche
        if(listeValide.size() <= 5){
            addAll(0, listeValide, getListeLieux(lieuPere.latitude, lieuPere.longitude, etape.transportRayon, etape.type, "", false, true));

            if (etape.contraintes.size() != 0) {
                for (String contrainte : etape.contraintes) {
                    addAll(1, listeValide, getListeLieux(lieuPere.latitude, lieuPere.longitude, etape.transportRayon, etape.type, contrainte, false, true));
                }
            }
        }


        // On ne dépasse pas le rayon imposé
        for (int i = 0; i < listeValide.size(); i++) {
            LieuGraphe lieu = listeValide.get(i);
            if (distance(lieuPere.latitude, lieu.latitude, lieuPere.longitude, lieu.longitude) > etape.transportRayon) {
                listeValide.remove(i);
                i--;
            }
        }


        // Calcul de la distance maximum
        double rayonMaxTrouve = 0;
        for (LieuGraphe lieu : listeValide) {
            rayonMaxTrouve = Math.max(rayonMaxTrouve, distance(lieuPere.latitude, lieu.latitude, lieuPere.longitude, lieu.longitude));
        }


        progressBar += pas;
        listener.onProgress("Lieux touvés : " + listeValide.size(), progressBar);


        Thread.sleep(500);

        listener.onProgress("Chargement des lieux interdits...", progressBar);

        // Chargement de la liste des lieux où il ne faut pas aller (on ratisse large)
        ArrayList<LieuGraphe> listeInvalide = new ArrayList<LieuGraphe>(0);

        for (String nonContrainte : etape.nonContraintes) {
            listeInvalide.addAll(getListeLieux(lieuPere.latitude, lieuPere.longitude, (int) rayonMaxTrouve, etape.type, nonContrainte, true, true));
        }

        progressBar += pas;
        listener.onProgress("Lieux interdit touvés : " + listeInvalide.size(), progressBar);


        Thread.sleep(500);


        listener.onProgress("Réduit la liste des possibles", progressBar);

        // On supprime les doublons en comptant leurs nombres
        for (int i = 0; i < listeValide.size(); i++) {
            LieuGraphe place = listeValide.get(i);

            for (int j = i + 1; j < listeValide.size(); j++) {
                if (listeValide.get(j).place_id.compareTo(place.place_id) == 0) {
                    listeValide.remove(j);
                    place.respectContraintes++;

                    j--;
                }
            }
        }


        // On enlève les lieux interdit de la liste des possibles
        for (int i = 0; i < listeInvalide.size(); i++) {
            String placeID = listeInvalide.get(i).place_id;
            for (int j = 0; j < listeValide.size(); j++) {
                if (listeValide.get(j).place_id.compareTo(placeID) == 0) {
                    listeValide.remove(j);
                    j--;
                }
            }
        }


        // On enlève les lieux d'après le titre (ceux passé dans les mailles)
        for (int i = 0; i < listeValide.size(); i++) {
            String nom = " " + flattenToAscii(listeValide.get(i).nom).toLowerCase() + " ";
            for (String nonContrainte : etape.nonContraintes) {
                if (nom.contains(" " + flattenToAscii(nonContrainte.toLowerCase()) + " ")) {
                    listeValide.remove(i);
                    i--;
                    break;
                }
            }
        }


        // Remplace les NaN,  met min à 1 et contraintes++
        for (int i = 0; i < listeValide.size(); i++) {
            LieuGraphe place = listeValide.get(i);
            if (Double.isNaN(place.note)) {
                place.note = 2;
            }
            place.note++;
            place.respectContraintes++;
        }


        progressBar += pas;
        listener.onProgress("Liste des lieux possibles restants : " + listeValide.size(), progressBar);


        Thread.sleep(500);


        // On limite le nombre de lieux fils à 50 max. On garde les meilleurs (contraintes * notes maximum)
        while (listeValide.size() > 50) {
            int idMin = (int) (Math.random() * listeValide.size());
            double minimum = listeValide.get(idMin).respectContraintes * listeValide.get(idMin).note;

            // recherche le plus petit score
            for (int i = 0; i < listeValide.size(); i++) {

                if ((listeValide.get(i).respectContraintes * listeValide.get(i).note) < minimum) {
                    minimum = (listeValide.get(i).respectContraintes * listeValide.get(i).note);
                    idMin = i;
                }
            }

            listeValide.remove(idMin);
        }


        listener.onProgress("Calcul des distances", progressBar);

        // On mesure la distance entre le noeud père et tout ses fils (temps et longeur)
        appliquerDistance(lieuPere.latitude, lieuPere.longitude, etape.transportQuery, listeValide);


        progressBar += pas;
        listener.onProgress("Calcul des horaires des 7 meilleurs", progressBar);


        // On garde les 7 meilleurs (  (durée trajet)/(note * contraintes)  )
        ArrayList<LieuGraphe> listeLieuxPres = new ArrayList<LieuGraphe>(0);
        for (int bla = 0; bla < Math.min(listeValide.size(), 7); bla++) {
            int idMin = (int) (Math.random() * listeValide.size());
            double minimum = listeValide.get(idMin).distanceTemps / (listeValide.get(idMin).respectContraintes * listeValide.get(idMin).note);

            // recherche le plus petit score
            for (int i = 0; i < listeValide.size(); i++) {
                LieuGraphe lieuTemp = listeValide.get(i);
                double valeur = lieuTemp.distanceTemps / (lieuTemp.respectContraintes * lieuTemp.note);

                if (valeur < minimum) {
                    minimum = valeur;
                    idMin = i;
                }
            }

            listeLieuxPres.add(listeValide.get(idMin));
            listeValide.remove(idMin);
        }


        // On précises les dates (Google details)
        for (LieuGraphe lieu : listeLieuxPres) {
            preciseLieu(lieu);
        }


        // On élimine les lieux toujours fermés
        for (int i = 0; i < listeLieuxPres.size(); i++) {
            boolean toutZero = true;
            for (int[] heures : listeLieuxPres.get(i).horaires) {
                if (heures[0] != 0 || heures[1] != 0) {
                    toutZero = false;
                    break;
                }
            }

            if (toutZero) {
                listeLieuxPres.remove(i);
                i--;
            }
        }


        // On calcule la vrai date d'arrivée pour chaque fils
        for (LieuGraphe lieu : listeLieuxPres) {
            long arrive = lieuPere.dateRepart + lieu.distanceTemps * 1000;
            long reel = calculVraiDate(arrive, lieu.horaires);

            lieu.attente = reel - arrive;
            lieu.dateArrivee = reel;
        }


        progressBar += pas;
        listener.onProgress("Récupération du meilleur", progressBar);

        Thread.sleep(500);


        // On garde ceux avec le moins de temps d'attente
        ArrayList<LieuGraphe> listeLieuxVraimentsBons = new ArrayList<LieuGraphe>(0);
        for (int bla = 0; bla < Math.min(listeLieuxPres.size(), etape.nbEmbranchements); bla++) {
            int idMin = (int) (Math.random() * listeLieuxPres.size());
            long minimum = listeLieuxPres.get(idMin).dateArrivee;

            // recherche le plus petit score
            for (int i = 0; i < listeLieuxPres.size(); i++) {
                LieuGraphe lieuTemp = listeLieuxPres.get(i);
                long valeur = lieuTemp.dateArrivee;

                if (valeur < minimum) {
                    minimum = valeur;
                    idMin = i;
                }
            }

            listeLieuxVraimentsBons.add(listeLieuxPres.get(idMin));
            listeLieuxPres.remove(idMin);
        }


        // On recusrsif si etape.suivant != null
        lieuPere.fils = listeLieuxVraimentsBons;


        for (LieuGraphe lieu : listeLieuxVraimentsBons) {
            lieu.pere = lieuPere;
            lieu.dateRepart = lieu.dateArrivee + etape.duree;
        }

        if (etape.suivant != null) {
            for (LieuGraphe lieu : listeLieuxVraimentsBons) {
                recherche(lieu, etape.suivant);
            }
        }

    }

    // dateArrive en milisecondes
    // horaires en minutes
    private long calculVraiDate(long dateArrive, int[][] horaires) {
        if (horaires == null) {
            return dateArrive + (7 * 24 * 60 * 60 * 1000);
        }

        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(dateArrive);

        // En minutes
        int heureArrive = calendrier.get(Calendar.HOUR_OF_DAY) * 60 + calendrier.get(Calendar.MINUTE);

        switch (calendrier.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 0);
                break;
            case Calendar.MONDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 1);
                break;
            case Calendar.TUESDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 2);
                break;
            case Calendar.WEDNESDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 3);
                break;
            case Calendar.THURSDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 4);
                break;
            case Calendar.FRIDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 5);
                break;
            case Calendar.SATURDAY:
                dateArrive += tempsDattente(heureArrive, horaires, 6);
                break;
        }
        return dateArrive;
    }

    // Calcul le temps qu'il faudra attendre devant le lieu (en milisecondes)
    private long tempsDattente(int heureArrive, int[][] horaires, int jour) {
        long attente = 0;

        for (int i = 0; i < 7; i++) {
            int jourAvant = jour - 1 < 0 ? 6 : jour - 1;

            if (horaires[jour][0] == 0 && horaires[jour][1] == 0) {
                attente += (24 * 60) - heureArrive;
                heureArrive = 0;
                jour = (jour + 1) % 7;

                // Si c'est ouver après minuit et toujours quand on arrive on attend pas
            } else if (horaires[jourAvant][1] < horaires[jourAvant][0] && heureArrive < horaires[jourAvant][1]) {
                // Minutes vers milisecondes
                attente = attente * 60 * 1000;
                return attente;
                // Si on est avant l'ouverture on attend
            } else if (heureArrive < horaires[jour][0]) {
                attente += horaires[jour][0] - heureArrive;

                // Minutes vers milisecondes
                attente = attente * 60 * 1000;
                return attente;
                // Si c'est ouvert (ou que ça ferme après minuit donc forcément ouvert) et pas fermé on n'attend pas
            } else if (heureArrive < horaires[jour][1] || (horaires[jour][1] < horaires[jour][0])) {
                // Minutes vers milisecondes
                attente = attente * 60 * 1000;
                return attente;
                // Sinon il faut attendre le lendemain
            } else {
                attente += (24 * 60) - heureArrive;
                heureArrive = 0;
                jour = (jour + 1) % 7;
            }
        }

        // Minutes vers milisecondes
        attente = attente * 60 * 1000;
        // Si on en est là c'est que c'est toujours fermé
        return attente;
    }

    private void preciseLieu(LieuGraphe lieu) throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_DETAIL).newBuilder();
        urlBuilder.addQueryParameter("key", KEY_API_DETAIL);
        urlBuilder.addQueryParameter("placeid", lieu.place_id);
        urlBuilder.addQueryParameter("language", "fr");
        String url = urlBuilder.build().toString();

        LieuGraphe lieuTemp = parseGooglePrecise(getSafeJSON(url));

        lieu.telephone = lieuTemp.telephone;
        lieu.horaires = lieuTemp.horaires;
    }

    private ArrayList<LieuGraphe> appliquerDistance(double latitude, double longitude, String transport, ArrayList<LieuGraphe> listeLieux) throws IOException, JSONException, InterruptedException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_DISTANCE).newBuilder();
        urlBuilder.addQueryParameter("key", KEY_API_DISTANCE);
        urlBuilder.addQueryParameter("origins", latitude + "," + longitude);

        String listeDestinations = "";
        for (LieuGraphe lieu : listeLieux) {
            listeDestinations += "|" + lieu.latitude + "," + lieu.longitude;
        }

        urlBuilder.addQueryParameter("destinations", listeDestinations.substring(1));
        urlBuilder.addQueryParameter("mode", transport);
        urlBuilder.addQueryParameter("units", "metric");
        urlBuilder.addQueryParameter("language", "fr");
        String url = urlBuilder.build().toString();

        JSONObject jObject = getSafeJSON(url);

        JSONArray listeAdresse = jObject.optJSONArray("destination_addresses");
        for (int i = 0; i < listeLieux.size(); i++) {
            listeLieux.get(i).adresse = listeAdresse.optString(i);
        }

        JSONArray listeRows = jObject.optJSONArray("rows").optJSONObject(0).optJSONArray("elements");

        for (int i = 0; i < listeLieux.size(); i++) {
            try {
                listeLieux.get(i).distanceTaille = listeRows.optJSONObject(i).optJSONObject("distance").optLong("value");
                listeLieux.get(i).distanceTemps = listeRows.optJSONObject(i).optJSONObject("duration").optLong("value");
            } catch (NullPointerException e) {
                e.printStackTrace();
                listeLieux.remove(i);
                i--;
            }
        }

        return null;
    }

    private ArrayList<LieuGraphe> getListeLieux(double latitude, double longitude, int rayon, String type, String contrainte, boolean radar, boolean prominence) throws IOException, JSONException, InterruptedException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(radar ? BASE_URL_PLACE_RADAR : BASE_URL_PLACE).newBuilder();
        urlBuilder.addQueryParameter("key", KEY_API_PLACE);
        urlBuilder.addQueryParameter("location", latitude + "," + longitude);

        if (prominence) {
            urlBuilder.addQueryParameter("radius", Integer.toString(rayon));
            urlBuilder.addQueryParameter("rankby", "prominence");
        } else {
            urlBuilder.addQueryParameter("rankby", "distance");
        }
        urlBuilder.addQueryParameter("types", type);
        urlBuilder.addQueryParameter("keyword", contrainte);
        urlBuilder.addQueryParameter("language", "fr");
        String url = urlBuilder.build().toString();

        return getListeLieux(url);
    }

    private ArrayList<LieuGraphe> getListeLieux(String url) throws IOException, JSONException, InterruptedException {
        ArrayList<LieuGraphe> listeNoeuds = new ArrayList<LieuGraphe>(0);

        JSONObject jObject = getSafeJSON(url);

        if (false && jObject.has("next_page_token")) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL_PLACE).newBuilder();
            urlBuilder.addQueryParameter("key", KEY_API_PLACE);
            urlBuilder.addQueryParameter("pagetoken", jObject.getString("next_page_token"));
            String newUrl = urlBuilder.build().toString();

            Thread.sleep(18000);

            listeNoeuds.addAll(getListeLieux(newUrl));
        }

        listeNoeuds.addAll(parseGooglePlace(jObject));

        return listeNoeuds;
    }

    private JSONObject getSafeJSON(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient();

        try {
            JSONObject jObject = new JSONObject(client.newCall(request).execute().body().string());

            // Si la requette a marché mais que google dit qu'il y a erreur on se stoppe
            if ("OK".compareTo(jObject.optString("status")) != 0 && "ZERO_RESULTS".compareTo(jObject.optString("status")) != 0) {
                Log.d("Calcul Sortie", "Retourne " + jObject.optString("status") + " pour l'URL " + url);
                throw new IOException();
            }

            return jObject;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            Log.d("Calcul Sortie", "Pas de connection internet");
            throw e;
        }

        throw new IOException();
    }

    @Override
    public void onPostExecute(LieuGraphe result) {
        if (result == null) {
            listener.onProgress("Une erreur c'est produite (pas de connexion internet)", 0);
        } else {
            listener.onProgress("Fin", 100);
        }

        listener.onFinished(result);
    }

    public interface Listener {
        void onFinished(LieuGraphe result);

        void onProgress(String texte, double avance);

        void onLog(String texte);
    }
}
