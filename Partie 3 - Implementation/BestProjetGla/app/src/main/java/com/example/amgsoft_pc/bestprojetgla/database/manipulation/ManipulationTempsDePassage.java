package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.TempsDePassage;

import java.util.ArrayList;

public class ManipulationTempsDePassage {

    public static String creer() {
        return "CREATE TABLE " + TempsDePassage.TABLE + "("
                + TempsDePassage.KEY_ID_TYPE + " INTEGER,"
                + TempsDePassage.KEY_NOM + " TEXT,"
                + TempsDePassage.KEY_DUREE + " INTEGER)";
    }

    public static long inserer(SQLiteDatabase db, TempsDePassage tempsDePassage) {
        ContentValues values = new ContentValues();

        values.put(TempsDePassage.KEY_ID_TYPE, tempsDePassage.getIdType());
        values.put(TempsDePassage.KEY_NOM, tempsDePassage.getNom());
        values.put(TempsDePassage.KEY_DUREE, tempsDePassage.getDuree());

        return db.insertOrThrow(TempsDePassage.TABLE, null, values);
    }

    public static TempsDePassage getAvecID(SQLiteDatabase db, String ID) {
        TempsDePassage tempsDePassageTemp = null;

        String selectQuery = "SELECT * FROM " + TempsDePassage.TABLE + " WHERE " + TempsDePassage.KEY_ID_TYPE + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            tempsDePassageTemp = new TempsDePassage();

            tempsDePassageTemp.setIdType(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_ID_TYPE)));
            tempsDePassageTemp.setNom(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_NOM)));
            tempsDePassageTemp.setDuree(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_DUREE)));
        }

        cursor.close();

        return tempsDePassageTemp;
    }

    public static ArrayList<TempsDePassage> liste(SQLiteDatabase db) {
        ArrayList<TempsDePassage> listeContraintes = new ArrayList<TempsDePassage>();

        String selectQuery = "SELECT * FROM " + TempsDePassage.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempsDePassage tempsDePassageTemp = new TempsDePassage();

                tempsDePassageTemp.setIdType(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_ID_TYPE)));
                tempsDePassageTemp.setNom(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_NOM)));
                tempsDePassageTemp.setDuree(cursor.getString(cursor.getColumnIndex(TempsDePassage.KEY_DUREE)));

                listeContraintes.add(tempsDePassageTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + TempsDePassage.TABLE;
    }
}
