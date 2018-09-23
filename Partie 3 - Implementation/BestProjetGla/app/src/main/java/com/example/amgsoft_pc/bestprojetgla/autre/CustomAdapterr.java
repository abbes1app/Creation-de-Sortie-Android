package com.example.amgsoft_pc.bestprojetgla.autre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;

import java.util.List;

/**
 * Created by amgsoft-pc on 10/05/2017.
 */

public class CustomAdapterr extends BaseAdapter {

    public static final int TYPE_ODD = 0;
    public static final int TYPE_EVEN = 1;
    public static final int TYPE_ODD1 = 2;
    public static final int item = 3;
    public static final int TYPE_EVEN1 = 4;
    public static final int TYPE_EVEN3 = 5;
    public static final int text = 99;
    public static final int ligne = 7;




    LayoutInflater mInflater;



    private Context context;
    private List<ItemSlideMenu> lstItem;

    @Override
    public int getViewTypeCount() {
        return 100;
    }

    @Override
    public int getItemViewType(int position) {
        return lstItem.get(position).getType();
    }


    public CustomAdapterr(Context context, List<ItemSlideMenu> lstItem) {
        mInflater = LayoutInflater.from(context);
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

        int listViewItemType = getItemViewType(position);

        if (convertView == null) {



            if (listViewItemType == TYPE_EVEN3) {
                convertView = mInflater.inflate(R.layout.item_2,parent,false);
                ImageView img = (ImageView)convertView.findViewById(R.id.item_img1);
                TextView tv = (TextView)convertView.findViewById(R.id.item_title1);
                TextView tk = (TextView)convertView.findViewById(R.id.item_title2);

                ItemSlideMenu item = lstItem.get(position);
                img.setImageResource(item.getImgId());
                tv.setText(item.getTitle());
                tk.setText(item.getabbes());
            }


        }

        return convertView ;

    }}



