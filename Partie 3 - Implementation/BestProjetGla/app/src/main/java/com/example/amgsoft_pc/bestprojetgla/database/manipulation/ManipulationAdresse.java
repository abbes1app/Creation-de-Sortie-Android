package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;

import java.util.ArrayList;

public class ManipulationAdresse {

    public static String creer() {
        return "CREATE TABLE " + Adresse.TABLE + "("
                + Adresse.KEY_ID_ADRESSE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Adresse.KEY_NOM + " TEXT,"
                + Adresse.KEY_ADRESSE + " TEXT,"
                + Adresse.KEY_TELEPHONE + " TEXT,"
                + Adresse.KEY_LATITUDE + " REAL,"
                + Adresse.KEY_LONGITUDE + " REAL)";
    }

    public static long inserer(SQLiteDatabase db, Adresse adresse) {
        ContentValues values = new ContentValues();

        values.put(Adresse.KEY_NOM, adresse.getNom());
        values.put(Adresse.KEY_ADRESSE, adresse.getAdresse());
        values.put(Adresse.KEY_TELEPHONE, adresse.getTelephone());
        values.put(Adresse.KEY_LATITUDE, adresse.getLatitude());
        values.put(Adresse.KEY_LONGITUDE, adresse.getLongitude());

        return db.insertOrThrow(Adresse.TABLE, null, values);
    }

    public static long update(SQLiteDatabase db, Adresse adresse) {
        ContentValues values = new ContentValues();

        values.put(Adresse.KEY_NOM, adresse.getNom());
        values.put(Adresse.KEY_ADRESSE, adresse.getAdresse());
        values.put(Adresse.KEY_TELEPHONE, adresse.getTelephone());
        values.put(Adresse.KEY_LATITUDE, adresse.getLatitude());
        values.put(Adresse.KEY_LONGITUDE, adresse.getLongitude());

        return db.update(Adresse.TABLE, values, Adresse.KEY_ID_ADRESSE + "=" + adresse.getIdAdresse(), null);
    }

    public static Adresse getAvecID(SQLiteDatabase db, String ID) {
        Adresse adresseTemp = null;

        String selectQuery = "SELECT * FROM " + Adresse.TABLE + " WHERE " + Adresse.KEY_ID_ADRESSE + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            adresseTemp = new Adresse();

            adresseTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ID_ADRESSE)));
            adresseTemp.setNom(cursor.getString(cursor.getColumnIndex(Adresse.KEY_NOM)));
            adresseTemp.setAdresse(cursor.getString(cursor.getColumnIndex(Adresse.KEY_ADRESSE)));
            adresseTemp.setTelephone(cursor.getString(cursor.getColumnIndex(Adresse.KEY_TELEPHONE)));
            adresseTemp.setLatitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LATITUDE)));
            adresseTemp.setLongitude(cursor.getString(cursor.getColumnIndex(Adresse.KEY_LONGITUDE)));
        }

        cursor.close();

        return adresseTemp;
    }

    public static ArrayList<Adresse> liste(SQLiteDatabase db) {
        ArrayList<Adresse> listeAdresses = new ArrayList<Adresse>();

        String selectQuery = "SELECT * FROM " + Adresse.TABLE;

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

                listeAdresses.add(adresseTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeAdresses;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + Adresse.TABLE;
    }
}
