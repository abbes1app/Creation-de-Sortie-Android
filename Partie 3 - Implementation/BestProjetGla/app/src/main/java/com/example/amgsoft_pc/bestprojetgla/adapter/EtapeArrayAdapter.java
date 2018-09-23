package com.example.amgsoft_pc.bestprojetgla.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Adresse;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Etape;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.amgsoft_pc.bestprojetgla.autre.LieuGraphe.msToStringNoYear;

public class EtapeArrayAdapter extends ArrayAdapter<Etape> {
    private static HashMap<String, Integer> listeIconEtape;
    private final Context context;
    private final ArrayList<Etape> listeEtape;
    private final ArrayList<Adresse> listeAdresse;
    private final int iconTransport;

    public EtapeArrayAdapter(Context context, ArrayList<Etape> listeEtape, ArrayList<Adresse> listeAdresse, int iconTransport) {
        super(context, R.layout.item_etape, listeEtape);
        this.context = context;

        this.listeEtape = listeEtape;
        this.listeAdresse = listeAdresse;

        if (listeIconEtape == null) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ArrayList<TypeEtape> listeType = ManipulationTypeEtape.liste(db);
            DatabaseManager.getInstance().closeDatabase();

            listeIconEtape = new HashMap<String, Integer>(listeType.size());
            for (int i = 0; i < listeType.size(); i++) {
                listeIconEtape.put(listeType.get(i).getIdType(), Integer.valueOf(listeType.get(i).getImage()));
            }
        }

        this.iconTransport = iconTransport;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_etape, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.texteArrive = (TextView) rowView.findViewById(R.id.texte_arrive);
            viewHolder.texteDepart = (TextView) rowView.findViewById(R.id.texte_depart);

            viewHolder.iconeType = (ImageView) rowView.findViewById(R.id.image_type_etape);

            viewHolder.texteNom = (TextView) rowView.findViewById(R.id.texte_nom_etape);
            viewHolder.texteAdresse = (TextView) rowView.findViewById(R.id.texte_adresse_etape);
            viewHolder.texteTelephone = (TextView) rowView.findViewById(R.id.texte_telephone_etape);

            viewHolder.texteDuree = (TextView) rowView.findViewById(R.id.texte_duree_trajet);

            viewHolder.iconeTransport = (ImageView) rowView.findViewById(R.id.image_transport);

            rowView.setTag(viewHolder);
        }


        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Etape etapeSelection = listeEtape.get(position);
        Adresse adresseSelection = listeAdresse.get(position);

        long arrive = Long.valueOf(etapeSelection.getDebut());
        holder.texteArrive.setText(msToStringNoYear(arrive));
        holder.texteDepart.setText(msToStringNoYear(Long.valueOf(etapeSelection.getFin())));

        if (position == 0) {
            holder.texteArrive.setVisibility(View.INVISIBLE);
            rowView.findViewById(R.id.texte_avant_arrive).setVisibility(View.INVISIBLE);
            holder.iconeType.setImageResource(R.drawable.etape_home);
        } else {
            holder.iconeType.setImageResource(listeIconEtape.get(etapeSelection.getIdType()));
        }

        holder.texteNom.setText(adresseSelection.getNom());
        holder.texteAdresse.setText(adresseSelection.getAdresse());
        holder.texteTelephone.setText(adresseSelection.getTelephone());


        if (position == 0) {
            ((LinearLayout) rowView.findViewById(R.id.layout_temps_trajet)).removeAllViews();
            rowView.findViewById(R.id.layout_temps_trajet).setVisibility(View.INVISIBLE);
        } else {
            long finPrecedent = Long.valueOf(listeEtape.get(position - 1).getFin());
            long attente = Long.valueOf(etapeSelection.getAttenteAvant());

            String temp = tempToString(arrive - finPrecedent - attente);
            if(attente>0){
                temp += "    (attente : " + tempToString(attente) + ")";
            }

            holder.texteDuree.setText(temp);
        }

        holder.iconeTransport.setImageResource(iconTransport);

        return rowView;
    }

    private String tempToString(long date){
        date = date/(1000*60);

        return (date/60) + "h" + ((date%60)<10 ? ("0" + (date%60)) : (date%60));
    }

    private static class ViewHolder {
        private TextView texteArrive;
        private TextView texteDepart;

        private ImageView iconeType;

        private TextView texteNom;
        private TextView texteAdresse;
        private TextView texteTelephone;

        private TextView texteDuree;

        private ImageView iconeTransport;
    }
}
