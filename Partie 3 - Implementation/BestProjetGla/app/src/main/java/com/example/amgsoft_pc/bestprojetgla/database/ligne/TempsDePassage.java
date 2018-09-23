package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class TempsDePassage {
    // Identification de la classe
    public static final String TAG = TempsDePassage.class.getSimpleName();
    public static final String TABLE = "TempsDePassage";

    // Nom des colonnes
    public static final String KEY_ID_TYPE = "IdType";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_DUREE = "Duree";

    // Sauvegarde des valeurs
    private String idType;
    private String nom;
    private String duree;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
