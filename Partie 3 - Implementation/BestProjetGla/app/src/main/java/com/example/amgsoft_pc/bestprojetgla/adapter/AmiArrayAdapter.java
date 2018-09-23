package com.example.amgsoft_pc.bestprojetgla.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.Ami;

import java.util.ArrayList;

public class AmiArrayAdapter extends ArrayAdapter<Ami> {
    private final Context context;
    private final ArrayList<Ami> listeAmis;

    public AmiArrayAdapter(Context context, ArrayList<Ami> listeAmis) {
        super(context, R.layout.item_ami, listeAmis);
        this.context = context;

        this.listeAmis = listeAmis;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_ami, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.texteNom = (TextView) rowView.findViewById(R.id.nom);
            viewHolder.textePrenom = (TextView) rowView.findViewById(R.id.prenom);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Ami amiSelection = listeAmis.get(position);

        holder.texteNom.setText(amiSelection.getNom());
        holder.textePrenom.setText(amiSelection.getPrenom());

        rowView.findViewById(R.id.bouton_ami_ajouter).setVisibility(View.INVISIBLE);

        return rowView;
    }

    private static class ViewHolder {
        private TextView texteNom;
        private TextView textePrenom;
    }
}
