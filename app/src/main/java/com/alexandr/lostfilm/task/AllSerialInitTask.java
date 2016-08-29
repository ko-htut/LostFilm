package com.alexandr.lostfilm.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.Loader;
import android.util.Log;

import com.alexandr.lostfilm.MainActivity;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.fragment.FragmentAll;
import com.alexandr.lostfilm.inet.HTTPUrl;
import com.alexandr.lostfilm.inet.WebParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AsyncTask will load all serials on first run
 */
public class AllSerialInitTask extends AsyncTask<MainActivity, Void, Void> {

    Context mCtx;
    MainActivity mActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mActivity.dialog.dismiss();


        mActivity.mViewPager.setCurrentItem(0);
        mActivity.mViewPager.setCurrentItem(1);
        if(mActivity.mViewPager.getCurrentItem() == 1) {
            FragmentAll frag = (FragmentAll) mActivity.mViewPager.getAdapter().instantiateItem(mActivity.mViewPager, mActivity.mViewPager.getCurrentItem());
            frag.refreshRecyclerView();}
    }

    @Override
    protected Void doInBackground(MainActivity... params) {
        mCtx=params[0].getApplicationContext();
        mActivity=params[0];
        WebParser wb = new WebParser(mCtx);
        wb.parseAllSerials();

        return null;
    }
}
