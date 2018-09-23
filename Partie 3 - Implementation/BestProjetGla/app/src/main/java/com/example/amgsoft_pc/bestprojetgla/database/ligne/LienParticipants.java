package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class LienParticipants {
    // Identification de la classe
    public static final String TAG = LienParticipants.class.getSimpleName();
    public static final String TABLE = "LienParticipants";

    // Nom des colonnes
    public static final String KEY_ID_SORTIE = "IdSortie";
    public static final String KEY_ID_AMI = "IdAmi";

    // Sauvegarde des valeurs
    private String idSortie;
    private String idAmi;

    public String getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public String getIdAmi() {
        return idAmi;
    }

    public void setIdAmi(String idAmi) {
        this.idAmi = idAmi;
    }
}
