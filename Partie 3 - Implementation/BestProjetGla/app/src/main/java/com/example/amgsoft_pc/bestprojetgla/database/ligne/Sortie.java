package com.example.amgsoft_pc.bestprojetgla.database.ligne;

import java.util.Calendar;

public class Sortie {
    // Identification de la classe
    public static final String TAG = Sortie.class.getSimpleName();
    public static final String TABLE = "Sortie";

    // Nom des colonnes
    public static final String KEY_ID_SORTIE = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_DATE = "DateMili";
    public static final String KEY_ID_TRANSPORT = "IdTransport";
    public static final String KEY_ADRESSE = "Adresse";
    public static final String KEY_RAYON = "Rayon";
    // 0 si la sortie n'a pas été validée, 1 si l'utilisateur l'a validée
    public static final String KEY_SORTIE_VALIDEE = "SortieValidee";

    // Sauvegarde des valeurs
    private String idSortie;
    private String nom;
    private String dateMili;
    private String idTransport;
    private String adresse;
    private String rayon;
    private String sortieValidee = "0";

    public static String dateToString(long dateMili) {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(dateMili);

        int jour = calendrier.get(Calendar.DAY_OF_MONTH);
        int mois = calendrier.get(Calendar.MONTH) + 1;
        int annee = calendrier.get(Calendar.YEAR);

        return dateToString(jour, mois, annee);
    }

    public static String dateToString(String dateMili) {
        return dateToString(Long.valueOf(dateMili));
    }

    public static String dateToString(int jour, int mois, int annee) {
        return jour + "/" + mois + "/" + annee;
    }

    public static String heureToString(long dateMili) {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(dateMili);

        int heure = calendrier.get(Calendar.HOUR_OF_DAY);
        int minutes = calendrier.get(Calendar.MINUTE);

        return heureToString(heure, minutes);
    }

    public static String heureToString(String dateMili) {
        return heureToString(Long.valueOf(dateMili));
    }

    public static String heureToString(int heure, int minutes) {
        String temp = "";

        if (heure < 10) {
            temp += "0";
        }
        temp += heure + ":";

        if (minutes < 10) {
            temp += "0";
        }
        temp += minutes;

        return temp;
    }

    public String getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return dateMili;
    }

    public void setDate(String dateMili) {
        this.dateMili = dateMili;
    }

    public void setDate(String date, String heure) {
        String[] tempDate = date.split("/");
        String[] tempHeure = heure.split(":");

        Calendar calendrier = Calendar.getInstance();
        calendrier.set(
                Integer.valueOf(tempDate[2]),
                Integer.valueOf(tempDate[1]) - 1,
                Integer.valueOf(tempDate[0]),
                Integer.valueOf(tempHeure[0]),
                Integer.valueOf(tempHeure[1]));

        this.dateMili = Long.toString(calendrier.getTimeInMillis());
    }

    public String getIdTransport() {
        return idTransport;
    }

    public void setIdTransport(String idTransport) {
        this.idTransport = idTransport;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRayon() {
        return rayon;
    }

    public void setRayon(String rayon) {
        this.rayon = rayon;
    }

    public String getSortieValidee() {
        return sortieValidee;
    }

    public void setSortieValidee(String sortieValidee) {
        if (sortieValidee == null) {
            this.sortieValidee = "0";
        } else if (sortieValidee.compareTo("1") == 0) {
            this.sortieValidee = "1";
        } else {
            this.sortieValidee = "0";
        }
    }

    public void valider() {
        this.sortieValidee = "1";
    }

    public void invalider() {
        this.sortieValidee = "0";
    }

    public int getDateHeure() {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(Long.valueOf(dateMili));

        return calendrier.get(Calendar.HOUR_OF_DAY);
    }

    public int getDateMinute() {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(Long.valueOf(dateMili));

        return calendrier.get(Calendar.MINUTE);
    }


    public int getDateAnnee() {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(Long.valueOf(dateMili));

        return calendrier.get(Calendar.YEAR);
    }

    public int getDateMois() {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(Long.valueOf(dateMili));

        return calendrier.get(Calendar.MONTH);
    }

    public int getDateJour() {
        Calendar calendrier = Calendar.getInstance();
        calendrier.setTimeInMillis(Long.valueOf(dateMili));

        return calendrier.get(Calendar.DAY_OF_MONTH);
    }

}
