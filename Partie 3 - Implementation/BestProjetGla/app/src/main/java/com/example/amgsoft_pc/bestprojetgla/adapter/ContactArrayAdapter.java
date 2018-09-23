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

public class ContactArrayAdapter extends ArrayAdapter<Ami> {
    private final Context context;
    private final ArrayList<Ami> listeAmis;

    public ContactArrayAdapter(Context context, ArrayList<Ami> listeAmis) {
        super(context, R.layout.item_contact, listeAmis);
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
            rowView = inflater.inflate(R.layout.item_contact, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.texteNP = (TextView) rowView.findViewById(R.id.name);
            viewHolder.textePhone = (TextView) rowView.findViewById(R.id.phone);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        if (listeAmis.get(position).getNom() != null) {
            holder.texteNP.setText(listeAmis.get(position).getPrenom() + " " + listeAmis.get(position).getNom());
        } else {
            holder.texteNP.setText(listeAmis.get(position).getPrenom());
        }
        holder.textePhone.setText(listeAmis.get(position).getTelephone());

        return rowView;
    }

    private static class ViewHolder {
        private TextView texteNP;
        private TextView textePhone;
    }
}
