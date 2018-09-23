package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienParticipants;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;

import java.util.ArrayList;

public class ManipulationLienParticipants {

    public static String creer() {
        return "CREATE TABLE " + LienParticipants.TABLE + "("
                + LienParticipants.KEY_ID_SORTIE + " INTEGER,"
                + LienParticipants.KEY_ID_AMI + " INTEGER,"
                + " FOREIGN KEY( " + LienParticipants.KEY_ID_SORTIE + " ) REFERENCES " + Sortie.TABLE + " ( " + Sortie.KEY_ID_SORTIE + " ),"
                + " FOREIGN KEY( " + LienParticipants.KEY_ID_AMI + " ) REFERENCES " + Ami.TABLE + " ( " + Ami.KEY_ID_AMI + " ) )";
    }

    public static long inserer(SQLiteDatabase db, LienParticipants lienParticipants) {
        ContentValues values = new ContentValues();

        values.put(LienParticipants.KEY_ID_SORTIE, lienParticipants.getIdSortie());
        values.put(LienParticipants.KEY_ID_AMI, lienParticipants.getIdAmi());

        return db.insertOrThrow(LienParticipants.TABLE, null, values);
    }

    public static ArrayList<LienParticipants> liste(SQLiteDatabase db) {
        ArrayList<LienParticipants> listeLienContraintesSorties = new ArrayList<LienParticipants>();

        String selectQuery = "SELECT * FROM " + LienParticipants.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LienParticipants lienParticipantsTemp = new LienParticipants();

                lienParticipantsTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(LienParticipants.KEY_ID_SORTIE)));
                lienParticipantsTemp.setIdAmi(cursor.getString(cursor.getColumnIndex(LienParticipants.KEY_ID_AMI)));

                listeLienContraintesSorties.add(lienParticipantsTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeLienContraintesSorties;
    }

    public static ArrayList<LienParticipants> liste(SQLiteDatabase db, String IDSortie) {
        ArrayList<LienParticipants> listeLienContraintesSorties = new ArrayList<LienParticipants>();

        String selectQuery = "SELECT * FROM " + LienParticipants.TABLE + " WHERE " + LienParticipants.KEY_ID_SORTIE + " = " + IDSortie;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LienParticipants lienParticipantsTemp = new LienParticipants();

                lienParticipantsTemp.setIdSortie(cursor.getString(cursor.getColumnIndex(LienParticipants.KEY_ID_SORTIE)));
                lienParticipantsTemp.setIdAmi(cursor.getString(cursor.getColumnIndex(LienParticipants.KEY_ID_AMI)));

                listeLienContraintesSorties.add(lienParticipantsTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeLienContraintesSorties;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + LienParticipants.TABLE;
    }
}
