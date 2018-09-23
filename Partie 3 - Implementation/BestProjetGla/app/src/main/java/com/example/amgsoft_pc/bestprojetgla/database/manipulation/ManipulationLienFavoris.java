package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienFavoris;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;

import java.util.ArrayList;

public class ManipulationLienFavoris {

    public static String creer() {
        return "CREATE TABLE " + LienFavoris.TABLE + "("
                + LienFavoris.KEY_ID_TYPE + " INTEGER,"
                + LienFavoris.KEY_ID_ADRESSE + " INTEGER,"
                + " FOREIGN KEY( " + LienFavoris.KEY_ID_TYPE + " ) REFERENCES " + TypeEtape.TABLE + " ( " + TypeEtape.KEY_ID_TYPE + " ),"
                + " FOREIGN KEY( " + LienFavoris.KEY_ID_ADRESSE + " ) REFERENCES " + Adresse.TABLE + " ( " + Adresse.KEY_ID_ADRESSE + " ),"
                + " CONSTRAINT uniqueAdresse UNIQUE ( " + LienFavoris.KEY_ID_ADRESSE + " ) )";
    }

    public static long inserer(SQLiteDatabase db, LienFavoris lienFavoris) {
        ContentValues values = new ContentValues();

        values.put(LienFavoris.KEY_ID_TYPE, lienFavoris.getIdType());
        values.put(LienFavoris.KEY_ID_ADRESSE, lienFavoris.getIdAdresse());

        return db.insertOrThrow(LienFavoris.TABLE, null, values);
    }

    public static ArrayList<LienFavoris> liste(SQLiteDatabase db) {
        ArrayList<LienFavoris> listeLienContraintesSorties = new ArrayList<LienFavoris>();

        String selectQuery = "SELECT * FROM " + LienFavoris.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LienFavoris lienFavorisTemp = new LienFavoris();

                lienFavorisTemp.setIdType(cursor.getString(cursor.getColumnIndex(LienFavoris.KEY_ID_TYPE)));
                lienFavorisTemp.setIdAdresse(cursor.getString(cursor.getColumnIndex(LienFavoris.KEY_ID_ADRESSE)));

                listeLienContraintesSorties.add(lienFavorisTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeLienContraintesSorties;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + LienFavoris.TABLE;
    }
}
