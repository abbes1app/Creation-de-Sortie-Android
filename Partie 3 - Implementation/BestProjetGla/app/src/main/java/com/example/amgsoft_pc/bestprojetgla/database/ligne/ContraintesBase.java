package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class ContraintesBase {
    // Identification de la classe
    public static final String TAG = ContraintesBase.class.getSimpleName();
    public static final String TABLE = "ContraintesBase";

    // Nom des colonnes
    public static final String KEY_ID_CONTRAINTE_BASE = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_ID_TYPE = "IDType";

    // Sauvegarde des valeurs
    private String idContrainteBase;
    private String nom;
    private String image;
    private String idType;

    public String getIdContrainteBase() {
        return idContrainteBase;
    }

    public void setIdContrainteBase(String idContrainteBase) {
        this.idContrainteBase = idContrainteBase;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}
