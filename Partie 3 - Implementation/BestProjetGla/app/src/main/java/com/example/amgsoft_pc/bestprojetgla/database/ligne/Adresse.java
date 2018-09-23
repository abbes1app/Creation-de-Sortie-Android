package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class Adresse {
    // Identification de la classe
    public static final String TAG = Adresse.class.getSimpleName();
    public static final String TABLE = "Adresse";

    // Nom des colonnes
    public static final String KEY_ID_ADRESSE = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_ADRESSE = "Adresse";
    public static final String KEY_TELEPHONE = "Telephone";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";

    // Sauvegarde des valeurs
    private String idAdresse;
    private String nom;
    private String adresse;
    private String telephone;
    private String latitude;
    private String longitude;

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
