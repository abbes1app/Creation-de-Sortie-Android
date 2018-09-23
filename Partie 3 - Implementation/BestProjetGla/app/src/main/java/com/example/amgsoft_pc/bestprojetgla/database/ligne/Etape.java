package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class Etape {
    // Identification de la classe
    public static final String TAG = Etape.class.getSimpleName();
    public static final String TABLE = "Etape";

    // Nom des colonnes
    public static final String KEY_ID_SORTIE = "IdSortie";
    public static final String KEY_POSITION = "Position";
    public static final String KEY_ATTENTE_AVANT = "Attente";
    public static final String KEY_DEBUT = "Debut";
    public static final String KEY_FIN = "Fin";
    public static final String KEY_ID_TYPE = "IdType";
    public static final String KEY_ID_ADRESSE = "IdAdresse";

    // Sauvegarde des valeurs
    private String idSortie;
    private String position;
    private String attenteAvant;
    private String debut;
    private String fin;
    private String idType;
    private String idAdresse;

    public String getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAttenteAvant() {
        return attenteAvant;
    }

    public void setAttenteAvant(String attenteAvant) {
        this.attenteAvant = attenteAvant;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

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
