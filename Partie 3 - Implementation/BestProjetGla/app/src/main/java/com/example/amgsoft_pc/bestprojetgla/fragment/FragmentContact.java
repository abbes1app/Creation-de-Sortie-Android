package com.example.amgsoft_pc.bestprojetgla.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.autre.CustomAdapterr;
import com.example.amgsoft_pc.bestprojetgla.autre.ItemSlideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContact extends Fragment {

    private List<ItemSlideMenu> listSliding33;
    String arg88 ;

    private ListView listViewSliding33;
    public FragmentContact() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_contact, container, false);
        listViewSliding33 = (ListView) rootView.findViewById(R.id.lv_sliding_menu33);
        listSliding33 = new ArrayList<>();
        String a = "Vous souhaitez nous poser une question sur l'application";
        String b ="Vous avez une idée d'évolution pour l'application";
        String c = "Vous avez rencontré un bug, constaté une anomalie ?";
        listSliding33.add(new ItemSlideMenu(R.drawable.question,"Question" ,a, CustomAdapterr.TYPE_EVEN3));
        listSliding33.add(new ItemSlideMenu(R.drawable.idea, "Idée", b, CustomAdapterr.TYPE_EVEN3));
        listSliding33.add(new ItemSlideMenu(R.drawable.anomalie, "Anomalie", c, CustomAdapterr.TYPE_EVEN3));


        CustomAdapterr customAdapter = new CustomAdapterr(getActivity(),listSliding33);
        listViewSliding33.setAdapter(customAdapter);


        listViewSliding33.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set title

                //item selected
                listViewSliding33.setItemChecked(position, true);
                //Replace fragment
                Intent intent,chooser;
//              intent=new Intent(Intent.ACTION_SENDTO);
                intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                String[] to={"mohammed.choukchou-braham@u-psud.fr","cedric.vachaudez@u-psud.fr","ismaila.ndiaye@u-psud.fr"};
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                TextView text = (TextView) view.findViewById(R.id.item_title1);
                String Sujet = text.getText().toString(); ;
                intent.putExtra(Intent.EXTRA_SUBJECT, Sujet);
                intent.setType("message/rfc822");
//              intent.setType("text/plain");
                chooser= Intent.createChooser(intent, "Envoyer un Email");
                startActivity(chooser);
                //Close menu

            }
        });
        return rootView ;
    }

}
