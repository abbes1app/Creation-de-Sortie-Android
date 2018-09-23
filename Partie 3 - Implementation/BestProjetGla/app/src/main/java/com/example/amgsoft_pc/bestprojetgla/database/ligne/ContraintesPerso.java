package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class ContraintesPerso {
    // Identification de la classe
    public static final String TAG = ContraintesPerso.class.getSimpleName();
    public static final String TABLE = "ContraintesPerso";

    // Nom des colonnes
    public static final String KEY_ID_CONTRAINTE_PERSO = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_AVIS = "Avis"; // 0 = rien  ;  1 = aime  ;  2 = aime pas
    public static final String KEY_ID_SORTIE = "IDSortie";
    public static final String KEY_ID_TYPE = "IDType";

    // Sauvegarde des valeurs
    private String idContraintePerso;
    private String nom;
    private String avis;
    private String idSortie;
    private String idType;

    public String getIdContraintePerso() {
        return idContraintePerso;
    }

    public void setIdContraintePerso(String idContraintePerso) {
        this.idContraintePerso = idContraintePerso;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public String getIdSortie() {
        return idSortie;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}
