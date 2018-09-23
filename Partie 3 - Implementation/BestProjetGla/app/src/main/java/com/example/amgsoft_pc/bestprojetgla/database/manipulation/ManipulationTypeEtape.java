package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;

import java.util.ArrayList;

public class ManipulationTypeEtape {

    public static String creer() {
        return "CREATE TABLE " + TypeEtape.TABLE + "("
                + TypeEtape.KEY_ID_TYPE + " INTEGER PRIMARY KEY NOT NULL,"
                + TypeEtape.KEY_ARTICLE + " TEXT,"
                + TypeEtape.KEY_NOM + " TEXT,"
                + TypeEtape.KEY_TYPE + " TEXT,"
                + TypeEtape.KEY_IMAGE + " INTEGER,"
                + TypeEtape.KEY_DUREE + " INTEGER)";
    }

    public static long inserer(SQLiteDatabase db, TypeEtape typeEtape) {
        ContentValues values = new ContentValues();

        values.put(TypeEtape.KEY_ID_TYPE, typeEtape.getIdType());
        values.put(TypeEtape.KEY_ARTICLE, typeEtape.getNom());
        values.put(TypeEtape.KEY_NOM, typeEtape.getNom());
        values.put(TypeEtape.KEY_TYPE, typeEtape.getType());
        values.put(TypeEtape.KEY_IMAGE, typeEtape.getImage());
        values.put(TypeEtape.KEY_DUREE, typeEtape.getDuree());

        return db.insertOrThrow(TypeEtape.TABLE, null, values);
    }

    public static TypeEtape getAvecID(SQLiteDatabase db, String ID) {
        TypeEtape typeEtapeTemp = null;

        String selectQuery = "SELECT * FROM " + TypeEtape.TABLE + " WHERE " + TypeEtape.KEY_ID_TYPE + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            typeEtapeTemp = new TypeEtape();

            typeEtapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_ID_TYPE)));
            typeEtapeTemp.setArticle(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_ARTICLE)));
            typeEtapeTemp.setNom(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_NOM)));
            typeEtapeTemp.setType(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_TYPE)));
            typeEtapeTemp.setImage(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_IMAGE)));
            typeEtapeTemp.setDuree(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_DUREE)));
        }

        cursor.close();

        return typeEtapeTemp;
    }

    public static ArrayList<TypeEtape> liste(SQLiteDatabase db) {
        ArrayList<TypeEtape> listeContraintes = new ArrayList<TypeEtape>();

        String selectQuery = "SELECT * FROM " + TypeEtape.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TypeEtape typeEtapeTemp = new TypeEtape();

                typeEtapeTemp.setIdType(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_ID_TYPE)));
                typeEtapeTemp.setArticle(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_ARTICLE)));
                typeEtapeTemp.setNom(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_NOM)));
                typeEtapeTemp.setType(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_TYPE)));
                typeEtapeTemp.setImage(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_IMAGE)));
                typeEtapeTemp.setDuree(cursor.getString(cursor.getColumnIndex(TypeEtape.KEY_DUREE)));

                listeContraintes.add(typeEtapeTemp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeContraintes;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + TypeEtape.TABLE;
    }

    public static void remplir(SQLiteDatabase db) {
        db.execSQL(insertString(0, "au", "Restaurant", "restaurant", R.drawable.etape_restaurant, 5880000L));
        db.execSQL(insertString(1, "au", "Bar", "bar", R.drawable.etape_bar, 7200000L));
        db.execSQL(insertString(2, "en", "Boîte", "night_club", R.drawable.etape_boite, 14400000L));
        db.execSQL(insertString(3, "au", "Café", "cafe", R.drawable.etape_cafe, 2700000L));
        db.execSQL(insertString(4, "au", "Cinéma", "movie_theater", R.drawable.etape_cinema, 9000000L));
        db.execSQL(insertString(5, "au", "Musée", "museum", R.drawable.etape_musee, 10800000L));
    }

    private static String insertString(int ID, String article, String nom, String type, int image, long duree) {
        return "INSERT INTO " + TypeEtape.TABLE + " VALUES (" + ID + ", '" + article + "', '" + nom + "', '" + type + "', " + image + ", " + duree + ");";
    }
}
