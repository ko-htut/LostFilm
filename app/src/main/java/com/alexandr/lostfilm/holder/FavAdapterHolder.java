package com.alexandr.lostfilm.holder;

import com.alexandr.lostfilm.adapter.FavSerialsAdapter;

public class FavAdapterHolder {
    static FavSerialsAdapter adapter;

    public static void setAdapter(FavSerialsAdapter asa)
    {
        adapter=asa;
    }

    public static FavSerialsAdapter getAdapter()
    {
        return adapter;
    }
}
