package com.example.amgsoft_pc.bestprojetgla.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amgsoft_pc.bestprojetgla.AdapterMenuGauche;
import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.autre.MenuGauche;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentAccueil;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentContact;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentCredits;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentListeAmis;
import com.example.amgsoft_pc.bestprojetgla.fragment.FragmentListeSorties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    long dernierBackPressed;
    private List<MenuGauche> listSliding;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    // Pour savoir
    private int itemActuel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        dernierBackPressed = Calendar.getInstance().getTimeInMillis() - 1000;

        //Init component
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        //Add item for sliding pastille_liste
        listSliding.add(new MenuGauche(R.drawable.menu_home, "Accueil"));
        listSliding.add(new MenuGauche(R.drawable.menu_carte, "Autour de moi"));
        listSliding.add(new MenuGauche(R.drawable.menu_cocktail, "Liste des sorties"));
        listSliding.add(new MenuGauche(R.drawable.menu_groupe_amis, "Liste des amis"));
        listSliding.add(new MenuGauche(R.drawable.menu_contact, "Contact"));
        listSliding.add(new MenuGauche(R.drawable.menu_info, "Credits"));

        AdapterMenuGauche adapter = new AdapterMenuGauche(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/close sliding pastille_liste
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //item selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);

        //Display fragment 1 when start
        replaceFragment(0, false);
        //Hanlde on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                replaceFragment(position);

                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
    }

    @Override
    protected void onResume() {
        listViewSliding.setItemChecked(itemActuel, true);

        if(itemActuel == 0){
            replaceFragment(0);
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        long heureMaintenant = Calendar.getInstance().getTimeInMillis();

        FragmentManager sfm = getSupportFragmentManager();

        String tag = sfm.findFragmentById(R.id.main_content).getTag();

        System.out.println(sfm.findFragmentById(R.id.main_content).getTag());
        System.out.println(sfm.getBackStackEntryCount());
        System.out.println("------------------------------------------------");

        if (drawerLayout.isDrawerOpen(listViewSliding)) {
            drawerLayout.closeDrawer(listViewSliding);
        } else if (itemActuel != 0 && sfm.getBackStackEntryCount() == 0) {
            itemActuel = 0;
            replaceFragment(0);
        } else if (sfm.getBackStackEntryCount() == 0 && dernierBackPressed < heureMaintenant - 1000) {
            dernierBackPressed = heureMaintenant;
            Toast.makeText(getBaseContext(), "Appuyez deux fois pour quiter", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

        System.out.println(sfm.findFragmentById(R.id.main_content).getTag());
        System.out.println(sfm.getBackStackEntryCount());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Create method replace fragment

    private void replaceFragment(int pos) {
        replaceFragment(pos, true);
    }

    private void replaceFragment(int pos, boolean anim) {

        Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new FragmentAccueil();
                break;
            case 1:
                Intent i2 = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i2);
                break;
            case 2:
                fragment = new FragmentListeSorties();
                break;
            case 3:
                fragment = new FragmentListeAmis();
                break;
            case 4:
                fragment = new FragmentContact();
                break;
            case 5:
                fragment = new FragmentCredits();
                break;

        }

        if (fragment != null) {
            itemActuel = pos;
            listViewSliding.setItemChecked(pos, true);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (!anim) {
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            }

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.add(R.id.main_content, fragment, "optionMenu");
            transaction.commitAllowingStateLoss();
        } else {
            listViewSliding.setItemChecked(itemActuel, true);
            //Toast.makeText(getBaseContext(), "Cet onglet n'existe pas encore", Toast.LENGTH_SHORT).show();
        }
    }
}







