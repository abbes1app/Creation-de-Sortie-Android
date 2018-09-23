package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class TypeEtape {
    // Identification de la classe
    public static final String TAG = TypeEtape.class.getSimpleName();
    public static final String TABLE = "TypeEtape";

    // Nom des colonnes
    public static final String KEY_ID_TYPE = "_ID";
    public static final String KEY_ARTICLE = "Article";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_DUREE = "Duree";

    // Sauvegarde des valeurs
    private String idType;
    private String article;
    private String nom;
    private String type;
    private String image;
    private String duree;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
