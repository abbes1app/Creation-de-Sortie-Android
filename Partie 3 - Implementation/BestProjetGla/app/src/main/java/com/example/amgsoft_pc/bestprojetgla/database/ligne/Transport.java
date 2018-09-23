package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class Transport {
    // Identification de la classe
    public static final String TAG = Transport.class.getSimpleName();
    public static final String TABLE = "Transports";

    // Nom des colonnes
    public static final String KEY_ID_TRANSPORT = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_ICON = "Icon";
    public static final String KEY_ICON_GRIS = "IconGris";
    public static final String KEY_QUERY = "Query";
    public static final String KEY_RAYON = "Rayon";

    // Sauvegarde des valeurs
    private String idTransport;
    private String nom;
    private String icon;
    private String iconGris;
    private String query;
    private String rayon;

    public String getIdTransport() {
        return idTransport;
    }

    public void setIdTransport(String idTransport) {
        this.idTransport = idTransport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconGris() {
        return iconGris;
    }

    public void setIconGris(String iconGris) {
        this.iconGris = iconGris;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRayon() {
        return rayon;
    }

    public void setRayon(String rayon) {
        this.rayon = rayon;
    }
}
