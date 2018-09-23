package com.example.amgsoft_pc.bestprojetgla.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tan on 1/26/2016.
 */

public class DatabaseManager {
    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private Integer mOpenCounter = 0;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter += 1;
        if (mOpenCounter == 1) {
            // Opening new database
            Log.d("DataBase Manager", "nouvelle BD créée");
            mDatabase = mDatabaseHelper.getWritableDatabase();
        } else {
            Log.d("DataBase Manager", "Ancienne BD réutilisée");
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter -= 1;
        if (mOpenCounter == 0) {
            // Closing database
            Log.d("DataBase Manager", "On ferme la Db");
            mDatabase.close();
        } else {
            Log.d("DataBase Manager", "On ferme pas (il en reste)");
        }
    }
}