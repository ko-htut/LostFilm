package com.alexandr.lostfilm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.example.alexandr.lostfilm.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alexandr Novak on 18/08/16.
 */


public class AllSerialAdapter extends RecyclerView.Adapter<AllSerialAdapter.AllSerialViewHolder>{

    private List<AllSerials> listAll;
    private Context mCtx;

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

    public AllSerialAdapter(List<AllSerials> allList,Context context) {
        this.listAll = allList;
        this.mCtx=context;
    }

    @Override
    public AllSerialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all, parent, false);

        return new AllSerialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AllSerialViewHolder holder, int position) {
        final AllSerials serial = listAll.get(position);
        holder.ruName.setText(serial.getRuName());
        holder.engName.setText(serial.getEngName());
        holder.date.setText(serial.getDate());
        holder.episode.setText(serial.getEpisode());
        Picasso.with(mCtx).load(serial.getImg_small())
                .error(R.drawable.error)
                .resize(200,200)
                .into(holder.allImg, new Callback() {
                    boolean retry=false;
                    @Override
                    public void onSuccess() {
                        if (retry)
                        {
                            retry=false;
                            DB db = new DB(mCtx);
                            db.updatePicLink(serial.getRuName(),serial.getImg_small());
                            Log.i("debugPicasso","updated: "+serial.getRuName());
                        }
                    }
                    @Override
                    public void onError() {
                        Log.i("debugPicasso","error: "+serial.getImg_small());
                        /*if (serial.getImg_small().endsWith("jpg"))
                        {
                            serial.setImg_small(serial.getImg_small().replace("jpg","jpeg"));
                            retry=true;
                        }
                        if (serial.getImg_small().endsWith("jpeg"))
                        {
                            serial.setImg_small(serial.getImg_small().replace("jpeg","jpg"));
                            retry=true;
                        }*/

                        Log.i("debugPicasso",serial.getImg_small());
                        Picasso.with(mCtx).load(serial.getImg_big())
                                .error(R.drawable.error)
                                .resize(200,200)
                                .into(holder.allImg);

                    }
                });
    }

    @Override
    public int getItemCount() {
        return listAll.size();
    }
}
