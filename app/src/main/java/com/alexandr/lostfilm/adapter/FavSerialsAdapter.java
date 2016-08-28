package com.alexandr.lostfilm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alexandr.lostfilm.database.FavSerials;
import com.example.alexandr.lostfilm.R;


import java.util.List;

/**
 * Created by alexandr on 22/05/16.
 */
public class FavSerialsAdapter extends RecyclerView.Adapter<FavSerialsAdapter.FavSerialViewHolder>{

    private List<FavSerials> listFav;

    @Override
    public FavSerialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavSerialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavSerialViewHolder holder, int position) {
        FavSerials serial = listFav.get(position);
        holder.ruName.setText(serial.getName());
        holder.ruDetail.setText(serial.getDescr_ru());
        holder.date.setText(serial.getDate());
        holder.episode.setText(serial.getSeason());
        //holder.allImg.setImageDrawable(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return listFav.size();
    }


    public class FavSerialViewHolder extends RecyclerView.ViewHolder {
        public TextView ruName, ruDetail, date,episode;
        public ImageView img;

        public FavSerialViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.favImg);
            ruName = (TextView) view.findViewById(R.id.favTVnameRu);
            ruDetail = (TextView) view.findViewById(R.id.favTVDetailRu);
            episode = (TextView) view.findViewById(R.id.favEpisode);
            date = (TextView) view.findViewById(R.id.favDate);
        }
    }

    public FavSerialsAdapter(List<FavSerials> favList) {
        this.listFav = favList;
    }


}
