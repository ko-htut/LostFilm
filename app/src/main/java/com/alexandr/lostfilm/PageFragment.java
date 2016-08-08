package com.alexandr.lostfilm;

import android.support.v4.app.Fragment;

import com.alexandr.lostfilm.fragment.FragmentAll;
import com.alexandr.lostfilm.fragment.FragmentFavorite;

/**
 * Created by alexandr on 22/05/16.
 */

    public class PageFragment extends Fragment {

        static Fragment newInstance(int page) {
            PageFragment pageFragment = new PageFragment();
            switch (page) {
                case 0:
                    return new FragmentFavorite();
                case 1:
                    //return new FragmentAll();
                    return FragmentAll.newInstance();

            }
            return pageFragment;
        }



    }

