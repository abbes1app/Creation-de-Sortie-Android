package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesPerso;

import java.util.ArrayList;

public class ManipulationContraintesPerso {

    public static String creer() {
        return "CREATE TABLE " + ContraintesPerso.TABLE + "("
                + ContraintesPerso.KEY_ID_CONTRAINTE_PERSO + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + ContraintesPerso.KEY_NOM + " TEXT,"
                + ContraintesPerso.KEY_AVIS + " INTEGER,"
                + ContraintesPerso.KEY_ID_SORTIE + " INTEGER,"
                + ContraintesPerso.KEY_ID_TYPE + " INTEGER )";
    }

    public static long inserer(SQLiteDatabase db, ContraintesPerso contrainte) {
        ContentValues values = new ContentValues();

        values.put(ContraintesPerso.KEY_NOM, contrainte.getNom());
        values.put(ContraintesPerso.KEY_AVIS, contrainte.getAvis());
        values.put(ContraintesPerso.KEY_ID_SORTIE, contrainte.getIdSortie());
        values.put(ContraintesPerso.KEY_ID_TYPE, contrainte.getIdType());

        return db.insertOrThrow(ContraintesPerso.TABLE, null, values);
    }

    public static ContraintesPerso getAvecID(SQLiteDatabase db, String ID) {
        ContraintesPerso contrainteTemp = null;

        String selectQuery = "SELECT * FROM " + ContraintesPerso.TABLE + " WHERE " + ContraintesPerso.KEY_ID_CONTRAINTE_PERSO + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            contrainteTemp = new ContraintesPerso();

            contrainteTemp.setIdContraintePerso(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_CONTRAINTE_PERSO)));
            contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_NOM)));
            contrainteTemp.setAvis(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_AVIS)));
            contrainteTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_SORTIE)));
            contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_TYPE)));
        }

        cursor.close();

        return contrainteTemp;
    }

    public static ArrayList<ContraintesPerso> liste(SQLiteDatabase db) {
        ArrayList<ContraintesPerso> listeContraintes = new ArrayList<ContraintesPerso>();

        String selectQuery = "SELECT * FROM " + ContraintesPerso.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContraintesPerso contrainteTemp = new ContraintesPerso();

                contrainteTemp.setIdContraintePerso(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_CONTRAINTE_PERSO)));
                contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_NOM)));
                contrainteTemp.setAvis(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_AVIS)));
                contrainteTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_SORTIE)));
                contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_TYPE)));

                listeContraintes.add(contrainteTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static ArrayList<ContraintesPerso> liste(SQLiteDatabase db, String IDSortie) {
        ArrayList<ContraintesPerso> listeContraintes = new ArrayList<ContraintesPerso>(0);

        String selectQuery = "SELECT * FROM " + ContraintesPerso.TABLE  +
                " WHERE " + ContraintesPerso.KEY_ID_SORTIE + " = " + IDSortie;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContraintesPerso contrainteTemp = new ContraintesPerso();

                contrainteTemp.setIdContraintePerso(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_CONTRAINTE_PERSO)));
                contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_NOM)));
                contrainteTemp.setAvis(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_AVIS)));
                contrainteTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_SORTIE)));
                contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesPerso.KEY_ID_TYPE)));

                listeContraintes.add(contrainteTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static ArrayList<ContraintesPerso> liste(SQLiteDatabase db, String IDSortie, String IDType) {
        ArrayList<ContraintesPerso> listeContraintes = liste(db, IDSortie);

        for(int i=0;i<listeContraintes.size();i++){
            if(listeContraintes.get(i).getIdType().compareTo(IDType) != 0){
                listeContraintes.remove(i);
                i--;
            }
        }

        return listeContraintes;
    }

    public static void remove(SQLiteDatabase db, String IDSortie) {
        db.execSQL("DELETE FROM " + ContraintesPerso.TABLE + " WHERE " +
                ContraintesPerso.KEY_ID_SORTIE + " = " + IDSortie);
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + ContraintesPerso.TABLE;
    }
}
