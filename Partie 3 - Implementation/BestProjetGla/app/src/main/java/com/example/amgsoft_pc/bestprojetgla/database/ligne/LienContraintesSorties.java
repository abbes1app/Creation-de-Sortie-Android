package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class LienContraintesSorties {
    // Identification de la classe
    public static final String TAG = LienContraintesSorties.class.getSimpleName();
    public static final String TABLE = "LienContraintesSorties";

    // Nom des colonnes
    public static final String KEY_ID_SORTIE = "IdSortie";
    public static final String KEY_AVIS = "Avis"; // 0 = rien  ;  1 = aime  ;  2 = aime pas
    public static final String KEY_ID_CONTRAINTE_BASE = "IdContrainte";

    // Sauvegarde des valeurs
    private String idSortie;
    private String avis;
    private String idContrainteBase;

    public String getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public String getIdContrainteBase() {
        return idContrainteBase;
    }

    public void setIdContrainteBase(String idContrainte) {
        this.idContrainteBase = idContrainte;
    }
}
