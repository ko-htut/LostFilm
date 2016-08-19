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

import com.alexandr.lostfilm.adapter.AllSerialAdapter;
import com.alexandr.lostfilm.adapter.DividerItemDecoration;
import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.example.alexandr.lostfilm.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentAll extends Fragment  implements  SwipeRefreshLayout.OnRefreshListener{

    private List<AllSerials> serialList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AllSerialAdapter mAdapter;
    DB mDB;
    public static FragmentAll FRAGMENT_ALL;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentAll()
    {

    }

    public static FragmentAll newInstance()
    {
        //FragmentAll f = new FragmentAll();
        FRAGMENT_ALL=new FragmentAll();
       // return f;
        return FRAGMENT_ALL;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmemt_all, container, false);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_all);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
        mAdapter = new AllSerialAdapter(serialList);

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
                AllSerials movie = serialList.get(position);
                //Toast.makeText(getContext(), "click: "+movie.getRuName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                AllSerials movie = serialList.get(position);
                //Toast.makeText(getContext(),"long click: "+movie.getRuName(),Toast.LENGTH_SHORT).show();
            }
        }));

        refreshRecyclerView();

        return rootView;
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            mDB.open();
            mDB.addToFav(serialList.get(viewHolder.getAdapterPosition()).getRuName());
            mDB.close();
            serialList.remove(viewHolder.getAdapterPosition());
            FragmentFavorite.FRAGMENT_FAV.refreshRecyclerView();
            mAdapter.notifyDataSetChanged();

        }
    };

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragmentAll.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentAll.ClickListener clickListener) {
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
        mDB = new DB(getContext());
        mDB.open();

        Cursor serials = mDB.getAllSerials();
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

            do {
               serialList.add(new AllSerials(serials.getString(linkColIndex),serials.getString(ruNameColIndex),
                       serials.getString(engNamenameColIndex),serials.getString(imgBigColIndex),
                       serials.getString(imgSmallColIndex),serials.getString(episodeColIndex),
                       serials.getString(dateColIndex) ));
            } while (serials.moveToNext());
        } else
            Log.d("debug", "nothing to select");
        mAdapter.notifyDataSetChanged();
        serials.close();
        mDB.close();
    }
}
