package com.alexandr.lostfilm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alexandr.lostfilm.Serial.SerialFavorite;
import com.alexandr.lostfilm.adapter.FavSerialsAdapter;
import com.example.alexandr.lostfilm.R;

import java.util.ArrayList;

/**
 * Created by alexandr on 22/05/16.
 */
public class FragmentFavorite extends Fragment {

    ArrayList<SerialFavorite> serials = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_favorite, container, false);


        serials.add(new SerialFavorite(1,R.mipmap.ic_launcher,"Vikings","24.02.1993"));
        serials.add(new SerialFavorite(2,R.mipmap.ic_launcher,"Game of Thrones","24.02.1993"));

        FavSerialsAdapter adapter = new FavSerialsAdapter(getContext(),serials);
        ListView lvFav = (ListView) rootView.findViewById(R.id.lvFav);
        lvFav.setAdapter(adapter);
        return rootView;
    }
}
