package com.alexandr.lostfilm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.database.FavSerials;
import com.alexandr.lostfilm.util.ConvertDensity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.alexandr.lostfilm.R;



import java.util.List;

/**
 * Created by alexandr on 22/05/16.
 */
public class FavSerialsAdapter extends RecyclerView.Adapter<FavSerialsAdapter.FavSerialViewHolder>{

    private List<FavSerials> listFav;
    private Context mCtx;
    private int imgSize=120;

    @Override
    public FavSerialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavSerialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavSerialViewHolder holder, int position) {

        final FavSerials serial = listFav.get(position);
        holder.ruName.setText(serial.getName());
        holder.ruDetail.setText(serial.getDescr_ru());
        holder.date.setText(serial.getDate());
        holder.episode.setText(serial.getSeason());
        Glide.with(mCtx)
                .load(serial.getPic_link())
               // .override(ConvertDensity.convertDpToPixel(imgSize), ConvertDensity.convertDpToPixel(imgSize)) // resize
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (serial.getPic_link().endsWith("jpg"))
                        {
                            serial.setPic_link(serial.getPic_link().replace("jpg","jpeg"));
                            Log.i("debugblide","подправленный: "+serial.getPic_link());
                        }
                        else
                        {
                            serial.setPic_link(serial.getPic_link().replace("jpeg","jpg"));
                            Log.i("debugblide","подправленный: "+serial.getPic_link());
                        }
                        DB db = new DB(mCtx);
                        db.openWritable();
                        db.updatePicLink(serial.getName(),serial.getPic_link());
                        db.close();
                        Glide.with(mCtx)
                                .load(serial.getPic_link())
                                .into(holder.img);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.img);
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

    public FavSerialsAdapter(List<FavSerials> favList,Context context) {

        this.listFav = favList;
        this.mCtx=context;
    }


}
