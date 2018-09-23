package com.example.amgsoft_pc.bestprojetgla.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;

import java.util.ArrayList;

public class EtapeSimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;

    private final ArrayList<Integer> listeIconEtape;
    private final ArrayList<String> listeNomEtape;

    public EtapeSimpleArrayAdapter(Context context, ArrayList<Integer> listeIconEtape, ArrayList<String> listeNomEtape) {
        super(context, R.layout.item_etape_simple, listeNomEtape);
        this.context = context;

        this.listeIconEtape = listeIconEtape;
        this.listeNomEtape = listeNomEtape;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_etape_simple, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.iconeEtape = (ImageView) rowView.findViewById(R.id.icone_etape_simple);
            viewHolder.nomEtape = (TextView) rowView.findViewById(R.id.texte_etape_simple);

            rowView.setTag(viewHolder);
        }


        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.iconeEtape.setImageResource(listeIconEtape.get(position));

        holder.nomEtape.setText(listeNomEtape.get(position));

        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private static class ViewHolder {
        private ImageView iconeEtape;
        private TextView nomEtape;
    }
}
