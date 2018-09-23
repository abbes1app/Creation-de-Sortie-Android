package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.ContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.LienContraintesSorties;

import java.util.ArrayList;

public class ManipulationContraintesBase {

    public static String creer() {
        return "CREATE TABLE " + ContraintesBase.TABLE + "("
                + ContraintesBase.KEY_ID_CONTRAINTE_BASE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + ContraintesBase.KEY_NOM + " TEXT,"
                + ContraintesBase.KEY_IMAGE + " INTEGER,"
                + ContraintesBase.KEY_ID_TYPE + " INTEGER )";
    }

    public static long inserer(SQLiteDatabase db, ContraintesBase contrainte) {
        ContentValues values = new ContentValues();

        values.put(ContraintesBase.KEY_NOM, contrainte.getNom());
        values.put(ContraintesBase.KEY_IMAGE, contrainte.getImage());
        values.put(ContraintesBase.KEY_ID_TYPE, contrainte.getIdType());

        return db.insertOrThrow(ContraintesBase.TABLE, null, values);
    }

    public static long updateImage(SQLiteDatabase db, String IDContrainte, int image) {
        ContentValues values = new ContentValues();

        values.put(ContraintesBase.KEY_IMAGE, image);

        return db.update(ContraintesBase.TABLE, values, ContraintesBase.KEY_ID_CONTRAINTE_BASE + " = " + IDContrainte, null);
    }

    public static ContraintesBase getAvecID(SQLiteDatabase db, String ID) {
        ContraintesBase contrainteTemp = null;

        String selectQuery = "SELECT * FROM " + ContraintesBase.TABLE + " WHERE " + ContraintesBase.KEY_ID_CONTRAINTE_BASE + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            contrainteTemp = new ContraintesBase();

            contrainteTemp.setIdContrainteBase(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_CONTRAINTE_BASE)));
            contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_NOM)));
            contrainteTemp.setImage(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_IMAGE)));
            contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_TYPE)));
        }

        cursor.close();

        return contrainteTemp;
    }

    public static ArrayList<ContraintesBase> liste(SQLiteDatabase db) {
        ArrayList<ContraintesBase> listeContraintes = new ArrayList<ContraintesBase>();

        String selectQuery = "SELECT * FROM " + ContraintesBase.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContraintesBase contrainteTemp = new ContraintesBase();

                contrainteTemp.setIdContrainteBase(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_CONTRAINTE_BASE)));
                contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_NOM)));
                contrainteTemp.setImage(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_IMAGE)));
                contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_TYPE)));

                listeContraintes.add(contrainteTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static ArrayList<ContraintesBase> liste(SQLiteDatabase db, String IDType) {
        ArrayList<ContraintesBase> listeContraintes = liste(db);

        for(int i=0;i<listeContraintes.size();i++){
            if(listeContraintes.get(i).getIdType().compareTo(IDType) != 0){
                listeContraintes.remove(i);
                i--;
            }
        }

        return listeContraintes;
    }

    public static ArrayList<ContraintesPerso> listeAvecAvis(SQLiteDatabase db, String IDSortie, String IDType) {
        ArrayList<ContraintesPerso> listeContraintes = new ArrayList<ContraintesPerso>();

        String selectQuery = "SELECT * FROM " + ContraintesBase.TABLE + " LEFT OUTER JOIN " + LienContraintesSorties.TABLE +
                " ON " + ContraintesBase.KEY_ID_CONTRAINTE_BASE + " = " + LienContraintesSorties.KEY_ID_CONTRAINTE_BASE +
                " AND " + LienContraintesSorties.KEY_ID_SORTIE + " = " + IDSortie +
                " WHERE " + ContraintesBase.KEY_ID_TYPE + " = " + IDType;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ContraintesPerso contrainteTemp = new ContraintesPerso();

                contrainteTemp.setIdContraintePerso(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_CONTRAINTE_BASE)));
                contrainteTemp.setNom(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_NOM)));
                contrainteTemp.setAvis(cursor.getString(cursor.getColumnIndex(LienContraintesSorties.KEY_AVIS)));

                if(cursor.isNull(cursor.getColumnIndex(LienContraintesSorties.KEY_AVIS))){
                    contrainteTemp.setAvis("0");
                }

                contrainteTemp.setIdSortie(IDSortie);
                contrainteTemp.setIdType(cursor.getString(cursor.getColumnIndex(ContraintesBase.KEY_ID_TYPE)));

                listeContraintes.add(contrainteTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        System.out.println(listeContraintes.size());

        return listeContraintes;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + ContraintesBase.TABLE;
    }

    public static void remplir(SQLiteDatabase db) {
        db.execSQL(insertString("Kebab", R.drawable.contrainte_kebab, 0));
        db.execSQL(insertString("McDonald's", R.drawable.contrainte_mcdonalds, 0));
        db.execSQL(insertString("KFC", R.drawable.contrainte_kfc, 0));
        db.execSQL(insertString("Halal", R.drawable.contrainte_halal, 0));
        db.execSQL(insertString("Fruits de mer", R.drawable.contrainte_fruits_de_mer, 0));
        db.execSQL(insertString("Chinois", R.drawable.contrainte_chinois, 0));
        db.execSQL(insertString("Japonais", R.drawable.contrainte_japonais, 0));

        db.execSQL(insertString("Chic", R.drawable.contrainte_chic, 1));
        db.execSQL(insertString("Alcool", R.drawable.contrainte_alcool, 1));
        db.execSQL(insertString("Romantique", R.drawable.contrainte_romantique, 1));

        db.execSQL(insertString("Electro", R.drawable.contrainte_electro, 2));
        db.execSQL(insertString("Pas Chers", R.drawable.contrainte_pas_chers, 2));
        db.execSQL(insertString("VIP", R.drawable.contrainte_vip_club, 2));

        db.execSQL(insertString("Starbuck", R.drawable.contrainte_starbuck, 3));
        db.execSQL(insertString("Viennoiserie", R.drawable.contrainte_vienoiserie, 3));
        db.execSQL(insertString("Smothie", R.drawable.contrainte_smoothie, 3));
        db.execSQL(insertString("Léger", R.drawable.contrainte_leger, 3));
        db.execSQL(insertString("Alcool", R.drawable.contrainte_alcool, 3));

        db.execSQL(insertString("3D", R.drawable.contrainte_3d, 4));

        db.execSQL(insertString("Abstrait", R.drawable.contrainte_abstrait, 5));
        db.execSQL(insertString("Asiatique", R.drawable.contrainte_asiatique, 5));
        db.execSQL(insertString("Cubisme", R.drawable.contrainte_cubisme, 5));
        db.execSQL(insertString("Surréalisme", R.drawable.contrainte_surrealisme, 5));
        db.execSQL(insertString("Moyen-Age", R.drawable.contrainte_moyen_age, 5));
    }

    private static String insertString(String nom, int image, int type) {
        return "INSERT INTO " + ContraintesBase.TABLE + " VALUES ( NULL , \"" + nom + "\", " + image + ", " + type + ");";
    }
}
