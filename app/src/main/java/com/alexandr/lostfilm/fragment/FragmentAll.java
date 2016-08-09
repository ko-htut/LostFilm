package com.alexandr.lostfilm.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexandr.lostfilm.adapter.CursorObserver;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.adapter.AllCursorLoader;
import com.example.alexandr.lostfilm.R;

public class FragmentAll extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> , SwipeRefreshLayout.OnRefreshListener{


    SimpleCursorAdapter scAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FragmentAll()
    {

    }

    public static FragmentAll newInstance()
    {
        FragmentAll f = new FragmentAll();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragmemt_all, container, false);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_all);
        swipeRefreshLayout.setOnRefreshListener(this);
        // формируем столбцы сопоставления
        String[] from = new String[] { DB.ALL_COLUMN_RU_NAME };
        int[] to = new int[] { R.id.allTVname };

        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_all, null, from, to, 0);
//костыль
        DB db = new DB(getContext());
        db.open();
        Cursor c = db.getAllSerials();
        if (c.getCount()==0) {
            Toast.makeText(getContext(),"please wait",Toast.LENGTH_LONG).show();
        }
        else
        {

        }
        db.close();
        c.close();
//конец костыля


        ListView lvAll = (ListView) rootView.findViewById(R.id.lvAll);
        lvAll.setAdapter(scAdapter);

       getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("debugLoader","onCreateLoader");
        return new AllCursorLoader(getContext(), new DB(getContext()));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("debugLoader","onLoadFinished");
        scAdapter.swapCursor(data);

        /**
         * Registering content observer for this cursor, When this cursor value will be change
         * This will notify our loader to reload its data*/
        CursorObserver cursorObserver = new CursorObserver(new Handler(), loader);
        data.registerContentObserver(cursorObserver);
        data.setNotificationUri(getContext().getContentResolver(), DB.URI_TABLE_ALL);
        //scAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i("debugLoader","onLoaderReset");

    }

    @Override
    public void onRefresh() {
        Log.i("debugOnRefresh","onRefresh");
        swipeRefreshLayout.setRefreshing(true);
        Loader loader =getActivity().getSupportLoaderManager().getLoader(0);
        if (loader==null)
        {
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        }
        else
        {
            loader.forceLoad();
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
