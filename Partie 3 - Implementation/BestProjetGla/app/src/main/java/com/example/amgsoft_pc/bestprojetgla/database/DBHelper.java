package com.example.amgsoft_pc.bestprojetgla.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAdresse;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationAmi;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesBase;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationContraintesPerso;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationLienContraintesSorties;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationLienFavoris;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationLienParticipants;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTempsDePassage;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTransport;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 20;

    private static final String DATABASE_NAME = "sqliteDBglobal.db";
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initialiseIcones(SQLiteDatabase db) {
        db.execSQL(ManipulationTypeEtape.supprimer());
        db.execSQL(ManipulationTypeEtape.creer());

        ManipulationTypeEtape.remplir(db);


        db.execSQL(ManipulationTransport.supprimer());
        db.execSQL(ManipulationTransport.creer());

        ManipulationTransport.remplir(db);


        db.execSQL(ManipulationContraintesBase.supprimer());
        db.execSQL(ManipulationContraintesBase.creer());

        ManipulationContraintesBase.remplir(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ManipulationAdresse.creer());
        db.execSQL(ManipulationAmi.creer());
        db.execSQL(ManipulationContraintesBase.creer());
        db.execSQL(ManipulationContraintesPerso.creer());
        db.execSQL(ManipulationEtape.creer());
        db.execSQL(ManipulationLienContraintesSorties.creer());
        db.execSQL(ManipulationLienFavoris.creer());
        db.execSQL(ManipulationLienParticipants.creer());
        db.execSQL(ManipulationSortie.creer());
        db.execSQL(ManipulationTempsDePassage.creer());
        db.execSQL(ManipulationTransport.creer());
        db.execSQL(ManipulationTypeEtape.creer());

        ManipulationTypeEtape.remplir(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onDowngrade(%d -> %d)", oldVersion, newVersion));

        db.execSQL(ManipulationAdresse.supprimer());
        db.execSQL(ManipulationAmi.supprimer());
        db.execSQL(ManipulationContraintesBase.supprimer());
        db.execSQL(ManipulationContraintesPerso.supprimer());
        db.execSQL(ManipulationEtape.supprimer());
        db.execSQL(ManipulationLienContraintesSorties.supprimer());
        db.execSQL(ManipulationLienFavoris.supprimer());
        db.execSQL(ManipulationLienParticipants.supprimer());
        db.execSQL(ManipulationSortie.supprimer());
        db.execSQL(ManipulationTempsDePassage.supprimer());
        db.execSQL(ManipulationTransport.supprimer());
        db.execSQL(ManipulationTypeEtape.supprimer());

        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        db.execSQL(ManipulationAdresse.supprimer());
        db.execSQL(ManipulationAmi.supprimer());
        db.execSQL(ManipulationContraintesBase.supprimer());
        db.execSQL(ManipulationContraintesPerso.supprimer());
        db.execSQL(ManipulationEtape.supprimer());
        db.execSQL(ManipulationLienContraintesSorties.supprimer());
        db.execSQL(ManipulationLienFavoris.supprimer());
        db.execSQL(ManipulationLienParticipants.supprimer());
        db.execSQL(ManipulationSortie.supprimer());
        db.execSQL(ManipulationTempsDePassage.supprimer());
        db.execSQL(ManipulationTransport.supprimer());
        db.execSQL(ManipulationTypeEtape.supprimer());

        onCreate(db);
    }

}