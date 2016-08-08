package com.alexandr.lostfilm.fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.adapter.AllCursorLoader;
import com.example.alexandr.lostfilm.R;



public class FragmentAll extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {


    SimpleCursorAdapter scAdapter;


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



        // формируем столбцы сопоставления
        String[] from = new String[] { DB.ALL_COLUMN_RU_NAME };
        int[] to = new int[] { R.id.allTVname };

        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_all, null, from, to, 0);
        if (scAdapter.getCount()==0)
        {
            Toast.makeText(getContext(),"please weait",Toast.LENGTH_LONG).show();
        }
        ListView lvAll = (ListView) rootView.findViewById(R.id.lvAll);
        lvAll.setAdapter(scAdapter);

       getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AllCursorLoader(getContext(), new DB(getContext()));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        scAdapter.swapCursor(data);
        scAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }
}
