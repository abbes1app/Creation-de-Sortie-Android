package com.example.amgsoft_pc.bestprojetgla.autre;

import java.util.ArrayList;
import java.util.Calendar;

public class LieuGraphe {
    public LieuGraphe pere;
    public ArrayList<LieuGraphe> fils = new ArrayList<LieuGraphe>();
    public long dateArrivee = Long.MAX_VALUE;
    public long dateRepart = Long.MAX_VALUE;
    public long distanceTaille = Long.MAX_VALUE;
    public long distanceTemps = Long.MAX_VALUE;
    public long attente = Long.MAX_VALUE;

    public int respectContraintes;

    public double latitude;
    public double longitude;
    public String nom;
    public String place_id;
    public String adresse;
    public String typePrincipal;
    public ArrayList<String> typesSecondaires;
    public String telephone;
    public double note;

    // En secondes, commence dimanche ([0][0] = heure de début dimanche)
    public int[][] horaires;

    public static String msToString(long date) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);


        return mDay + "/" + mMonth + "/" + mYear + " " + formateHeure(mHour, mMinute);
    }

    public static String msToStringNoYear(long date) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);


        return fill0(mDay) + "/" + fill0(mMonth) + " " + formateHeure(mHour, mMinute);
    }

    public static String fill0(int i) {
        return ((i < 10) ? "0" + i : i + "");
    }


    public static String tempToString(long temp) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(temp);

        int mDay = calendar.get(Calendar.DAY_OF_YEAR) - 1;
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        if (mDay == 0) {
            return formateHeure(mHour, mMinute);
        }

        return mDay + "j " + formateHeure(mHour, mMinute);
    }

    private static String formateHeure(int heures, int minutes) {
        return heures + "h" + fill0(minutes);
    }

    @Override
    public String toString() {
        String temp = "\n------------------------------------------\n";

        temp += (pere != null ? "A un" : "N'a pas de ") + " père et " + (fils != null ? "A un" : "N'a pas de ") + " fils\n";
        temp += "Date d'arrivée : " + msToString(dateArrivee) + "\n";
        temp += "Date repart : " + msToString(dateRepart) + "\n";
        temp += "Distance temp (s) : " + tempToString(distanceTemps * 1000) + "\n";
        temp += "Distance taille (m) : " + distanceTaille + "\n";
        temp += "Attente : " + tempToString(attente) + "\n";
        temp += "Latitude : " + latitude + "\n";
        temp += "Longitude : " + longitude + "\n";
        temp += "Nom : " + nom + "\n";
        temp += "place_id : " + place_id + "\n";
        temp += "Adresse : " + adresse + "\n";
        temp += "Type Principal : " + typePrincipal + "\n";

        temp += "Types secondaires : \n";
        if (typesSecondaires != null) {
            for (String type : typesSecondaires) {
                temp += "\t" + type + "\n";
            }
        }

        temp += "Télephone : " + telephone + "\n";
        temp += "Note : " + note + "\n";

        temp += "Horaires : \n";
        if (horaires != null) {
            for (int[] horaire : horaires) {
                temp += "\t" + tempToString(horaire[0] * 60 * 1000) + " --> " + tempToString(horaire[1] * 60 * 1000) + "\n";
            }
        }

        return temp;
    }
}
