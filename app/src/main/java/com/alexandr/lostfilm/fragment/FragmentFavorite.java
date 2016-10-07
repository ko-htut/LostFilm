package com.alexandr.lostfilm.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alexandr.lostfilm.MainActivity;
import com.alexandr.lostfilm.adapter.AllSerialAdapter;
import com.alexandr.lostfilm.adapter.DividerItemDecoration;
import com.alexandr.lostfilm.adapter.FavSerialsAdapter;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.database.FavSerials;
import com.example.alexandr.lostfilm.R;

import java.util.ArrayList;


public class FragmentFavorite extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    ArrayList<FavSerials> serialList = new ArrayList<>();
    RecyclerView recyclerView;
    private FavSerialsAdapter mAdapter;
    DB mDB;
    public SwipeRefreshLayout swipeRefreshLayout;
    //public static FragmentFavorite FRAGMENT_FAV;
    public static String FRAGMENT_TAG;
    OnAllListChanged mCallback;

    public FragmentFavorite()
    {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnAllListChanged) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public static FragmentFavorite newInstance()
    {
        return new FragmentFavorite();
    }

    public interface OnAllListChanged {
        void onAllListChange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_favorite, container, false);


        FRAGMENT_TAG=this.getTag();
        Log.i("debugFragment","inside Fav tag="+FRAGMENT_TAG);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_fav);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fav_recycler_view);
        mAdapter = new FavSerialsAdapter(serialList,getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        refreshRecyclerView();

        return rootView;
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            Log.i("debug_db","called from FragmentFav, swipe");
            mDB= new DB(getContext());
            mDB.openWritable();
            mDB.delFromFav(serialList.get(viewHolder.getAdapterPosition()).getName());
            mDB.close();
            serialList.remove(viewHolder.getAdapterPosition());
            mCallback.onAllListChange();
            mAdapter.notifyDataSetChanged();
            //FragmentAll.FRAGMENT_ALL.refreshRecyclerView();

        }
    };

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragmentFavorite.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentFavorite.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child,rv.getChildAdapterPosition(child) ); //rv.getChildPosition(child)
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshRecyclerView()
    {
        Log.i("debug_db","called from FragmentFav, refresh");
        mDB = new DB(getContext());
        mDB.openReadOnly();

        Cursor serials = mDB.getFavSerials();
        serialList.clear();
        // AllSerials serial;
        if (serials.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int ruNameColIndex = serials.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
            int engNamenameColIndex = serials.getColumnIndex(DB.ALL_COLUMN_ENG_NAME);
            int dateColIndex = serials.getColumnIndex(DB.ALL_COLUMN_DATE);
            int imgSmallColIndex = serials.getColumnIndex(DB.ALL_COLUMN_SMALL_PICTURE);
            int imgBigColIndex = serials.getColumnIndex(DB.ALL_COLUMN_BIG_PICTURE);
            int episodeColIndex = serials.getColumnIndex(DB.ALL_COLUMN_LAST_EPISODE);
            int linkColIndex = serials.getColumnIndex(DB.ALL_COLUMN_LINK);
            int descr_ru = serials.getColumnIndex(DB.ALL_COLUMN_DETAIL_RU);
            int descr_eng = serials.getColumnIndex(DB.ALL_COLUMN_DETAIL_ENG);
            do {
                serialList.add(new FavSerials(serials.getString(episodeColIndex),serials.getString(ruNameColIndex),
                        serials.getString(imgSmallColIndex), serials.getString(descr_ru),serials.getString(descr_eng),
                        serials.getString(dateColIndex),serials.getString(imgBigColIndex)));
            } while (serials.moveToNext());
        } else
            Log.d("debug", "nothing to select");
        mAdapter.notifyDataSetChanged();
        serials.close();
        mDB.close();

    }
}
