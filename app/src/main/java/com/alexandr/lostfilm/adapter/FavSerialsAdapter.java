package com.alexandr.lostfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandr.lostfilm.Serial.SerialFavorite;
import com.example.alexandr.lostfilm.R;

import java.util.ArrayList;

/**
 * Created by alexandr on 22/05/16.
 */
public class FavSerialsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SerialFavorite> objects;

    public FavSerialsAdapter(Context context, ArrayList<SerialFavorite> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_favorite, parent, false);
        }

        SerialFavorite serial = getSerial(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.favTVname)).setText(serial.getName());
        ((TextView) view.findViewById(R.id.favTVdate)).setText(serial.getDate());
        ((ImageView) view.findViewById(R.id.imgFav)).setImageResource(R.mipmap.ic_launcher);


        return view;
    }

    SerialFavorite getSerial(int position) {
        return ((SerialFavorite) getItem(position));
    }
}
