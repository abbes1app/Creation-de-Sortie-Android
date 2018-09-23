package com.example.amgsoft_pc.bestprojetgla.autre;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amgsoft_pc.bestprojetgla.R;

import java.util.List;

/**
 * Created by amgsoft-pc on 08/05/2017.
 */

public class CustomAdapter extends BaseAdapter{
        private Context context;
        private List<SpinnerModel> lstItem;

        public CustomAdapter(Context context, List<SpinnerModel> lstItem) {
            this.context = context;
            this.lstItem = lstItem;
        }




    public int getCount() {
            return lstItem.size();
        }

        public Object getItem(int position) {
            return lstItem.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_rows, null);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.endroitimg);
        TextView tv = (TextView) convertView.findViewById(R.id.endroittxt);

        SpinnerModel item = lstItem.get(position);
        img.setImageResource(item.getImg());
        tv.setText(item.getTxt());

        return convertView;
    }





}