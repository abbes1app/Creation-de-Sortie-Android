package com.example.amgsoft_pc.bestprojetgla.database.ligne;

public class Ami {
    // Identification de la classe
    public static final String TAG = Ami.class.getSimpleName();
    public static final String TABLE = "Ami";

    // Nom des colonnes
    public static final String KEY_ID_AMI = "_ID";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_PRENOM = "Prenom";
    public static final String KEY_ID_ADRESSE = "IdAdresse";
    public static final String KEY_TELEPHONE = "Telephone";

    // Sauvegarde des valeurs
    private String idAmi;
    private String nom;
    private String prenom;
    private String idAdresse;
    private String telephone;

    public String getIdAmi() {
        return idAmi;
    }

    public void setIdAmi(String idAmi) {
        this.idAmi = idAmi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null || telephone.compareTo("") == 0) {
            telephone = null;
        }
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "{" + idAmi + ";" + nom + ";" + prenom + ";" + idAdresse + ";" + telephone + "}";
    }
}
