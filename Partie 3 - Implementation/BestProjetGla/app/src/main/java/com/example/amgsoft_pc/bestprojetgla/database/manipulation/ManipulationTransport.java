package com.example.amgsoft_pc.bestprojetgla.database.manipulation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Transport;

import java.util.ArrayList;

public class ManipulationTransport {

    public static String creer() {
        return "CREATE TABLE " + Transport.TABLE + "("
                + Transport.KEY_ID_TRANSPORT + " INTEGER PRIMARY KEY NOT NULL,"
                + Transport.KEY_NOM + " TEXT,"
                + Transport.KEY_ICON + " INTEGER,"
                + Transport.KEY_ICON_GRIS + " INTEGER,"
                + Transport.KEY_QUERY + " TEXT,"
                + Transport.KEY_RAYON + " INTEGER)";
    }

    public static long inserer(SQLiteDatabase db, Transport tempsDePassage) {
        ContentValues values = new ContentValues();

        values.put(Transport.KEY_ID_TRANSPORT, tempsDePassage.getIdTransport());
        values.put(Transport.KEY_NOM, tempsDePassage.getNom());
        values.put(Transport.KEY_ICON, tempsDePassage.getIcon());
        values.put(Transport.KEY_ICON_GRIS, tempsDePassage.getIconGris());
        values.put(Transport.KEY_QUERY, tempsDePassage.getQuery());
        values.put(Transport.KEY_RAYON, tempsDePassage.getRayon());

        return db.insertOrThrow(Transport.TABLE, null, values);
    }

    public static Transport getAvecID(SQLiteDatabase db, String ID) {
        Transport transport = null;

        String selectQuery = "SELECT * FROM " + Transport.TABLE + " WHERE " + Transport.KEY_ID_TRANSPORT + "=" + ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            transport = new Transport();

            transport.setIdTransport(cursor.getString(cursor.getColumnIndex(Transport.KEY_ID_TRANSPORT)));
            transport.setNom(cursor.getString(cursor.getColumnIndex(Transport.KEY_NOM)));
            transport.setIcon(cursor.getString(cursor.getColumnIndex(Transport.KEY_ICON)));
            transport.setIconGris(cursor.getString(cursor.getColumnIndex(Transport.KEY_ICON_GRIS)));
            transport.setQuery(cursor.getString(cursor.getColumnIndex(Transport.KEY_QUERY)));
            transport.setRayon(cursor.getString(cursor.getColumnIndex(Transport.KEY_RAYON)));
        }

        cursor.close();

        return transport;
    }

    public static ArrayList<Transport> liste(SQLiteDatabase db) {
        ArrayList<Transport> listeTransports = new ArrayList<Transport>();

        String selectQuery = "SELECT * FROM " + Transport.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transport transport = new Transport();

                transport.setIdTransport(cursor.getString(cursor.getColumnIndex(Transport.KEY_ID_TRANSPORT)));
                transport.setNom(cursor.getString(cursor.getColumnIndex(Transport.KEY_NOM)));
                transport.setIcon(cursor.getString(cursor.getColumnIndex(Transport.KEY_ICON)));
                transport.setIconGris(cursor.getString(cursor.getColumnIndex(Transport.KEY_ICON_GRIS)));
                transport.setQuery(cursor.getString(cursor.getColumnIndex(Transport.KEY_QUERY)));
                transport.setRayon(cursor.getString(cursor.getColumnIndex(Transport.KEY_RAYON)));

                listeTransports.add(transport);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listeTransports;
    }

    public static String supprimer() {
        return "DROP TABLE IF EXISTS " + Transport.TABLE;
    }

    public static void remplir(SQLiteDatabase db) {
        db.execSQL(insertString(0, "Pied", R.drawable.transport_pieton, R.drawable.transport_pieton_gris, "walking", 3000));
        db.execSQL(insertString(1, "Vélo", R.drawable.transport_velo, R.drawable.transport_velo_gris, "bicycling", 23000));
        db.execSQL(insertString(2, "Voiture", R.drawable.transport_voiture, R.drawable.transport_voiture_gris, "driving", 50000));
    }

    private static String insertString(int ID, String nom, int icon, int iconGris, String query, int rayon) {
        return "INSERT INTO " + Transport.TABLE + " VALUES (" + ID + ", '" + nom + "' , " + icon + " ," + iconGris + " ,'" + query + "', " + rayon + " );";
    }
}
