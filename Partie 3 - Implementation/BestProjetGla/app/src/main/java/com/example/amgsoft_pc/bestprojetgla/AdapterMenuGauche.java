package com.example.amgsoft_pc.bestprojetgla;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.autre.MenuGauche;

import java.util.List;

public class AdapterMenuGauche extends BaseAdapter {

    private Context context;
    private List<MenuGauche> lstItem;

    public AdapterMenuGauche(Context context, List<MenuGauche> lstItem) {
        this.context = context;
        this.lstItem = lstItem;
    }

    @Override
    public int getCount() {
        return lstItem.size();
    }

    @Override
    public Object getItem(int position) {
        return lstItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.menu_gauche, null);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.item_img);
        TextView tv = (TextView) convertView.findViewById(R.id.item_title);

        MenuGauche item = lstItem.get(position);
        img.setImageResource(item.getImgId());
        tv.setText(item.getTitle());

        return convertView;
    }
}
