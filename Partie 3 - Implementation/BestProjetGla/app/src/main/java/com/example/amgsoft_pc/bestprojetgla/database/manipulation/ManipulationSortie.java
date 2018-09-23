package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;

import java.util.ArrayList;

public class ManipulationSortie {

    public static String creer() {
        return "CREATE TABLE " + Sortie.TABLE + "("
                + Sortie.KEY_ID_SORTIE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Sortie.KEY_NOM + " TEXT,"
                + Sortie.KEY_DATE + " INTEGER,"
                + Sortie.KEY_ID_TRANSPORT + " INTEGER,"
                + Sortie.KEY_ADRESSE + " TEXT,"
                + Sortie.KEY_RAYON + " INTEGER,"
                + Sortie.KEY_SORTIE_VALIDEE + " INTEGER DEFAULT 0 NOT NULL,"
                + " CHECK ( " + Sortie.KEY_SORTIE_VALIDEE + " == 0 || " + Sortie.KEY_SORTIE_VALIDEE + " == 1 ),"
                + " FOREIGN KEY( " + Sortie.KEY_ID_TRANSPORT + " ) REFERENCES " + Transport.TABLE + " ( " + Transport.KEY_ID_TRANSPORT + " ) )";
    }

    public static long inserer(SQLiteDatabase db, Sortie sortie) {
        ContentValues values = new ContentValues();

        values.put(Sortie.KEY_NOM, sortie.getNom());
        values.put(Sortie.KEY_DATE, sortie.getDate());
        values.put(Sortie.KEY_ID_TRANSPORT, sortie.getIdTransport());
        values.put(Sortie.KEY_ADRESSE, sortie.getAdresse());
        values.put(Sortie.KEY_RAYON, sortie.getRayon());
        values.put(Sortie.KEY_SORTIE_VALIDEE, sortie.getSortieValidee());

        return db.insertOrThrow(Sortie.TABLE, null, values);
    }

    public static void update(SQLiteDatabase db, Sortie sortie) {
        ContentValues values = new ContentValues();

        values.put(Sortie.KEY_NOM, sortie.getNom());
        values.put(Sortie.KEY_DATE, sortie.getDate());
        values.put(Sortie.KEY_ID_TRANSPORT, sortie.getIdTransport());
        values.put(Sortie.KEY_ADRESSE, sortie.getAdresse());
        values.put(Sortie.KEY_RAYON, sortie.getRayon());
        values.put(Sortie.KEY_SORTIE_VALIDEE, sortie.getSortieValidee());

        db.update(Sortie.TABLE, values, Sortie.KEY_ID_SORTIE + "=" + sortie.getIdSortie(), null);
    }

    public static Sortie getAvecID(SQLiteDatabase db, String ID) {
        Sortie sortieTemp = null;

        String selectQuery = "SELECT * FROM " + Sortie.TABLE + " WHERE " + Sortie.KEY_ID_SORTIE + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            sortieTemp = new Sortie();

            sortieTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_SORTIE)));
            sortieTemp.setNom(cursor.getString(cursor.getColumnIndex(Sortie.KEY_NOM)));
            sortieTemp.setDate(cursor.getString(cursor.getColumnIndex(Sortie.KEY_DATE)));
            sortieTemp.setIdTransport(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_TRANSPORT)));
            sortieTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ADRESSE)));
            sortieTemp.setRayon(cursor.getString(cursor.getColumnIndex(Sortie.KEY_RAYON)));
            sortieTemp.setSortieValidee(cursor.getString(cursor.getColumnIndex(Sortie.KEY_SORTIE_VALIDEE)));
        }

        cursor.close();

        return sortieTemp;
    }

    public static boolean nameExist(SQLiteDatabase db, String nom) {
        String selectQuery = "SELECT * FROM " + Sortie.TABLE +
                " WHERE " + Sortie.KEY_NOM + "= '" + nom + "' " +
                " AND " + Sortie.KEY_SORTIE_VALIDEE + " = 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    public static ArrayList<Sortie> listeToutes(SQLiteDatabase db) {
        ArrayList<Sortie> listeContraintes = new ArrayList<Sortie>();

        String selectQuery = "SELECT * FROM " + Sortie.TABLE +
                " ORDER BY " + Sortie.KEY_DATE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sortie sortieTemp = new Sortie();

                sortieTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_SORTIE)));
                sortieTemp.setNom(cursor.getString(cursor.getColumnIndex(Sortie.KEY_NOM)));
                sortieTemp.setDate(cursor.getString(cursor.getColumnIndex(Sortie.KEY_DATE)));
                sortieTemp.setIdTransport(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_TRANSPORT)));
                sortieTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ADRESSE)));
                sortieTemp.setRayon(cursor.getString(cursor.getColumnIndex(Sortie.KEY_RAYON)));
                sortieTemp.setSortieValidee(cursor.getString(cursor.getColumnIndex(Sortie.KEY_SORTIE_VALIDEE)));

                listeContraintes.add(sortieTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static ArrayList<Sortie> liste(SQLiteDatabase db) {
        ArrayList<Sortie> listeContraintes = new ArrayList<Sortie>();

        String selectQuery = "SELECT * FROM " + Sortie.TABLE +
                " WHERE " + Sortie.KEY_SORTIE_VALIDEE + " = 1 " +
                " ORDER BY " + Sortie.KEY_DATE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sortie sortieTemp = new Sortie();

                sortieTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_SORTIE)));
                sortieTemp.setNom(cursor.getString(cursor.getColumnIndex(Sortie.KEY_NOM)));
                sortieTemp.setDate(cursor.getString(cursor.getColumnIndex(Sortie.KEY_DATE)));
                sortieTemp.setIdTransport(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_TRANSPORT)));
                sortieTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ADRESSE)));
                sortieTemp.setRayon(cursor.getString(cursor.getColumnIndex(Sortie.KEY_RAYON)));
                sortieTemp.setSortieValidee(cursor.getString(cursor.getColumnIndex(Sortie.KEY_SORTIE_VALIDEE)));

                listeContraintes.add(sortieTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static ArrayList<String> listeIDNonValidee(SQLiteDatabase db) {
        ArrayList<String> listeContraintes = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + Sortie.TABLE +
                " WHERE " + Sortie.KEY_SORTIE_VALIDEE + " <> 1 ";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                listeContraintes.add(cursor.getString(cursor.getColumnIndex(Sortie.KEY_ID_SORTIE)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + Sortie.TABLE;
    }
}
