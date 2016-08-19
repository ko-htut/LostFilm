package com.alexandr.lostfilm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandr.lostfilm.database.AllSerials;
import com.example.alexandr.lostfilm.R;

import java.util.List;

/**
 * Created by Alexandr Novak on 18/08/16.
 */


public class AllSerialAdapter extends RecyclerView.Adapter<AllSerialAdapter.AllSerialViewHolder>{

    private List<AllSerials> listAll;

    public class AllSerialViewHolder extends RecyclerView.ViewHolder {
        public TextView ruName, engName, date,episode;
        public ImageView allImg;

        public AllSerialViewHolder(View view) {
            super(view);
            allImg = (ImageView) view.findViewById(R.id.allImg);
            ruName = (TextView) view.findViewById(R.id.allTVnameRu);
            engName = (TextView) view.findViewById(R.id.allTVnameEng);
            episode = (TextView) view.findViewById(R.id.allEpisode);
            date = (TextView) view.findViewById(R.id.allDate);
        }
    }

    public AllSerialAdapter(List<AllSerials> allList) {
        this.listAll = allList;
    }

    @Override
    public AllSerialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all, parent, false);

        return new AllSerialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllSerialViewHolder holder, int position) {
        AllSerials serial = listAll.get(position);
        holder.ruName.setText(serial.getRuName());
        holder.engName.setText(serial.getEngName());
        holder.date.setText(serial.getDate());
        holder.episode.setText(serial.getEpisode());
        //holder.allImg.setImageDrawable(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return listAll.size();
    }
}
