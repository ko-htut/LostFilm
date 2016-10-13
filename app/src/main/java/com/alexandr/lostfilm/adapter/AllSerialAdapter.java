package com.alexandr.lostfilm.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.alexandr.lostfilm.R;


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
        private RelativeLayout layout;
        public AllSerialViewHolder(View view) {
            super(view);
            allImg = (ImageView) view.findViewById(R.id.allImg);
            ruName = (TextView) view.findViewById(R.id.allTVnameRu);
            engName = (TextView) view.findViewById(R.id.allTVnameEng);
            episode = (TextView) view.findViewById(R.id.allEpisode);
            //date = (TextView) view.findViewById(R.id.allDate);
            layout= (RelativeLayout) view.findViewById(R.id.item_all_relativelayout);
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
        //holder.date.setText(serial.getDate());
        holder.episode.setText(serial.getEpisode());
        //holder.layout.getHeight();
      //  Log.i("debugAllItem",holder.engName.getText()+" "+String.valueOf(holder.engName.getBottom()));
    //    Log.i("debugAllItem","layoutSize "+String.valueOf(holder.layout.getHeight()));
        //Log.i("golders layout","NAME: "+serial.getRuName()+" height: "+holder.layout.getHeight()+" width: "+holder.layout.getWidth());

        Glide.with(mCtx)
                .load(serial.getImg_small())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (serial.getImg_small().endsWith("jpg"))
                        {
                            serial.setImg_small(serial.getImg_small().replace("jpg","jpeg"));
                            Log.i("debugblide","подправленный: "+serial.getImg_small());
                        }
                        else
                        {
                            serial.setImg_small(serial.getImg_small().replace("jpeg","jpg"));
                            Log.i("debugblide","подправленный: "+serial.getImg_small());
                        }
                        DB db = new DB(mCtx);
                        db.openWritable();
                        db.updatePicLink(serial.getRuName(),serial.getImg_small());
                        db.close();
                        Glide.with(mCtx)
                                .load(serial.getImg_small())
                                .into(holder.allImg);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.allImg);

    }

    @Override
    public int getItemCount() {
        return listAll.size();
    }
}
