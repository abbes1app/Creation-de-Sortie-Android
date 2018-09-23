package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienContraintesSorties;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;

import java.util.ArrayList;

public class ManipulationLienContraintesSorties {

    public static String creer() {
        return "CREATE TABLE " + LienContraintesSorties.TABLE + "("
                + LienContraintesSorties.KEY_ID_SORTIE + " INTEGER,"
                + LienContraintesSorties.KEY_AVIS + " INTEGER,"
                + LienContraintesSorties.KEY_ID_CONTRAINTE_BASE + " INTEGER,"
                + " FOREIGN KEY( " + LienContraintesSorties.KEY_ID_SORTIE + " ) REFERENCES " + Sortie.TABLE + " ( " + Sortie.KEY_ID_SORTIE + " ),"
                + " FOREIGN KEY( " + LienContraintesSorties.KEY_ID_CONTRAINTE_BASE + " ) REFERENCES " + ContraintesBase.TABLE + " ( " + ContraintesBase.KEY_ID_CONTRAINTE_BASE + " ) )";
    }

    public static long inserer(SQLiteDatabase db, LienContraintesSorties lienContraintesSorties) {
        ContentValues values = new ContentValues();

        values.put(LienContraintesSorties.KEY_ID_SORTIE, lienContraintesSorties.getIdSortie());
        values.put(LienContraintesSorties.KEY_AVIS, lienContraintesSorties.getAvis());
        values.put(LienContraintesSorties.KEY_ID_CONTRAINTE_BASE, lienContraintesSorties.getIdContrainteBase());

        return db.insertOrThrow(LienContraintesSorties.TABLE, null, values);
    }

    public static ArrayList<LienContraintesSorties> liste(SQLiteDatabase db) {
        ArrayList<LienContraintesSorties> listeLienContraintesSorties = new ArrayList<LienContraintesSorties>();

        String selectQuery = "SELECT * FROM " + LienContraintesSorties.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LienContraintesSorties lienContraintesSortiesTemp = new LienContraintesSorties();

                lienContraintesSortiesTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(LienContraintesSorties.KEY_ID_SORTIE)));
                lienContraintesSortiesTemp.setAvis(cursor.getString(cursor.getColumnIndex(LienContraintesSorties.KEY_AVIS)));
                lienContraintesSortiesTemp.setIdContrainteBase(cursor.getString(cursor.getColumnIndex(LienContraintesSorties.KEY_ID_CONTRAINTE_BASE)));

                listeLienContraintesSorties.add(lienContraintesSortiesTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeLienContraintesSorties;
    }

    public static void remove(SQLiteDatabase db, String IDSortie) {
        db.execSQL("DELETE FROM " + LienContraintesSorties.TABLE + " WHERE " +
                LienContraintesSorties.KEY_ID_SORTIE + " = " + IDSortie);
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + LienContraintesSorties.TABLE;
    }
}
