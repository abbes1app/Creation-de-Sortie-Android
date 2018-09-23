package com.example.amgsoft_pc.bestprojetgla.autre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parsing {

    public static ArrayList<LieuGraphe> parseGooglePlace(JSONObject jObject) throws JSONException {
        ArrayList<LieuGraphe> liste = new ArrayList<LieuGraphe>(0);

        JSONArray jArray = jObject.getJSONArray("results");

        for (int i = 0; i < jArray.length(); i++) {
            liste.add(parseOneGooglePlace(jArray.optJSONObject(i)));
        }

        return liste;
    }

    public static LieuGraphe parseOneGooglePlace(JSONObject jObject) {
        JSONObject jLocate = jObject.optJSONObject("geometry").optJSONObject("location");

        double latitude = jLocate.optDouble("lat");
        double longitude = jLocate.optDouble("lng");

        String nom = jObject.optString("name");
        String place_id = jObject.optString("place_id");
        String adresse = "";
        String typePrincipal = "";

        JSONArray jTypesSecondaires = jObject.optJSONArray("types");
        ArrayList<String> typesSecondaires = new ArrayList<String>(0);
        if (jTypesSecondaires != null) {
            for (int j = 0; j < jTypesSecondaires.length(); j++) {
                typesSecondaires.add(jTypesSecondaires.optString(j));
            }
        }

        String telephone = "";
        double note = jObject.optDouble("rating");

        int[][] horaires = new int[7][2];

        LieuGraphe lieuTemp = new LieuGraphe();
        lieuTemp.latitude = latitude;
        lieuTemp.longitude = longitude;
        lieuTemp.nom = nom;
        lieuTemp.place_id = place_id;
        lieuTemp.adresse = adresse;
        lieuTemp.typePrincipal = typePrincipal;
        lieuTemp.typesSecondaires = typesSecondaires;
        lieuTemp.telephone = telephone;
        lieuTemp.note = note;
        lieuTemp.horaires = horaires;

        return lieuTemp;
    }

    public static LieuGraphe parseGooglePrecise(JSONObject jObject) {

        LieuGraphe lieuTemp = new LieuGraphe();

        jObject = jObject.optJSONObject("result");

        lieuTemp.telephone = jObject.optString("international_phone_number");

        try {
            JSONArray array = jObject.getJSONObject("opening_hours").getJSONArray("periods");

            int[][] horaires = new int[7][2];

            for (int i = 0; i < 7; i++) {
                horaires[i][0] = 0;
                horaires[i][1] = 0;
            }

            for (int i = 0; i < array.length(); i++) {
                JSONObject horaire = array.getJSONObject(i);

                if (horaire.has("close")) {
                    int jourOuvert = horaire.getJSONObject("open").getInt("day");
                    int heureOuvert = horaire.getJSONObject("open").getInt("time");

                    horaires[jourOuvert][0] = heureOuvert;


                    int jourFerme = horaire.getJSONObject("close").getInt("day");
                    int heureFerme = horaire.getJSONObject("close").getInt("time");

                    if (jourOuvert != jourFerme) {
                        jourFerme = jourFerme - 1 < 0 ? 6 : jourFerme - 1;
                    }

                    horaires[jourFerme][1] = heureFerme;
                }
                // System.out.println(horaire.toString());
            }

            lieuTemp.horaires = horaires;

        } catch (Exception e) {
            e.printStackTrace();

            int[][] horaires = new int[7][2];

            // Initialise ouvert H24
            for (int i = 0; i < 7; i++) {
                horaires[i][0] = 900;
                horaires[i][1] = 2000;
            }

            lieuTemp.horaires = horaires;
        }


        // Conversion en secondes
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                int heure = lieuTemp.horaires[i][j];
                lieuTemp.horaires[i][j] = (heure / 100) * 60 + heure - 100 * (heure / 100);
            }

            if (lieuTemp.horaires[i][1] == 0 && lieuTemp.horaires[i][0] != 0) {
                lieuTemp.horaires[i][1] = 24 * 60 - 1;
            }
        }

        return lieuTemp;
    }


    public static String parseToAdresse(JSONObject jObject) throws JSONException {
        JSONObject jLocate = jObject.getJSONArray("results").getJSONObject(0);

        return jLocate.getString("formatted_address");
    }


    public static double[] parseToLocation(JSONObject jObject) throws JSONException {
        JSONObject jLocate = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

        double latitude = jLocate.getDouble("lat");
        double longitude = jLocate.getDouble("lng");

        return new double[]{latitude, longitude};
    }


}
