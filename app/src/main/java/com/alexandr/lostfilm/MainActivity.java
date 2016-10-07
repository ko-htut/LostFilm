package com.alexandr.lostfilm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.fragment.FragmentAll;
import com.alexandr.lostfilm.fragment.FragmentFavorite;

import com.alexandr.lostfilm.notification.CustomNotification;
import com.alexandr.lostfilm.receivers.CheckNewSerialsReceiver;
import com.alexandr.lostfilm.receivers.UpdateAllReceiver;
import com.alexandr.lostfilm.receivers.UpdateFavReceiver;
import com.alexandr.lostfilm.settings.SettingActivity;
import com.alexandr.lostfilm.task.CheckNewSerialsTask;
import com.alexandr.lostfilm.task.FavSerialUpdateTask;
import com.example.alexandr.lostfilm.R;

public class MainActivity extends AppCompatActivity implements FragmentAll.OnFavoriteListChanged, FragmentFavorite.OnAllListChanged {

    public FloatingActionButton fab;
    public ViewPager mViewPager;
    private String FIRST_RUN = "first_run";
    private Animation fab_fade_in;
    String Settings = "Settings";
    SectionsPagerAdapter mSectionsPagerAdapter;
    SharedPreferences sPrefSettings;
    AlarmManagerScheduler scheduler;
    Intent actionIntentAll;
    Intent actionIntentFav;
    Intent actionIntentCheckNew;
    Drawable fav;
    Drawable all;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        onAllListChange();
        onFavListChange();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sPrefSettings = getSharedPreferences(Settings, 0);
        if (sPrefSettings.getBoolean(FIRST_RUN, true)) {
            Log.i("debugFirstTime", "true");
            mViewPager.setCurrentItem(0);
            mViewPager.setCurrentItem(1);


            sPrefSettings.edit().putBoolean(FIRST_RUN, false).apply();
        } else {
            Log.i("debugFirstTime", "false");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduler = new AlarmManagerScheduler(getApplicationContext());
        sPrefSettings = getSharedPreferences(Settings, 0);
        scheduler.setAlarms();
        actionIntentAll = new Intent(getApplicationContext(), UpdateAllReceiver.class);
        actionIntentFav = new Intent(getApplicationContext(), UpdateFavReceiver.class);
        actionIntentCheckNew = new Intent(getApplicationContext(), CheckNewSerialsReceiver.class);

        if (sPrefSettings.getBoolean(FIRST_RUN, true)) {
            Log.i("debug_db", "mainActivityOnCreate");
            DB db = new DB(getApplicationContext());
            db.createDB();
            db.close();
        }

        setContentView(R.layout.activity_main);
        fab_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fav = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_refresh);
        all = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_input_add);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    fab.startAnimation(fab_fade_in);
                    fab.setImageDrawable(fav);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            favFabAction();
                        }
                    });
                }
                if (position == 1) {
                    fab.startAnimation(fab_fade_in);
                    fab.setImageDrawable(all);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allFabAction();
                        }
                    });
                }


            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favFabAction();
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void allFabAction() {
        String all = String.valueOf(scheduler.checkAlarm(actionIntentAll, PendingIntent.FLAG_UPDATE_CURRENT));
        String fav = String.valueOf(scheduler.checkAlarm(actionIntentFav, PendingIntent.FLAG_UPDATE_CURRENT));
        String check = String.valueOf(scheduler.checkAlarm(actionIntentCheckNew, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(this, "all:" + all + " fav:" + fav + " check:" + check, Toast.LENGTH_SHORT).show();
    }

    private void favFabAction() {
        FragmentFavorite fragmentFav = (FragmentFavorite)
                getSupportFragmentManager().findFragmentByTag(FragmentFavorite.FRAGMENT_TAG);
        if (fragmentFav != null) {
            fragmentFav.swipeRefreshLayout.setRefreshing(true);
        }
        FavSerialUpdateTask update = new FavSerialUpdateTask();
        update.execute(this);


    }

    @Override
    public void onFavListChange() {
        FragmentFavorite fragmentFav = (FragmentFavorite)
                getSupportFragmentManager().findFragmentByTag(FragmentFavorite.FRAGMENT_TAG);
        if (fragmentFav != null) {
            fragmentFav.refreshRecyclerView();
        }
    }

    @Override
    public void onAllListChange() {
        FragmentAll fragmentAll = (FragmentAll)
                getSupportFragmentManager().findFragmentByTag(FragmentAll.FRAGMENT_TAG);
        if (fragmentAll != null) {
            fragmentAll.refreshRecyclerView();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PageFragment.newInstance(position);
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Favorite";
                case 1:
                    return "All";

            }
            return null;
        }
    }
}
