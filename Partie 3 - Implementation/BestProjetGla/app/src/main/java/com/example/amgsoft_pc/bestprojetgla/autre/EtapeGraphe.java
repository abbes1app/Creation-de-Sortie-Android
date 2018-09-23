package com.example.amgsoft_pc.bestprojetgla.autre;

import java.util.ArrayList;

public class EtapeGraphe {
    public int nbEmbranchements;
    public EtapeGraphe suivant;
    public long duree = Long.MAX_VALUE;
    public String type;
    public ArrayList<String> contraintes;
    public ArrayList<String> nonContraintes;
    public String transportQuery;
    public int transportRayon; // en m

    @Override
    public String toString() {
        String temp = "\n++++++++++++++++++++++++++++++++++\n";

        temp += "Suivant ? : " + (suivant != null) + "\n";
        temp += "Durée : " + duree + "\n";

        temp += "Embranchements : " + nbEmbranchements + "\n";

        temp += "Contraintes : \n";
        for (String type : contraintes) {
            temp += "\t" + type + "\n";
        }

        temp += "Non Contraintes : \n";
        for (String type : nonContraintes) {
            temp += "\t" + type + "\n";
        }

        temp += "Type : " + type + "\n";
        temp += "Transport : " + transportQuery + "\n";
        temp += "Rayon de recherche : " + transportRayon + "\n";

        return temp;
    }
}
