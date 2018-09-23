package com.example.amgsoft_pc.bestprojetgla.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienContraintesSorties;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienParticipants;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;

import java.util.ArrayList;
import java.util.Collection;


public class ManipulationDatabase {

    public static Etape prochaineEtape(SQLiteDatabase db, String date) {
        Etape etapeTemp = null;

        String selectQuery = "SELECT * FROM " + Etape.TABLE +
                " WHERE " + Etape.KEY_FIN + ">" + date +
                " ORDER BY " + Etape.KEY_DEBUT;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            etapeTemp = new Etape();

            etapeTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_SORTIE)));
            etapeTemp.setPosition(cursor.getString(cursor.getColumnIndex(Etape.KEY_POSITION)));
            etapeTemp.setDebut(cursor.getString(cursor.getColumnIndex(Etape.KEY_DEBUT)));
            etapeTemp.setFin(cursor.getString(cursor.getColumnIndex(Etape.KEY_FIN)));
            etapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_TYPE)));
            etapeTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_ADRESSE)));
        }

        cursor.close();

        return etapeTemp;
    }

    public static ArrayList<Adresse> listeAdressesEtapes(SQLiteDatabase db, String IDSortie) {
        ArrayList<Adresse> listeAdresse = new ArrayList<Adresse>(0);

        String selectQuery = "SELECT * FROM " + Etape.TABLE + ", " + Adresse.TABLE +
                " WHERE " + Etape.KEY_ID_SORTIE + " = " + IDSortie +
                " AND " + Etape.KEY_ID_ADRESSE + " = " + Adresse.KEY_ID_ADRESSE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Adresse adresseTemp = new Adresse();

                adresseTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ID_ADRESSE)));
                adresseTemp.setNom(cursor.getString(cursor.getColumnIndex(Adresse.KEY_NOM)));
                adresseTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ADRESSE)));
                adresseTemp.setTelephone(cursor.getString(cursor.getColumnIndex(Adresse.KEY_TELEPHONE)));
                adresseTemp.setLatitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LATITUDE)));
                adresseTemp.setLongitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LONGITUDE)));

                listeAdresse.add(adresseTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeAdresse;
    }

    public static ArrayList<Adresse> listeAdressesParticipantsSortie(SQLiteDatabase db, String IDSortie) {
        ArrayList<Adresse> listeAdresse = new ArrayList<Adresse>(0);

        String a = Adresse.TABLE + ".";
        String s = ", ";
        String sa = s + a;

        String selectQuery = "SELECT " +a+Adresse.KEY_ID_ADRESSE+sa+Adresse.KEY_NOM+sa+
                Adresse.KEY_ADRESSE+sa+Adresse.KEY_TELEPHONE+sa+Adresse.KEY_LATITUDE+sa+Adresse.KEY_LONGITUDE+
                " FROM " + LienParticipants.TABLE + ", " + Ami.TABLE + ", " + Adresse.TABLE +
                " WHERE " + LienParticipants.TABLE + "." + LienParticipants.KEY_ID_SORTIE + " = " + IDSortie +
                " AND " + LienParticipants.TABLE + "." + LienParticipants.KEY_ID_AMI + " = " + Ami.TABLE + "." + Ami.KEY_ID_AMI +
                " AND " + Ami.TABLE + "." + Ami.KEY_ID_ADRESSE + " = " + a+Adresse.KEY_ID_ADRESSE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Adresse adresseTemp = new Adresse();

                adresseTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ID_ADRESSE)));
                adresseTemp.setNom(cursor.getString(cursor.getColumnIndex(Adresse.KEY_NOM)));
                adresseTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ADRESSE)));
                adresseTemp.setTelephone(cursor.getString(cursor.getColumnIndex(Adresse.KEY_TELEPHONE)));
                adresseTemp.setLatitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LATITUDE)));
                adresseTemp.setLongitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LONGITUDE)));

                listeAdresse.add(adresseTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeAdresse;
    }

    public static ArrayList<Ami> listeParticipantsSortie(SQLiteDatabase db, String IDSortie) {
        ArrayList<Ami> listeAmis = new ArrayList<Ami>(0);

        String selectQuery = "SELECT * FROM " + LienParticipants.TABLE + ", " + Ami.TABLE +
                " WHERE " + LienParticipants.KEY_ID_SORTIE + " = " + IDSortie +
                " AND " + LienParticipants.KEY_ID_AMI + " = " + Ami.KEY_ID_AMI;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ami amiTemp = new Ami();

                amiTemp.setIdAmi(cursor.getString(cursor.getColumnIndex(Ami.KEY_ID_AMI)));
                amiTemp.setNom(cursor.getString(cursor.getColumnIndex(Ami.KEY_NOM)));
                amiTemp.setPrenom(cursor.getString(cursor.getColumnIndex(Ami.KEY_PRENOM)));
                amiTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Ami.KEY_ID_ADRESSE)));
                amiTemp.setTelephone(cursor.getString(cursor.getColumnIndex(Ami.KEY_TELEPHONE)));

                listeAmis.add(amiTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeAmis;
    }


    public static ArrayList<ContraintesPerso> listeContrainteSortie(SQLiteDatabase db, String IDSortie, String IDType) {
        ArrayList<ContraintesPerso> listeContraintes = ManipulationContraintesBase.listeAvecAvis(db, IDSortie, IDType);

        listeContraintes.addAll(ManipulationContraintesPerso.liste(db, IDSortie, IDType));

        return listeContraintes;
    }


    public static void supprimerSortiesNonValide(SQLiteDatabase db) {
        for (String IDSortie : ManipulationSortie.listeIDNonValidee(db)) {
            supprimerSortieComplette(db, IDSortie);
        }
    }

    public static void supprimerLienParticipants(SQLiteDatabase db, String IDSortie) {
        db.delete(LienParticipants.TABLE, LienParticipants.KEY_ID_SORTIE + "=" + IDSortie, null);
    }

    public static void supprimerAmiPropre(SQLiteDatabase db, String IDAmi) {
        db.delete(LienParticipants.TABLE, LienParticipants.KEY_ID_AMI + "=" + IDAmi, null);
        db.delete(Ami.TABLE, Ami.KEY_ID_AMI + "=" + IDAmi, null);
    }

    public static void supprimerSortieComplette(SQLiteDatabase db, String IDSortie) {
        db.delete(ContraintesPerso.TABLE, ContraintesPerso.KEY_ID_SORTIE + "=" + IDSortie, null);
        db.delete(LienContraintesSorties.TABLE, LienContraintesSorties.KEY_ID_SORTIE + "=" + IDSortie, null);

        supprimerEtapesSortie(db, IDSortie);
        supprimerLienParticipants(db, IDSortie);
        supprimerSortie(db, IDSortie);
    }

    private static void supprimerSortie(SQLiteDatabase db, String IDSortie) {
        db.delete(Sortie.TABLE, Sortie.KEY_ID_SORTIE + "=" + IDSortie, null);
    }

    public static void supprimerEtapesSortie(SQLiteDatabase db, String IDSortie) {
        db.delete(Etape.TABLE, Etape.KEY_ID_SORTIE + "=" + IDSortie, null);
    }

    public static void ajouterEtapes(SQLiteDatabase db, Collection<Etape> Etapes) {
        for (Etape etape : Etapes) {
            ManipulationEtape.inserer(db, etape);
        }
    }

}
