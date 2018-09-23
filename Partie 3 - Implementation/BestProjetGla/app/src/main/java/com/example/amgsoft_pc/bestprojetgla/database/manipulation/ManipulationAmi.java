package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;

import java.util.ArrayList;

public class ManipulationAmi {

    public static String creer() {
        return "CREATE TABLE " + Ami.TABLE + "("
                + Ami.KEY_ID_AMI + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Ami.KEY_NOM + " TEXT,"
                + Ami.KEY_PRENOM + " TEXT,"
                + Ami.KEY_ID_ADRESSE + " INTEGER,"
                + Ami.KEY_TELEPHONE + " TEXT,"
                + " FOREIGN KEY( " + Ami.KEY_ID_ADRESSE + " ) REFERENCES " + Adresse.TABLE + " ( " + Adresse.KEY_ID_ADRESSE + " ),"
                + " CONSTRAINT uniqueNP UNIQUE ( " + Ami.KEY_NOM + " , " + Ami.KEY_PRENOM + " ),"
                + " CONSTRAINT uniqueNumTel UNIQUE ( " + Ami.KEY_TELEPHONE + " ) )";
    }

    public static long inserer(SQLiteDatabase db, Ami ami) {
        ContentValues values = new ContentValues();

        values.put(Ami.KEY_NOM, ami.getNom());
        values.put(Ami.KEY_PRENOM, ami.getPrenom());
        values.put(Ami.KEY_ID_ADRESSE, ami.getIdAdresse());
        values.put(Ami.KEY_TELEPHONE, ami.getTelephone());

        return db.insertOrThrow(Ami.TABLE, null, values);
    }

    public static long update(SQLiteDatabase db, Ami ami) {
        ContentValues values = new ContentValues();

        values.put(Ami.KEY_NOM, ami.getNom());
        values.put(Ami.KEY_PRENOM, ami.getPrenom());
        values.put(Ami.KEY_ID_ADRESSE, ami.getIdAdresse());
        values.put(Ami.KEY_TELEPHONE, ami.getTelephone());

        return db.update(Ami.TABLE, values, Ami.KEY_ID_AMI + "=" + ami.getIdAmi(), null);
    }

    public static Ami getAvecID(SQLiteDatabase db, String ID) {
        Ami amiTemp = null;

        String selectQuery = "SELECT * FROM " + Ami.TABLE + " WHERE " + Ami.KEY_ID_AMI + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            amiTemp = new Ami();

            amiTemp.setIdAmi(cursor.getString(cursor.getColumnIndex(Ami.KEY_ID_AMI)));
            amiTemp.setNom(cursor.getString(cursor.getColumnIndex(Ami.KEY_NOM)));
            amiTemp.setPrenom(cursor.getString(cursor.getColumnIndex(Ami.KEY_PRENOM)));
            amiTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(Ami.KEY_ID_ADRESSE)));
            amiTemp.setTelephone(cursor.getString(cursor.getColumnIndex(Ami.KEY_TELEPHONE)));
        }

        cursor.close();

        return amiTemp;
    }

    public static ArrayList<Ami> liste(SQLiteDatabase db) {
        ArrayList<Ami> listeAmis = new ArrayList<Ami>();

        String selectQuery = "SELECT * FROM " + Ami.TABLE;

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

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + Ami.TABLE;
    }
}
