package com.example.amgsoft_pc.bestprojetgla.activity;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Sortie;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationSortie;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentCreerSortie;


public class CreationSortie extends FragmentActivity {

    String IDSortie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_creation_sortie);

        // Chargement d'une ID sortie
        Bundle b = getIntent().getExtras();
        // Si on a passé un IDSortie en parametre à cette activity on l'utilise
        if (b != null && b.containsKey("IDSortie")) {
            IDSortie = b.getString("IDSortie");
        } else { // Sinon on crée une nouvelle sortie

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            Sortie sortieTemp = new Sortie();
            IDSortie = Long.toString(ManipulationSortie.inserer(db, sortieTemp));
            DatabaseManager.getInstance().closeDatabase();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new FragmentCreerSortie();

        // Envoie de l'ID de sortie
        Bundle bundle = new Bundle();
        bundle.putString("IDSortie", IDSortie);
        fragment.setArguments(bundle);

        transaction.replace(R.id.main_content, fragment);
        transaction.commitAllowingStateLoss();
    }
}
