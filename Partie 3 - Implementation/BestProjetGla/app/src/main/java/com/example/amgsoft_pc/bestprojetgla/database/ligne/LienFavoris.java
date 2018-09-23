package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class LienFavoris {
    // Identification de la classe
    public static final String TAG = LienFavoris.class.getSimpleName();
    public static final String TABLE = "LienFavoris";

    // Nom des colonnes
    public static final String KEY_ID_TYPE = "idType";
    public static final String KEY_ID_ADRESSE = "IdAdresse";

    // Sauvegarde des valeurs
    private String idType;
    private String idAdresse;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }
}
