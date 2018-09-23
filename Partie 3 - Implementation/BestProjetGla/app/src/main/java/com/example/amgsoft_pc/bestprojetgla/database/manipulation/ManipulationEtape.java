package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;

import java.util.ArrayList;

public class ManipulationEtape {

    public static String creer() {
        return "CREATE TABLE " + Etape.TABLE + "("
                + Etape.KEY_ID_SORTIE + " INTEGER,"
                + Etape.KEY_POSITION + " INTEGER,"
                + Etape.KEY_ATTENTE_AVANT + " INTEGER,"
                + Etape.KEY_DEBUT + " INTEGER,"
                + Etape.KEY_FIN + " INTEGER,"
                + Etape.KEY_ID_TYPE + " INTEGER,"
                + Etape.KEY_ID_ADRESSE + " INTEGER,"
                + " FOREIGN KEY( " + Etape.KEY_ID_TYPE + " ) REFERENCES " + TypeEtape.TABLE + " ( " + TypeEtape.KEY_ID_TYPE + " ),"
                + " FOREIGN KEY( " + Etape.KEY_ID_ADRESSE + " ) REFERENCES " + Adresse.TABLE + " ( " + Adresse.KEY_ID_ADRESSE + " ) )";
    }

    public static long inserer(SQLiteDatabase db, Etape etape) {
        ContentValues values = new ContentValues();

        values.put(Etape.KEY_ID_SORTIE, etape.getIdSortie());
        values.put(Etape.KEY_POSITION, etape.getPosition());
        values.put(Etape.KEY_ATTENTE_AVANT, etape.getAttenteAvant());
        values.put(Etape.KEY_DEBUT, etape.getDebut());
        values.put(Etape.KEY_FIN, etape.getFin());
        values.put(Etape.KEY_ID_TYPE, etape.getIdType());
        values.put(Etape.KEY_ID_ADRESSE, etape.getIdAdresse());

        return db.insertOrThrow(Etape.TABLE, null, values);
    }

    public static Etape getAvecID(SQLiteDatabase db, String IDSortie, String position) {
        Etape etapeTemp = null;

        String selectQuery = "SELECT * FROM " + Etape.TABLE +
                " WHERE " + Etape.KEY_ID_SORTIE + "=" + IDSortie +
                " AND " + Etape.KEY_POSITION + "=" + position;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            etapeTemp = new Etape();

            etapeTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_SORTIE)));
            etapeTemp.setPosition(cursor.getString(cursor.getColumnIndex(Etape.KEY_POSITION)));
            etapeTemp.setAttenteAvant(cursor.getString(cursor.getColumnIndex(Etape.KEY_ATTENTE_AVANT)));
            etapeTemp.setDebut(cursor.getString(cursor.getColumnIndex(Etape.KEY_DEBUT)));
            etapeTemp.setFin(cursor.getString(cursor.getColumnIndex(Etape.KEY_FIN)));
            etapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_TYPE)));
            etapeTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_ADRESSE)));
        }

        cursor.close();

        return etapeTemp;
    }

    public static ArrayList<Etape> liste(SQLiteDatabase db, String IDSortie) {
        ArrayList<Etape> listeEtapes = new ArrayList<Etape>(0);

        String selectQuery = "SELECT * FROM " + Etape.TABLE +
                " WHERE " + Etape.KEY_ID_SORTIE + "=" + IDSortie +
                " ORDER BY " + Etape.KEY_POSITION;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Etape etapeTemp = new Etape();

                etapeTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_SORTIE)));
                etapeTemp.setPosition(cursor.getString(cursor.getColumnIndex(Etape.KEY_POSITION)));
                etapeTemp.setAttenteAvant(cursor.getString(cursor.getColumnIndex(Etape.KEY_ATTENTE_AVANT)));
                etapeTemp.setDebut(cursor.getString(cursor.getColumnIndex(Etape.KEY_DEBUT)));
                etapeTemp.setFin(cursor.getString(cursor.getColumnIndex(Etape.KEY_FIN)));
                etapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_TYPE)));
                etapeTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_ADRESSE)));

                listeEtapes.add(etapeTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeEtapes;
    }

    public static ArrayList<Etape> liste(SQLiteDatabase db) {
        ArrayList<Etape> listeEtapes = new ArrayList<Etape>(0);

        String selectQuery = "SELECT * FROM " + Etape.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Etape etapeTemp = new Etape();

                etapeTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_SORTIE)));
                etapeTemp.setPosition(cursor.getString(cursor.getColumnIndex(Etape.KEY_POSITION)));
                etapeTemp.setAttenteAvant(cursor.getString(cursor.getColumnIndex(Etape.KEY_ATTENTE_AVANT)));
                etapeTemp.setDebut(cursor.getString(cursor.getColumnIndex(Etape.KEY_DEBUT)));
                etapeTemp.setFin(cursor.getString(cursor.getColumnIndex(Etape.KEY_FIN)));
                etapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_TYPE)));
                etapeTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Etape.KEY_ID_ADRESSE)));

                listeEtapes.add(etapeTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeEtapes;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + Etape.TABLE;
    }
}
